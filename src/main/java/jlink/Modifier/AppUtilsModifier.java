/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jlink.Modifier;

import jlink.Common.AppUtilsCommon;
import jlink.Config.JumbfConfig;
import jlink.Const.AppConst;
import jlink.Const.JLINKConst;
import jlink.Const.MetaConst;
import jlink.JLINKBuilder.BuilderWeb;
import jlink.JLINKImage.JLINKImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.Graphics2D;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.mipams.jumbf.entities.BinaryDataBox;
import org.mipams.jumbf.entities.BmffBox;
import org.mipams.jumbf.entities.ContiguousCodestreamBox;
import org.mipams.jumbf.entities.JumbfBox;
import org.mipams.jumbf.entities.XmlBox;
import org.mipams.jumbf.services.CoreGeneratorService;
import org.mipams.jumbf.services.CoreParserService;
import org.mipams.jumbf.services.JpegCodestreamGenerator;
import org.mipams.jumbf.services.JpegCodestreamParser;
import org.mipams.jumbf.services.boxes.JumbfBoxService;
import org.mipams.jumbf.services.content_types.ContiguousCodestreamContentType;
import org.mipams.jumbf.services.content_types.XmlContentType;
import org.mipams.jumbf.util.MipamsException;
import org.mipams.privsec.entities.ProtectionDescriptionBox;
import org.mipams.privsec.entities.ReplacementDescriptionBox;
import org.mipams.privsec.services.content_types.ProtectionContentType;
import org.mipams.privsec.services.content_types.ReplacementContentType;
import org.mipams.jlink.services.JlinkContentType;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import jlink.Viewer.AppUtilsViewer;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
public class AppUtilsModifier {

    private Map<String, Integer> images;
    private int JLINK_box_id;
    private int cont_scene;
    private int cont_sprite;
    private JLINKImage scene;
    public Boolean decyrptionPossible = true;
    private SecretKeySpec secretKey = null;
    private IvParameterSpec ivSpec = null;

    public AppUtilsModifier() {
        images = new HashMap<>();
        JLINK_box_id = 1;
        cont_scene = 0;
        cont_sprite = 0;
    }

    public static void saveImage(String appPath, Part filePart, String image_title) throws Exception{

        String saveDir = appPath + File.separator + AppConst.MODIFIER_DIR;
        String savePath = saveDir + File.separator + image_title + ".jpeg";

        BufferedImage image = ImageIO.read(filePart.getInputStream());
        if(image != null){
            ImageIO.write(image, "jpeg", new File(savePath));
        } else {
            System.out.println("[ERROR] The image could not be read");
        }
    }

    public void renameFile(String old, String rename, String appPath) {
        File oldFile = new File(appPath + File.separator + AppConst.MODIFIER_DIR + File.separator + old + ".jpeg");
        File newFile = new File(appPath + File.separator + AppConst.MODIFIER_DIR + File.separator + rename + ".jpeg");

        if (oldFile.exists()) {
            oldFile.renameTo(newFile);
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

    public void saveCorrectImages(JLINKImage image) throws Exception{
        String replacement = image.getReplacement();
        if(image.getPossibleAction() && replacement != null) {

            String original = image.getTitle() + "_original";
            String img = image.getTitle();
            String repl = image.getTitle() + "_replacement";
            String aux = image.getTitle() + "_aux";

            if(replacement.equals("roi")) {
                renameFile(original, aux, image.getAppPath());
                renameFile(img, original, image.getAppPath());
                renameFile(aux, img, image.getAppPath());
            } else if (replacement.equals("replace_img")) {
                renameFile(original, repl, image.getAppPath());
            }
        }
    }

    public static void setProtection(JLINKImage image, Part filePart, String appPath, HttpServletRequest request) throws Exception{
        String title;
        int roi_x, roi_y, roi_width, roi_height;
        
        if(image.getReplacement() != null && image.getReplacement().equals("roi") && image.getChange()){

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
            String saveDir = appPath + File.separator + AppConst.MODIFIER_DIR + File.separator + image.getTitle() + "_original.jpeg";
            BufferedImage img = ImageIO.read(new File(saveDir));

            // Save R   
            BufferedImage R = img.getSubimage(roi_x, roi_y, roi_width, roi_height);
            String saveROI = appPath + File.separator + AppConst.MODIFIER_DIR + File.separator + title + ".jpeg";
            ImageIO.write(R, "jpeg", new File(saveROI));    

            // Create I' with R' and save I'
            Graphics2D graph = img.createGraphics();
            graph.drawImage(resized_image, roi_x, roi_y, roi_width, roi_height, null);
            graph.dispose();
            ImageIO.write(img, "jpeg", new File(appPath + File.separator + AppConst.MODIFIER_DIR + File.separator + image.getTitle() + ".jpeg"));

        }else if(image.getReplacement() != null && image.getReplacement().equals("replace_img") && image.getChange()){
            title = image.getTitle() + "_replacement";
            image.setReplacementImage(title);

            saveImage(appPath, filePart, title);
        } 
    }

    public void setJLINKImage(JLINKImage image, String file_name, String user, String role) throws Exception {
        
        AppUtilsCommon utils = new AppUtilsCommon(null, user, role);
        String fileUrl = image.getAppPath() + File.separator + AppConst.SAVE_DIR + File.separator + file_name + ".jpeg";

        ApplicationContext context = new AnnotationConfigApplicationContext(JumbfConfig.class);
        JpegCodestreamParser jpegCodestreamParser = context.getBean(JpegCodestreamParser.class);

        List<JumbfBox> jumbfBoxList = jpegCodestreamParser.parseMetadataFromFile(fileUrl);

        ((ConfigurableApplicationContext) context).close();

        JumbfBox mainJlinkBox = null;

        JlinkContentType contentType = new JlinkContentType();
        for (JumbfBox jumbfBox : jumbfBoxList) {
            if (contentType.getContentTypeUuid().equals(jumbfBox.getDescriptionBox().getUuid())) {
                mainJlinkBox = jumbfBox;
                break;
            }
        }

        if (mainJlinkBox == null) {
            throw new MipamsException("No JLink JUMBF Box found");
        }

        String hash = AppUtilsCommon.getFileHash(fileUrl);
        utils.setSecretKey(hash);
        this.parseJLINKFile(mainJlinkBox, image, hash, utils);

    }

    public JLINKImage parseJLINKFile(JumbfBox jlinkBox, JLINKImage img, String hash, AppUtilsCommon utils) throws Exception {

        String label, uuid;
        ProtectionDescriptionBox protectionDescriptionBox = null;
        BinaryDataBox binaryDataBox = null;
        ReplacementDescriptionBox replacementDescriptionBox = null;
        XmlBox xmlBoxProtect = null;
        ContiguousCodestreamBox jp2cBoxR = null;

        JumbfBox xmlContentJumbfBox = (JumbfBox) jlinkBox.getContentBoxList().get(0);

        //XmlBox xmlBox = (XmlBox) xmlContentJumbfBox.getContentBoxList().get(0);
        utils.getImageFromXmlBox(xmlContentJumbfBox, img);

        JlinkContentType jlinkContentType = new JlinkContentType();
        ContiguousCodestreamContentType jp2c = new ContiguousCodestreamContentType();
        ProtectionContentType protectionContentType = new ProtectionContentType();
        ReplacementContentType replacementContentType = new ReplacementContentType();
        XmlContentType xmlContentType = new XmlContentType();


        for (BmffBox contentBox : jlinkBox.getContentBoxList()) {
            JumbfBox contentJumbfBox = (JumbfBox) contentBox;
            uuid = contentJumbfBox.getDescriptionBox().getUuid();

            if (jlinkContentType.getContentTypeUuid().equals(uuid)) {
                JLINKImage aux = utils.locateLinkImage(img, contentJumbfBox);
                aux.setAppPath(img.getAppPath());
                aux = this.parseJLINKFile(contentJumbfBox, aux, hash, utils);
                aux.setAppPath(img.getAppPath());
                aux.setPrevious_image(img.getTitle());
            }

            label = contentJumbfBox.getDescriptionBox().getLabel();

            if (jp2c.getContentTypeUuid().equals(uuid)) {

                ContiguousCodestreamBox jp2cBox = (ContiguousCodestreamBox) contentJumbfBox
                        .getContentBoxList().get(0);

                if (label != null) {
                    utils.saveContent(jp2cBox, label, img.getAppPath(), AppConst.MODIFIER_DIR);

                    if(label.contains("image")) {
                        img.setLabel(label);
                        utils.saveContent(jp2cBox, label + "_original", img.getAppPath(), AppConst.MODIFIER_DIR);
                    }
                }
            }

            if (protectionContentType.getContentTypeUuid().equals(uuid)) {

                protectionDescriptionBox = (ProtectionDescriptionBox) contentJumbfBox.getContentBoxList().get(0);
                binaryDataBox = (BinaryDataBox) contentJumbfBox.getContentBoxList().get(1);

                if (label != null && !label.equals("replacementBox")) {
                    img.setLabel(label);
                } else if (label != null && label.equals("replacementBox")) {
                    img.setReplacement("True");
                }
            }

            if (replacementContentType.getContentTypeUuid().equals(uuid)) {

                replacementDescriptionBox = (ReplacementDescriptionBox) contentJumbfBox.getContentBoxList().get(0);
                jp2cBoxR = (ContiguousCodestreamBox) contentJumbfBox.getContentBoxList().get(1);

                if (label != null) {
                    img.setReplacementImage(label);
                    utils.saveContent(jp2cBoxR, label, img.getAppPath(), AppConst.MODIFIER_DIR);
                }
            }

            if (xmlContentType.getContentTypeUuid().equals(uuid)) {
                if (label.equals("access-rules-reference")){
                    xmlBoxProtect = (XmlBox) contentJumbfBox.getContentBoxList().get(0);
                }
            }
        }

        utils.checkProtection(img, protectionDescriptionBox, binaryDataBox, replacementDescriptionBox, xmlBoxProtect, AppConst.MODIFIER_DIR, "edit");

        renameFile(img.getLabel(), img.getTitle(), img.getAppPath());
        renameFile(img.getLabel() + "_replacement", img.getTitle() + "_replacement", img.getAppPath());
        renameFile(img.getLabel() +"_original", img.getTitle() + "_original", img.getAppPath());

        saveCorrectImages(img);
        return img;
    }

    public void deleteLINK(JLINKImage image, String deleted_image) {

        for (JLINKImage aux : image.getLinked_images()) {
            if (aux.getTitle().equals(deleted_image.replace("_", " "))) {
                image.getLinked_images().remove(aux);
                return;
            }
            this.deleteLINK(aux, deleted_image);
        }
    }

    public void containsTable(PrintWriter out, JLINKImage image) {
        // String extraImg = "";
        String rowStyle = "";

        String onClick = " onclick=\"window.location='SceneModifier?scene=" + image.getTitle() + "'\"";
        Boolean possible = image.getPossibleAction();
        String saveDir = AppConst.MODIFIER_DIR + File.separator + image.getTitle() + ".jpeg";

        if (!possible){
            rowStyle = " style='background-color:#828282 !important;'";
            onClick = "";
            saveDir = AppConst.MODIFIER_DIR + File.separator + image.getTitle() + "_original.jpeg";
        } 

        out.println("<tr onmouseover=\"showImg(this, " + possible + ")\" onmouseout=\"hideImg(this, "+ possible + ")\" data-value=\""
                + saveDir + "\" " 
                + onClick + rowStyle + ">");
        out.println("<td>" + image.getTitle() + "</td>");
        out.println("<td>" + image.getNote() + "</td>");

        if (image.isIsMain()) {
            out.println("<td>-</td>");
        } else {
            if (image.getPrevious_image() != null) {
                out.println("<td>" + image.getPrevious_image().replace("_", " ") + "</td>");
            }
        }
        if (image.getEncryption() != null && image.getEncryption().equals("AES256")) {
            out.println("<td>AES 256</td>");
        } else if (image.getEncryption() != null && image.getEncryption().equals("AES256IV")) {
            out.println("<td>AES 256 with IV</td>");
        } else {
            out.println("<td>Not Used</td>");
        }

         if (image.getReplacement() != null && image.getReplacement().equals("roi")) {
            out.println("<td>ROI</td>");
        } else if (image.getReplacement() != null && image.getReplacement().equals("replace_img")) {
            out.println("<td>Replacement Image</td>");
        } else {
            out.println("<td>Not Used</td>");
        }

        if (image.getViewAccess() != null) {
            out.println("<td>Used</td></tr>");
        } else {
            out.println("<td>Not Used</td></tr>");
        }

        for (JLINKImage aux : image.getLinked_images()) {
            this.containsTable(out, aux);
        }
    }

    public void formOptions(PrintWriter out, JLINKImage image) {

        if (image.isIsMain()) {
            out.println("<option selected>" + image.getTitle() + "</option>");
        } else {
            out.println("<option>" + image.getTitle()+ "</option>");
        }
        for (JLINKImage aux : image.getLinked_images()) {
            this.formOptions(out, aux);
        }
    }

    public void formDelete(PrintWriter out, JLINKImage image) {

        if (image.getLinked_images().isEmpty()) {
            out.println("<option>" + image.getTitle() + "</option>");
        }
        for (JLINKImage aux : image.getLinked_images()) {
            this.formDelete(out, aux);
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

    public void builder(JLINKImage image, boolean mainBox, BuilderWeb builder_file, String user) throws Exception {

        int JUMBF_box_id = 1;

        builder_file = new BuilderWeb(mainBox, builder_file);

        builder_file.setJLINKDescriptionBox(JLINKConst.SCENE_LABEL + cont_scene, JLINK_box_id);
        cont_scene++;
        JLINK_box_id++;

        builder_file.setXMLMetadata(image, AppConst.MODIFIER_DIR);
        if (image.getEncryption() != null && image.getSecretKey() == null) {
            getNewKey_IVSpec(image);

            AppUtilsCommon.setImagesSpecs(image, image.getSecretKey(), image.getIVSpec());
        }

        
        String basePath = image.getAppPath() + File.separator + AppConst.MODIFIER_DIR + File.separator;

        String imName = image.getTitle() + ".jpeg";
        String originalName = image.getTitle() + "_original.jpeg";
        String replacementName = image.getTitle() + "_replacement.jpeg";

        String cleanName = image.getTitle().replace(" ", "_") + ".jpeg";
        String cleanOriginalName = image.getTitle().replace(" ", "_") + "_original.jpeg";
        String cleanReplacementName = image.getTitle().replace(" ", "_") + "_replacement.jpeg";

        System.out.println(imName);
        BufferedImage im = ImageIO.read(new File(basePath + imName));
        if (im != null) {
            ImageIO.write(im, "jpeg", new File(basePath + cleanName));
        }
        

        File f = new File(basePath + originalName);
        if (f.exists())  {
            BufferedImage originalImage = ImageIO.read(f);
            ImageIO.write(originalImage, "jpeg", new File(basePath + cleanOriginalName));
        }
        

        if (image.getReplacement() != null) {
            BufferedImage replacementImage = ImageIO.read(new File(basePath + replacementName));
            ImageIO.write(replacementImage, "jpeg", new File(basePath + cleanReplacementName));
        }
        

        builder_file.addPrivacy(image, JUMBF_box_id, user, AppConst.MODIFIER_DIR);
        builder_file.addJUMBFContentBox(image, JUMBF_box_id, AppConst.MODIFIER_DIR);
        JUMBF_box_id++;

        if (!image.getLinked_images().isEmpty()) {
            builder_file.addJUMBFContentBoxForSprite(cont_sprite, JUMBF_box_id, image.getAppPath(),
                    image.getSprite_color());
            cont_sprite++;
        }

        for (JLINKImage aux : image.getLinked_images()) {
            this.builder(aux, false, builder_file, user);
        }

        if (!mainBox) {
            builder_file.getParent().getJlinkBoxBuilder()
                    .appendContentBox(builder_file.getJlinkBoxBuilder().getResult());
        }

        JumbfBox builderJumbfBox = builder_file.getJlinkBoxBuilder().getResult();
        String label = builderJumbfBox.getDescriptionBox().getLabel();

        String targetUrl = new String(
                image.getAppPath() + File.separator + AppConst.MODIFIER_DIR + File.separator + label + ".jumbf");

        ApplicationContext context = new AnnotationConfigApplicationContext(JumbfConfig.class);
        CoreGeneratorService generatorService = context.getBean(CoreGeneratorService.class);

        generatorService.generateJumbfMetadataToFile(List.of(builderJumbfBox), targetUrl);

        ((ConfigurableApplicationContext) context).close();
    }

    public String mergeFile(JLINKImage image, String merge_file) throws IOException, Exception {

        String image_fileName, jumbf_fileName, merge_fileName;

        image_fileName = image.getAppPath() + File.separator + AppConst.MODIFIER_DIR + File.separator
                + image.getTitle()+ ".jpeg";
        if(image.getReplacement().equals("replace_img")) {
            image_fileName = image.getAppPath() + File.separator + AppConst.MODIFIER_DIR + File.separator
                + image.getTitle() + "_replacement.jpeg";
        } else if (image.getEncryption() != null){

             image_fileName = image.getAppPath() + File.separator + AppConst.MODIFIER_DIR + File.separator
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

        jumbf_fileName = image.getAppPath() + File.separator + AppConst.MODIFIER_DIR + File.separator
                + JLINKConst.SCENE_LABEL + 0 + ".jumbf";
        merge_fileName = image.getAppPath() + File.separator + AppConst.SAVE_DIR + File.separator + merge_file
                + ".jpeg";

        ApplicationContext context = new AnnotationConfigApplicationContext(JumbfConfig.class);
        JpegCodestreamGenerator jpegCodestreamGenerator = context.getBean(JpegCodestreamGenerator.class);
        CoreParserService coreParserService = context.getBean(CoreParserService.class);

        List<JumbfBox> jumbfBoxList = coreParserService.parseMetadataFromFile(jumbf_fileName);
        jpegCodestreamGenerator.generateJumbfMetadataToFile(jumbfBoxList, image_fileName, merge_fileName);

        ((ConfigurableApplicationContext) context).close();

        return merge_fileName;
    }

    public String updateFileName(String file_name, String appPath) {

        StringBuilder name;
        char v;
        File f;
        Boolean file_not_exist;
        name = new StringBuilder(file_name);
        v = file_name.charAt(file_name.length() - 1);
        file_not_exist = true;

        if (file_name.contains("_v") && Character.isDigit(v)) {
            do {
                v++;
                System.out.println(v);
                name.setCharAt(file_name.length() - 1, v);
                f = new File(appPath + File.separator + AppConst.SAVE_DIR + File.separator + name + ".jpeg");
                if (!f.exists()) {
                    file_not_exist = false;
                }
            } while (file_not_exist);
        } else {
            name.append("_v1");
            f = new File(appPath + File.separator + AppConst.SAVE_DIR + File.separator + name + ".jpeg");
            if (f.exists()) {
                v = name.charAt(name.length() - 1);
                do {
                    v++;
                    System.out.println(v);
                    name.setCharAt(name.length() - 1, v);
                    f = new File(appPath + File.separator + AppConst.SAVE_DIR + File.separator + name + ".jpeg");
                    if (!f.exists()) {
                        file_not_exist = false;
                    }
                } while (file_not_exist);
            }
        }
        return name.toString();
    }

    public void getSceneByLabel(JLINKImage image, String label) {
        for (JLINKImage aux : image.getLinked_images()) {
            this.getSceneByLabel(aux, label);

        }
        if (image.getTitle().equals(label)) {
            scene = image;
        }
    }

    public JLINKImage getScene() {
        return scene;
    }

    public int getContScene() {
        cont_scene++;
        return cont_scene;
    }

    public void modifySceneInformation(JLINKImage image, String label, String title, String description,
            String duration, String sprite_color, HttpServletRequest request, Part filePart) throws FileNotFoundException, IOException, Exception {

        String old_title, old_version;
        char v;

        for (JLINKImage aux : image.getLinked_images()) {
            this.modifySceneInformation(aux, label, title, description, duration, sprite_color, request, filePart);
        }
        if (image.getTitle().equals(label)) {
            old_title = image.getTitle();
            image.setTitle(title);
            image.setNote(description);
            image.setLink_duration(duration);
            image.setSprite_color(sprite_color);
            old_version = image.getVersion();
            v = old_version.charAt(0);
            v++;
            image.setVersion(v + ".0.0");
    
            if (!old_title.equals(title)) {
                renameFile(old_title, title, image.getAppPath());
                renameFile(old_title + "_original", title + "_original", image.getAppPath());
                renameFile(old_title + "_replacement", title + "_replacement", image.getAppPath());
            }
        }
    }
}
