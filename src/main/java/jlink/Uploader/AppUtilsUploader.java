/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jlink.Uploader;

import jlink.Common.AppUtilsCommon;
import jlink.Config.JumbfConfig;
import jlink.Const.AppConst;
import jlink.Const.JLINKConst;
import jlink.JLINKImage.JLINKImage;
import java.io.File;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.mipams.jumbf.entities.BinaryDataBox;
import org.mipams.jumbf.entities.BmffBox;
import org.mipams.jumbf.entities.ContiguousCodestreamBox;
import org.mipams.jumbf.entities.JumbfBox;
import org.mipams.jumbf.entities.XmlBox;
import org.mipams.jumbf.services.JpegCodestreamParser;
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

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
public class AppUtilsUploader {

        SecretKeySpec secretKey;
        
        public void setJLINKImage(JLINKImage image, String file_name, String user, String role) throws Exception {

                AppUtilsCommon utils = new AppUtilsCommon(null, user, role);
                String fileUrl = image.getAppPath() + File.separator + AppConst.UPLOADER_DIR + File.separator
                                + file_name;

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
                                aux.setPrevious_image(img.getTitle().replace(" ", "_"));
                        }

                        if (jp2c.getContentTypeUuid().equals(uuid)) {
                                label = contentJumbfBox.getDescriptionBox().getLabel();

                                ContiguousCodestreamBox jp2cBox = (ContiguousCodestreamBox) contentJumbfBox
                                                .getContentBoxList().get(0);

                                if (label != null) {
                                        utils.saveContent(jp2cBox, label, img.getAppPath(), AppConst.UPLOADER_DIR);
                                                
                                        if(label.contains("image")) {
                                                img.setLabel(label);
                                        }
                                }
                        }

                        label = contentJumbfBox.getDescriptionBox().getLabel();

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
                                utils.saveContent(jp2cBoxR, label.replace(" ", "_"), img.getAppPath(), AppConst.UPLOADER_DIR);
                                }
                        }

                        if (xmlContentType.getContentTypeUuid().equals(uuid)) {
                                if (label.equals("access-rules-reference")){
                                xmlBoxProtect = (XmlBox) contentJumbfBox.getContentBoxList().get(0);
                                }
                        }

                       
                }

                utils.checkProtection(img, protectionDescriptionBox, binaryDataBox, replacementDescriptionBox, xmlBoxProtect, AppConst.UPLOADER_DIR, "view");

                return img;
        }
}

        // private void checkProtection(JLINKImage image, ProtectionDescriptionBox protectionDescriptionBox, BinaryDataBox binaryDataBox, ReplacementDescriptionBox replacementDescriptionBox, XmlBox xmlBox) throws Exception{
        //         Boolean access = true;
        //         String saveDir;

        //         String label = image.getLabel();

        //         if ( xmlBox == null || access) {
        //         if (protectionDescriptionBox != null && this.secretKey != null){
        //                 //decrypt binaryDataBox
        //                 String url = saveEncriptedContent(binaryDataBox, label.toLowerCase() + "encripted_content", image.getAppPath());
        //                 byte[] iv = protectionDescriptionBox.getIv();
                        
        //                 if (image.getProtection() == "True" ) {
        //                 // get ReplcaementDescriptionBox and ContiguousCodestreamBox
        //                 saveDir = image.getAppPath() + File.separator + AppConst.UPLOADER_DIR + File.separator + label.toLowerCase() + "replacementBox" + ".jumbf";
        //                 decryptContent(url, saveDir, iv);

        //                 ApplicationContext context = new AnnotationConfigApplicationContext(JumbfConfig.class);
        //                 JpegCodestreamParser jpegCodestreamParser = context.getBean(JpegCodestreamParser.class);

        //                 List<JumbfBox> jumbfBoxList = jpegCodestreamParser.parseMetadataFromFile(saveDir);

        //                 ((ConfigurableApplicationContext) context).close();

        //                 JumbfBox replacementBox = null;
        //                 ReplacementContentType contentType = new ReplacementContentType();
        //                 for (JumbfBox jumbfBox : jumbfBoxList) {
        //                         if (contentType.getContentTypeUuid().equals(jumbfBox.getDescriptionBox().getUuid())) {
        //                         replacementBox = jumbfBox;
        //                         break;
        //                         }
        //                 }

        //                 if (replacementBox != null){
        //                         replacementDescriptionBox = (ReplacementDescriptionBox) replacementBox.getContentBoxList().get(0);
        //                         ContiguousCodestreamBox jp2cBox = (ContiguousCodestreamBox) replacementBox.getContentBoxList().get(1);
        //                         this.saveContent(jp2cBox, label.toLowerCase() + "_Replacement", image.getAppPath());
        //                 }

        //                 } else {
        //                 saveDir = image.getAppPath() + File.separator + AppConst.UPLOADER_DIR + File.separator + label.toLowerCase() + ".jpeg";
        //                 decryptContent(url, saveDir, iv);
        //                 }
        //         }

        //         if (replacementDescriptionBox != null){

        //                 int type = replacementDescriptionBox.getReplacementTypeId();
        //                 String targetUrl = image.getAppPath() + File.separator + AppConst.UPLOADER_DIR + File.separator + label.toLowerCase() + ".jpeg";
        //                 String replacementUrl = image.getAppPath() + File.separator + AppConst.UPLOADER_DIR + File.separator + label.toLowerCase() + "_Replacement.jpeg";

        //                 if (ReplacementType.ROI.getId() == type) {
        //                 RoiParamHandler roiParamHandler = (RoiParamHandler) replacementDescriptionBox.getParamHandler();
        //                 int offsetX = roiParamHandler.getOffsetX();
        //                 int offsetY = roiParamHandler.getOffsetY();

        //                 BufferedImage img = ImageIO.read(new File(targetUrl));
        //                 BufferedImage roiImage = ImageIO.read(new File(replacementUrl));

        //                 int width = roiImage.getWidth();
        //                 int height = roiImage.getHeight();

        //                 // enganchar R en I' para obtener I y guardar I
        //                 Graphics2D graph = img.createGraphics();
        //                 graph.drawImage(roiImage, offsetX, offsetY, width, height, null);
        //                 graph.dispose();
        //                 ImageIO.write(img, "jpeg", new File(targetUrl));
        //                 } else if (ReplacementType.FILE.getId() == type) {
        //                 BufferedImage replacementImage = ImageIO.read(new File(replacementUrl));
        //                 ImageIO.write(replacementImage, "jpeg", new File(targetUrl));
        //                 }
        //         }
        //         } else {
        //         String url = image.getAppPath() + File.separator + AppConst.UPLOADER_DIR + File.separator + label.toLowerCase() + ".jpeg";
        //         File file = new File(url);

        //         if (!file.exists() || file.length() == 0){
        //                 InputStream inputStream = AppUtilsCommon.class.getClassLoader().getResourceAsStream(AppConst.PROTECTED_CONTENT_PATH);
        //                 BufferedImage replacementImage = ImageIO.read(inputStream);     
        //                 ImageIO.write(replacementImage, "jpeg", new File(url));
        //         }
        //         }
        // }



//         private void saveContent(ContiguousCodestreamBox jp2c, String title, String appPath) throws MipamsException {
//                 String targetUrl = appPath + File.separator + AppConst.UPLOADER_DIR + File.separator + title
//                                 + ".jpeg";
//                 AppUtilsCommon.writeJp2cContentToUrl(jp2c, targetUrl);
//         }


//         private String saveEncriptedContent(BinaryDataBox binaryDataBox, String label, String appPath)
//             throws MipamsException {

//                 String targetUrl = appPath + File.separator + AppConst.UPLOADER_DIR + File.separator + label.toLowerCase()
//                         + ".enc";
//                 AppUtilsCommon.writeBinaryContentToUrl(binaryDataBox, targetUrl);

//                 return targetUrl;
//         }

//         private void decryptContent(String url, String targetUrl, byte[] iv) throws Exception{

//                 Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//                 cipher.init(Cipher.ENCRYPT_MODE, this.secretKey, new IvParameterSpec(iv));

//                 try (FileInputStream fileinputStream = new FileInputStream(url);
//                         FileOutputStream fileOutputStream = new FileOutputStream(targetUrl)) {
                
//                 byte[] bytes = new byte[4096];
//                 int read;
//                 while((read = fileinputStream.read(bytes)) != -1){
//                         byte[] outputBytes = cipher.update(bytes, 0, read);
//                         if (outputBytes != null) {
//                         fileOutputStream.write(outputBytes);
//                         }
//                 }

//                 byte[] decryptedBytes = cipher.doFinal();
//                 if(decryptedBytes != null){
//                         fileOutputStream.write(decryptedBytes);
//                 }
//                 }
//         }
// }