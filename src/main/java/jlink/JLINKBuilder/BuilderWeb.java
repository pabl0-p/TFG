/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jlink.JLINKBuilder;

import jlink.Common.AppUtilsCommon;
import jlink.Config.JumbfConfig;
import jlink.Const.AppConst;
import jlink.Const.JLINKConst;
import jlink.Creator.AppUtilsCreator;
import jlink.JLINKImage.JLINKImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore.SecretKeyEntry;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import org.mipams.jumbf.entities.BinaryDataBox;
import org.mipams.jumbf.entities.ContiguousCodestreamBox;
import org.mipams.jumbf.entities.JumbfBox;
import org.mipams.jumbf.entities.JumbfBoxBuilder;
import org.mipams.jumbf.entities.XmlBox;
import org.mipams.jumbf.services.CoreGeneratorService;
import org.mipams.jumbf.services.content_types.ContiguousCodestreamContentType;
import org.mipams.jumbf.services.content_types.XmlContentType;
import org.mipams.jumbf.util.MipamsException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jakarta.servlet.http.HttpSession;

import org.mipams.jlink.entities.JlinkElement;
import org.mipams.jlink.entities.JlinkImage;
import org.mipams.jlink.entities.JlinkLink;
import org.mipams.jlink.entities.JlinkRegion;
import org.mipams.jlink.entities.JlinkScene;
import org.mipams.jlink.entities.JlinkViewport;
import org.mipams.jlink.services.JlinkContentType;
import org.mipams.jlink.services.JlinkXmlGenerator;

import org.mipams.privsec.entities.ProtectionDescriptionBox;
import org.mipams.privsec.entities.ReplacementDescriptionBox;
import org.mipams.privsec.entities.replacement.FileParamHandler;
import org.mipams.privsec.entities.replacement.ReplacementType;
import org.mipams.privsec.entities.replacement.RoiParamHandler;
import org.mipams.privsec.services.content_types.ProtectionContentType;
import org.mipams.privsec.services.content_types.ReplacementContentType;
import java.awt.image.BufferedImage;
import java.nio.file.Path;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
public class BuilderWeb {

    private final boolean isMainBox;
    private final JumbfBoxBuilder jlinkBoxBuilder;
    private final BuilderWeb parent;

    public BuilderWeb(boolean mainBox, BuilderWeb parent) throws Exception {
        this.isMainBox = mainBox;
        this.jlinkBoxBuilder = new JumbfBoxBuilder(new JlinkContentType());
        this.parent = parent;
    }

    public void setJLINKDescriptionBox(String label, int id) throws Exception {
        jlinkBoxBuilder.setLabel(label);
        jlinkBoxBuilder.setId(id);
    }

    public void setXMLMetadata(JLINKImage image, String dir) throws Exception {
        // From JLINKImage to XML
        JlinkElement element = getJlinkElement(image);
        ApplicationContext context = null;
        XmlBox xmlBox = null;
        try{
            context = new AnnotationConfigApplicationContext(JumbfConfig.class);
            JlinkXmlGenerator jlinkXmlGenerator = context.getBean(JlinkXmlGenerator.class);

            xmlBox = jlinkXmlGenerator.getXmlBoxFromJlinkElement(element);
            ((ConfigurableApplicationContext) context).close();

        } catch(MipamsException e){
            e.printStackTrace();
            ((ConfigurableApplicationContext) context).close();
            throw new Exception(e);
        }

        JumbfBoxBuilder jumbfBoxBuilder = new JumbfBoxBuilder(new XmlContentType());

        jumbfBoxBuilder.setJumbfBoxAsRequestable();
        jumbfBoxBuilder
                .setLabel("XML Metadata file in JLINK: " + jlinkBoxBuilder.getResult().getDescriptionBox().getLabel());
        jumbfBoxBuilder.setId(1);
        jumbfBoxBuilder.appendContentBox(xmlBox);

        jlinkBoxBuilder.appendContentBox(jumbfBoxBuilder.getResult());
    }

    private JlinkElement getJlinkElement(JLINKImage image) {

        JlinkElement element = new JlinkElement();
        element.setNextId(1);

        JlinkScene scene = new JlinkScene();
        scene.setVersion(image.getVersion());
        scene.setTitle(image.getTitle());
        scene.setNote(image.getNote());

        JlinkImage jlinkImage = new JlinkImage();
        jlinkImage.setFormat(image.getImage_format());
        jlinkImage.setHref(image.getImage_Href());


        JlinkViewport jlinkViewport = new JlinkViewport();
        jlinkViewport.setId(image.getViewport_Id());
        jlinkViewport.setX(Double.parseDouble(image.getViewport_X()));
        jlinkViewport.setY(Double.parseDouble(image.getViewport_Y()));
        jlinkViewport.setXfov(Double.parseDouble(image.getViewport_xFov()));
        jlinkViewport.setYfov(Double.parseDouble(image.getViewport_yFov()));

        scene.setImage(jlinkImage);
        scene.getViewports().add(jlinkViewport);
        element.setScene(scene);

        System.out.println(String.format("Image %s has %d linked images", image.getLabel(), image.getLinked_images().size()));

        for(JLINKImage linkImage: image.getLinked_images()){

            JlinkLink link = new JlinkLink();
            JlinkRegion region = new JlinkRegion();
            
            if(linkImage.getLink_region_H() != null && !linkImage.getLink_region_H().isEmpty()) {
                region.setH(Double.parseDouble(linkImage.getLink_region_H()));
            } else {
                region.setH(0.0);
            }
            if(linkImage.getLink_region_W() != null && !linkImage.getLink_region_W().isEmpty()) {
                region.setW(Double.parseDouble(linkImage.getLink_region_W()));
            } else {
                region.setW(0.0);
            }
            if(linkImage.getLink_region_Y() != null && !linkImage.getLink_region_Y().isEmpty()) {
                region.setY(Double.parseDouble(linkImage.getLink_region_Y()));
            } else {
                region.setY(0.0);
            }
            if(linkImage.getLink_region_X() != null && !linkImage.getLink_region_X().isEmpty()) {
                region.setX(Double.parseDouble(linkImage.getLink_region_X()));
            } else {
                region.setX(0.0);
            }

            region.setShape(linkImage.getLink_region_shape());

            if(linkImage.getLink_region_rotation() != null && !linkImage.getLink_region_rotation().isEmpty()){
                region.setRotation(Double.parseDouble(linkImage.getLink_region_rotation()));
            } else {
                region.setRotation(0.0);
            }

            link.setDuration(Integer.parseInt(linkImage.getLink_duration()));
            link.setVpid(linkImage.getLink_Vpid());
            link.setSprite(image.getLink_sprite());
            link.setTo(linkImage.getLink_to());

            link.setRegion(region);
            element.addLink(link);
        }

        return element;        
    }

    public void addJUMBFContentBox(JLINKImage image, int id, String dir) throws Exception {
            
            String pathUrl = image.getAppPath() + File.separator + dir + File.separator
                + image.getTitle().replace(" ", "_") + ".jpeg";

            if(image.getReplacement() != null && image.getReplacement().equals("replace_img")) {
                pathUrl = image.getAppPath() + File.separator + dir + File.separator + image.getTitle().replace(" ", "_") + "_replacement.jpeg";
            }

            if ( image.getEncryption() != null && image.getReplacement() == null) {
                pathUrl = image.getAppPath() + File.separator + dir + File.separator + "protected_content.jpeg";

                InputStream inputStream = AppUtilsCommon.class.getClassLoader().getResourceAsStream(AppConst.PROTECTED_CONTENT_PATH);
                try (OutputStream outputStream = new FileOutputStream(pathUrl)) {
                    byte[] bytes = new byte[1024];
                    int read;

                    while ((read = inputStream.read(bytes)) != -1){
                        outputStream.write(bytes, 0, read);
                    }
                }
            }

            ContiguousCodestreamBox jp2cBox = new ContiguousCodestreamBox();
            jp2cBox.setFileUrl(pathUrl);

            JumbfBoxBuilder jp2cContentBoxBuilder = new JumbfBoxBuilder(new ContiguousCodestreamContentType());
            jp2cContentBoxBuilder.setJumbfBoxAsRequestable();
            jp2cContentBoxBuilder.setLabel(image.getLabel());
            jp2cContentBoxBuilder.setId(id);

            jp2cContentBoxBuilder.appendContentBox(jp2cBox);

            jlinkBoxBuilder.appendContentBox(jp2cContentBoxBuilder.getResult());

    }

    public void addJUMBFContentBoxForSprite(int cont_sprite, int id, String appPath, String sprite_path)
            throws Exception {

        ContiguousCodestreamBox jp2cBox = new ContiguousCodestreamBox();
        jp2cBox.setFileUrl(appPath + sprite_path);

        JumbfBoxBuilder jp2cContentBoxBuilder = new JumbfBoxBuilder(new ContiguousCodestreamContentType());
        jp2cContentBoxBuilder.setJumbfBoxAsRequestable();
        jp2cContentBoxBuilder.setLabel(JLINKConst.SPRITE_LABEL + cont_sprite);
        jp2cContentBoxBuilder.setId(id);

        jp2cContentBoxBuilder.appendContentBox(jp2cBox);

        jlinkBoxBuilder.appendContentBox(jp2cContentBoxBuilder.getResult());
    }

    public static StringBuilder getBlocks(String[] access, String action){
        StringBuilder block = new StringBuilder();

        for (String access_group : access){
            block.append(
                "<AllOf>\n" +
                "   <!-- Which role -->\n" +
                "   <Match MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">\n" +
                "   <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + access_group + "</AttributeValue>\n" +
                "   <AttributeDesignator \n" +
                "       Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\"\n" +
                "       AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:role\"\n" +
                "       DataType=\"http://www.w3.org/2001/XMLSchema#string\"\n" +
                "       MustBePresent=\"false\"/>\n" +
                "   </Match>\n" +
                "   <!-- Which action -->\n" +
                "   <Match MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">\n" +
                "   <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + action + "</AttributeValue>\n" +
                "   <AttributeDesignator \n" +
                "       Category=\"urn:oasis:names:tc:xacml:1.0:attribute-category:action\"\n" +
                "       AttributeId=\"urn:oasis:names:tc:xacml:1.0:action:action-id\"\n" +
                "       DataType=\"http://www.w3.org/2001/XMLSchema#string\"\n" +
                "       MustBePresent=\"false\"/>\n" +
                "   </Match>\n" +
                "</AllOf>\n"
            );
        }

        return block;

    }

    public String getXACMLrules(String[] view_access, String[] edit_access, String user) throws Exception{
        StringBuilder view_block, edit_block;
        
        InputStream inputStream = AppUtilsCommon.class.getClassLoader().getResourceAsStream("xacml/policy_template.xml");
        String template = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        view_block = getBlocks(view_access, "view");
        edit_block = getBlocks(view_access, "edit");

        if (!user.equals("Guest")){
            StringBuilder rule = new StringBuilder();
            rule.append(
                "<Rule Effect=\"Permit\" RuleId=\"urn:oasis:names:tc:xacml:3.0:permit-image-creator-user\">\n" +
                "    <Target>\n" +
                "    <AnyOf>\n" +
                "        <AllOf>\n" +
                "        <Match MatchId=\"urn:oasis:names:tc:xacml:1.0:function:string-equal\">" +
                "        <AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">" + user +"</AttributeValue>\n" +
                "        <AttributeDesignator\n" +
                "            Category=\"urn:oasis:names:tc:xacml:1.0:subject-category:access-subject\"\n" +
                "            AttributeId=\"urn:oasis:names:tc:xacml:1.0:subject:subject-id\"\n" +
                "            DataType=\"http://www.w3.org/2001/XMLSchema#string\"\n" +
                "            MustBePresent=\"false\"/>\n" +
                "        </Match>\n" +
                "        </AllOf>\n" +
                "    </AnyOf>\n" +
                "    </Target>\n" +
                "</Rule>\n");
            
            template = template.replace("{{USERNAME}}", rule);
        }else{
            template = template.replace("{{USERNAME}}", "");
        }


        template = template.replace("{{VIEW_BLOCK}}", view_block).replace("{{EDIT_BLOCK}}", edit_block);
        return template;
    }

    public JumbfBox getReplacementBox(JLINKImage image, String dir) throws Exception{
        String replacement, saveDir;

        replacement = image.getReplacement();

        if ( replacement != null){

            JumbfBoxBuilder replacementBoxBuilder = new JumbfBoxBuilder(new ReplacementContentType());
            ReplacementDescriptionBox replacementDescriptionBox = new ReplacementDescriptionBox();

            replacementBoxBuilder.setLabel(image.getLabel() + "_replacement");
            saveDir = image.getAppPath() + File.separator + dir + File.separator + image.getTitle() + "_replacement.jpeg";
            
            if ( replacement.equals("roi")){
                RoiParamHandler roiParamHandler = new RoiParamHandler();
                roiParamHandler.setOffsetX(image.getROI_region_X());
                roiParamHandler.setOffsetY(image.getROI_region_Y());

                replacementDescriptionBox.setAutoApply(true);
                replacementDescriptionBox.setReplacementTypeId(ReplacementType.ROI.getId());
                replacementDescriptionBox.setParamHandler(roiParamHandler);

            }else if (replacement.equals("replace_img")) {
                FileParamHandler fileParamHandler = new FileParamHandler();
                replacementDescriptionBox.setParamHandler(fileParamHandler);
                replacementDescriptionBox.setReplacementTypeId(ReplacementType.FILE.getId());

                saveDir = image.getAppPath() + File.separator + dir + File.separator + image.getTitle() + ".jpeg";
            }

            ContiguousCodestreamBox jp2cBox = new ContiguousCodestreamBox();
            
            jp2cBox.setFileUrl(saveDir);

            replacementBoxBuilder.appendContentBox(replacementDescriptionBox);
            replacementBoxBuilder.appendContentBox(jp2cBox);

            return replacementBoxBuilder.getResult();
        }

        return null;
    }

    public Boolean addProtectionBox(JLINKImage image, JumbfBox replacementBox, String user, String dir) throws Exception{
        String encryption ,saveDir;
        String[] view_access, edit_access;
        
        encryption = image.getEncryption();
        view_access = image.getViewAccess();
        edit_access = image.getEditAccess();

        if(encryption != null) {

            JumbfBoxBuilder protectionBoxBuilder = new JumbfBoxBuilder(new ProtectionContentType());
            ProtectionDescriptionBox protectionDescriptionBox = new ProtectionDescriptionBox();
            SecretKeySpec secretKey = image.getSecretKey();
            IvParameterSpec ivSpec = image.getIVSpec();
            System.out.println(ivSpec);

            if (encryption.equals("AES256")){
                protectionDescriptionBox.setAes256CbcProtection();
            }else if (encryption.equals("AES256IV")){
                protectionDescriptionBox.setAes256CbcWithIvProtection();
                protectionDescriptionBox.setIv(ivSpec.getIV());
            }

            if (view_access != null) {
                protectionDescriptionBox.setArLabel("access-rules-reference");
                protectionDescriptionBox.includeAccessRulesInToggle();

                String rules;
                JumbfBoxBuilder xmlBoxBuilder = new JumbfBoxBuilder(new XmlContentType());
                xmlBoxBuilder.setLabel("access-rules-reference");
                XmlBox xmlBox = new XmlBox();

                if(dir.equals(AppConst.MODIFIER_DIR) && !image.getPossibleAction()) {
                    rules  = Files.readString(Path.of(image.getAppPath() + File.separator + dir + File.separator + "xacml_policy.txt"));
                } else {
                    rules = getXACMLrules(view_access, edit_access, user);
                }
                xmlBox.setContent(rules.getBytes());
                xmlBoxBuilder.appendContentBox(xmlBox);
                xmlBoxBuilder.getResult();
                jlinkBoxBuilder.appendContentBox(xmlBoxBuilder.getResult());

                
            }
            
            BinaryDataBox binaryDataBox = new BinaryDataBox();
            if(replacementBox == null){
                
                protectionBoxBuilder.setLabel(image.getLabel());
                System.out.println("The image is: " + image.getTitle() + " in" + dir);
                saveDir = image.getAppPath() + File.separator + dir + File.separator + image.getTitle().replace(" ", "_") + ".jpeg";
                BufferedImage original_image = ImageIO.read(new File(saveDir));

                saveDir = image.getAppPath() + File.separator + dir + File.separator + image.getTitle().replace(" ", "_") + ".imgenc";
                AppUtilsCreator.encryptBuffImage(encryption, secretKey, ivSpec, original_image, saveDir);

            }else{

                protectionBoxBuilder.setLabel("replacementBox");

                String targetUrl = image.getAppPath() + File.separator + dir + File.separator + image.getTitle() + "replacementBox.jumbf";

                ApplicationContext context = new AnnotationConfigApplicationContext(JumbfConfig.class);
                CoreGeneratorService generatorService = context.getBean(CoreGeneratorService.class);

                generatorService.generateJumbfMetadataToFile(List.of(replacementBox), targetUrl);

                ((ConfigurableApplicationContext) context).close();

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

                saveDir = image.getAppPath() + File.separator + dir + File.separator +  image.getTitle() + "replacementBox.imgenc";
                File file = new File(targetUrl);

                try (OutputStream outStream = new FileOutputStream(new File(saveDir));
                     CipherInputStream cinput = new CipherInputStream(new FileInputStream(file), cipher);) {
                byte[] bytes = new byte[1024];
                int read;
                while ((read = cinput.read(bytes)) != -1) {
                    outStream.write(bytes, 0, read);
                }
                }
            }

            binaryDataBox.setFileUrl(saveDir);

            protectionBoxBuilder.appendContentBox(protectionDescriptionBox);
            protectionBoxBuilder.appendContentBox(binaryDataBox);

            jlinkBoxBuilder.appendContentBox(protectionBoxBuilder.getResult());

            return false;
            
        }

        return true; // replacementBox needs to be added
        
    }

    public void addPrivacy(JLINKImage image, int id, String user, String dir) throws Exception{

        if ( image.getEncryption() != null || image.getReplacement() != null){
            JumbfBox replacementBox = this.getReplacementBox(image, dir);
            Boolean add = this.addProtectionBox(image, replacementBox, user, dir);
            if (add && replacementBox != null){
                jlinkBoxBuilder.appendContentBox(replacementBox);
            }
        }
    }

    public BuilderWeb getParent() {
        return this.parent;
    }

    public JumbfBoxBuilder getJlinkBoxBuilder() {
        return jlinkBoxBuilder;
    }
}
