/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jlink.Creator;

import jlink.Common.AppUtilsCommon;
import jlink.Config.JumbfConfig;
import jlink.Const.AppConst;
import jlink.Const.JLINKConst;
import jlink.JLINKBuilder.BuilderWeb;
import jlink.JLINKImage.JLINKImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.security.MessageDigest;

import org.mipams.jumbf.entities.JumbfBox;
import org.mipams.jumbf.services.CoreGeneratorService;
import org.mipams.jumbf.services.CoreParserService;
import org.mipams.jumbf.services.JpegCodestreamGenerator;
import org.mipams.privsec.config.PrivsecConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpServletRequest;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.Graphics2D;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
public class AppUtilsCreator {

    private Map<String, Integer> images;
    private int JLINK_box_id;
    private int cont_scene;
    private int cont_sprite;
    private SecretKeySpec secretKey;
    private IvParameterSpec ivSpec;

    public AppUtilsCreator() {
        images = new HashMap<>();
        JLINK_box_id = 1;
        cont_scene = 0;
        cont_sprite = 0;
        secretKey = null;
        ivSpec = null;
    }

    public static void saveImage(String appPath, Part filePart, String image_title) throws Exception{

        String saveDir = appPath + File.separator + AppConst.CREATOR_DIR;
        String savePath = saveDir + File.separator + image_title.replace(" ", "_") + ".jpeg";

        BufferedImage image = ImageIO.read(filePart.getInputStream());
        if(image != null){
            ImageIO.write(image, "jpeg", new File(savePath));
        } else {
            System.out.println("[ERROR] The image could not be read");
        }
    }

    public void getNewKey_IVSpec(JLINKImage image){
        byte[] key = new byte[32];
        SecureRandom random = new SecureRandom();
        random.nextBytes(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        byte[] iv = new byte[16];
        if (image.getEncryption() == "AES256IV")
            random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        image.setSecretKey(secretKey);
        image.setIVSpec(ivSpec);
    }

    public static void encryptBuffImage(String encryption, SecretKeySpec secretKey, IvParameterSpec ivSpec, BufferedImage image, String savePath) throws Exception{

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(image, "jpeg", bytes);
        byte [] image_bytes = bytes.toByteArray();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
        byte[] encrypted_image = cipher.doFinal(image_bytes);
        
        try (FileOutputStream fileOutStream = new FileOutputStream(savePath)) {
            fileOutStream.write(encrypted_image);
        }

    }

    public static BufferedImage resizeImage(BufferedImage original_image, int width, int height) {

        int type = original_image.getType();

        if(type == 0){
            type = BufferedImage.TYPE_INT_RGB;
        }
        BufferedImage resized_image = new BufferedImage(width, height, type);
        Graphics2D g = resized_image.createGraphics();
        g.drawImage(original_image, 0, 0, width, height, null);
        g.dispose();

        return resized_image;
    }

    public static void setProtection(JLINKImage image, Part filePart, String appPath, HttpServletRequest request) throws Exception{
        String title;
        int roi_x, roi_y, roi_width, roi_height;
        
        if(image.getReplacement() != null && image.getReplacement().equals("roi")){

            roi_x = Integer.parseInt(request.getParameter("roi_x"));
            roi_y = Integer.parseInt(request.getParameter("roi_y"));
            image.setROI_region_X(roi_x);
            image.setROI_region_Y(roi_y);

            roi_width = Integer.parseInt(request.getParameter("roi_width"));
            roi_height = Integer.parseInt(request.getParameter("roi_height"));

            title = image.getTitle() + "_replacement";
            image.setReplacementImage(title);

            // Resize ROI replacement (R')
            BufferedImage original_image = ImageIO.read(filePart.getInputStream());
            BufferedImage resized_image = resizeImage(original_image, roi_width, roi_height);

            // Get I
            String saveDir = appPath + File.separator + AppConst.CREATOR_DIR + File.separator + image.getTitle().replace(" ", "_") + ".jpeg";
            BufferedImage img = ImageIO.read(new File(saveDir));

            // Save R
            BufferedImage R = img.getSubimage(roi_x, roi_y, roi_width, roi_height);
            String saveROI = appPath + File.separator + AppConst.CREATOR_DIR + File.separator + title + ".jpeg";
            ImageIO.write(R, "jpeg", new File(saveROI)); 

            // Create I' with R' and save I'
            Graphics2D graph = img.createGraphics();
            graph.drawImage(resized_image, roi_x, roi_y, roi_width, roi_height, null);
            graph.dispose();
            ImageIO.write(img, "jpeg", new File(saveDir));
 

        }else if(image.getReplacement() != null && image.getReplacement().equals("replace_img")){
            title = image.getTitle() + "_replacement";
            image.setReplacementImage(title);

            saveImage(appPath, filePart, title);
        }
    }

    public void insertLINK(JLINKImage source, JLINKImage destination) {

        if ((source.getTitle().equals(destination.getPrevious_image().replace("_", " "))
                && (!source.getLinked_images().contains(destination)))) {
            source.addLink(destination);
            return;
        }
        for (JLINKImage aux : source.getLinked_images()) {
            this.insertLINK(aux, destination);
        }
    }

    public void createFile(JLINKImage image, String merge_file, String user) throws Exception {

        this.setMetadataParam(image);
        cont_scene = 0;
        cont_sprite = 0;
        this.builder(image, true, null, user);
        String path = this.mergeFile(image, merge_file);
        if (image.getEncryption() != null) {
            AppUtilsCommon.saveKey(path, image);
        }
    }

    public void setDefaultParam(JLINKImage image) throws Exception {

        image.setViewport_Id(JLINKConst.DEFAULT_VIEWPORT_ID);
        image.setViewport_X(JLINKConst.DEFAULT_VIEWPORT_X);
        image.setViewport_Y(JLINKConst.DEFAULT_VIEWPORT_Y);
        image.setViewport_xFov(JLINKConst.DEFAULT_VIEWPORT_XFOV);
        image.setViewport_yFov(JLINKConst.DEFAULT_VIEWPORT_YFOV);
        image.setImage_format(JLINKConst.IMAGE_FORMAT_JPEG);
        image.setImage_Href(JLINKConst.JUMBF_URI + image.getLabel());
        image.setLink_region_shape(JLINKConst.DEFAULT_LINK_SHAPE);
        image.setLink_region_W(JLINKConst.DEFAULT_LINK_W);
        image.setLink_region_H(JLINKConst.DEFAULT_LINK_H);
        image.setLink_region_rotation(JLINKConst.DEFAULT_LINK_ROTATION);
        image.setLink_duration(JLINKConst.DEFAULT_LINK_DURATION);
        image.setLink_Vpid(JLINKConst.DEFAULT_LINK_VPID);
    }

    public void setMetadataParam(JLINKImage image) throws Exception {
        image.setLabel(JLINKConst.IMAGE_LABEL + cont_scene);
        this.setDefaultParam(image);
        if (image.isIsMain()) {
            image.setLink_region_X("");
            image.setLink_region_Y("");
            image.setLink_to("");
        } else {
            image.setLink_to(JLINKConst.JUMBF_URI + JLINKConst.SCENE_LABEL
                    + images.get(image.getTitle().replace(" ", "_")));
            System.out.println(String.format("I am going to write %s", image.getLink_to()));
        }
        images.put(image.getTitle().replace(" ", "_"), cont_scene);
        if (!image.getLinked_images().isEmpty()) {
            image.setLink_sprite(JLINKConst.JUMBF_URI + JLINKConst.SPRITE_LABEL + cont_sprite);
            cont_sprite++;
        } else {
            image.setLink_sprite("");
        }
        for (JLINKImage aux : image.getLinked_images()) {
            cont_scene++;
            images.put(aux.getTitle().replace(" ", "_"), cont_scene);
            this.setMetadataParam(aux);
        }
    }

    public void builder(JLINKImage image, boolean mainBox, BuilderWeb builder_file, String user) throws Exception {

        int JUMBF_box_id;

        builder_file = new BuilderWeb(mainBox, builder_file);

        JUMBF_box_id = 1;
        builder_file.setJLINKDescriptionBox(JLINKConst.SCENE_LABEL + cont_scene, JLINK_box_id);
        cont_scene++;
        JLINK_box_id++;
        builder_file.setXMLMetadata(image, AppConst.CREATOR_DIR);
        if (image.getEncryption() != null && image.getSecretKey() == null) {
            this.getNewKey_IVSpec(image);

            AppUtilsCommon.setImagesSpecs(image, image.getSecretKey(), image.getIVSpec());
        }
        builder_file.addPrivacy(image, JUMBF_box_id, user, AppConst.CREATOR_DIR);
        builder_file.addJUMBFContentBox(image, JUMBF_box_id, AppConst.CREATOR_DIR);
        JUMBF_box_id++;
        if (!image.getLinked_images().isEmpty()) {
            builder_file.addJUMBFContentBoxForSprite(cont_sprite, JUMBF_box_id, image.getAppPath(),
                    image.getSprite_color());
            cont_sprite++;
        }

        for (JLINKImage aux : image.getLinked_images()) {
            this.builder(aux, false, builder_file, user);
        }

        JumbfBox builderJumbfBox = builder_file.getJlinkBoxBuilder().getResult();

        if (!mainBox) {
            builder_file.getParent().getJlinkBoxBuilder().appendContentBox(builderJumbfBox);
        }

        String label = builderJumbfBox.getDescriptionBox().getLabel();

        String targetUrl = image.getAppPath() + File.separator + AppConst.CREATOR_DIR + File.separator + label
                + ".jumbf";

        ApplicationContext context = new AnnotationConfigApplicationContext(JumbfConfig.class);
        CoreGeneratorService generatorService = context.getBean(CoreGeneratorService.class);

        generatorService.generateJumbfMetadataToFile(List.of(builderJumbfBox), targetUrl);

        ((ConfigurableApplicationContext) context).close();
    }

    public String mergeFile(JLINKImage image, String merge_file) throws IOException, Exception {
        String image_fileName, jumbf_fileName, merge_fileName;

        image_fileName = image.getAppPath() + File.separator + AppConst.CREATOR_DIR + File.separator
                + image.getTitle().replace(" ", "_") + ".jpeg";
        if(image.getReplacement() != null && image.getReplacement().equals("replace_img")) {
            image_fileName = image.getAppPath() + File.separator + AppConst.CREATOR_DIR + File.separator
                + image.getTitle().replace(" ", "_") + "_replacement.jpeg";
        } else if (image.getEncryption() != null){
            image_fileName = image.getAppPath() + File.separator + AppConst.CREATOR_DIR + File.separator
                + "protected_content.jpeg";

            InputStream inputStream = AppUtilsCommon.class.getClassLoader().getResourceAsStream(AppConst.PROTECTED_CONTENT_PATH);
            try (OutputStream outputStream = new FileOutputStream(image_fileName)) {
                byte[] bytes = new byte[1024];
                int read;

                while ((read = inputStream.read(bytes)) != -1){
                    outputStream.write(bytes, 0, read);
                }
            }
        } 
        
        jumbf_fileName = image.getAppPath() + File.separator + AppConst.CREATOR_DIR + File.separator
                + JLINKConst.SCENE_LABEL + 0 + ".jumbf";
        merge_fileName = image.getAppPath() + File.separator + AppConst.SAVE_DIR + File.separator + merge_file
                + ".jpeg";

        ApplicationContext context = new AnnotationConfigApplicationContext(PrivsecConfig.class, JumbfConfig.class);
        JpegCodestreamGenerator jpegCodestreamGenerator = context.getBean(JpegCodestreamGenerator.class);
        CoreParserService coreParserService = context.getBean(CoreParserService.class);

        List<JumbfBox> jumbfBoxList = coreParserService.parseMetadataFromFile(jumbf_fileName);

        System.out.println(jumbfBoxList.toString());
        jpegCodestreamGenerator.generateJumbfMetadataToFile(jumbfBoxList, image_fileName, merge_fileName);

        ((ConfigurableApplicationContext) context).close();

        return merge_fileName;
    }
}
