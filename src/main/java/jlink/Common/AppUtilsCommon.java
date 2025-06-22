package jlink.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import org.apache.commons.lang3.StringUtils;
import org.mipams.jlink.entities.JlinkElement;
import org.mipams.jlink.entities.JlinkLink;
import org.mipams.jlink.entities.JlinkViewport;
import org.mipams.jlink.services.JlinkXmlValidator;
import org.mipams.jumbf.entities.BinaryDataBox;
import org.mipams.jumbf.entities.ContiguousCodestreamBox;
import org.mipams.jumbf.entities.JumbfBox;
import org.mipams.jumbf.entities.XmlBox;
import org.mipams.jumbf.services.JpegCodestreamParser;
import org.mipams.jumbf.util.CoreUtils;
import org.mipams.jumbf.util.MipamsException;
import org.mipams.privsec.entities.ProtectionDescriptionBox;
import org.mipams.privsec.entities.ReplacementDescriptionBox;
import org.mipams.privsec.entities.replacement.ReplacementType;
import org.mipams.privsec.entities.replacement.RoiParamHandler;
import org.mipams.privsec.services.content_types.ReplacementContentType;
import org.mipams.jumbf.services.CoreParserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import jlink.Config.JumbfConfig;
import jlink.Const.AppConst;
import jlink.Const.JLINKConst;
import jlink.Const.MetaConst;
import jlink.JLINKImage.JLINKImage;

import jlink.XacmlPDP.PDP;
import java.security.SecureRandom;

import java.nio.file.Path;

public class AppUtilsCommon {
    public SecretKeySpec secretKey;
    public String user, role;

    public AppUtilsCommon(SecretKeySpec secretKey, String user, String role){
        this.secretKey = secretKey;
        this.user = user;
        this.role = role;
    }

    public static PreparedStatement prepare_query(String sql, String[] parameters)
        throws SQLException, ClassNotFoundException{

        String dbName = System.getenv("POSTGRES_DB");
        String user = System.getenv("POSTGRES_USER");
        String password = System.getenv("POSTGRES_PASSWORD");
        
        String db_url = String.format(AppConst.DATABASE_CONNECTION, dbName, user, password);
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(db_url);
        PreparedStatement statement = connection.prepareStatement(sql);

        for(int i = 0; i< parameters.length ; i++){
            if (parameters[i].equals("true") || parameters[i].equals("false")){
                statement.setBoolean(i+1, Boolean.parseBoolean(parameters[i]));
            }else {
                statement.setString(i+1, parameters[i]);
            }
        }
        return statement;
    }

    public static String getFileHash(String path) throws Exception{
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

        try (InputStream inputStream = new FileInputStream(path)){
            byte[] bytes = new byte[8192];
            int read;

            while((read = inputStream.read(bytes)) != -1) {
                messageDigest.update(bytes, 0, read);
            }
        }

        byte[] hashBytes = messageDigest.digest();
        StringBuilder hash = new StringBuilder();
        for( byte b : hashBytes){
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }

    public static SecretKeySpec getMasterSpecs() throws Exception{

        String sk = System.getenv("SECRETKEY");
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] key = digest.digest(sk.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        return secretKey;
    }

    // public static void getMetadata(byte[] data, JLINKImage image) {

    //     String metadata, value, start_template;

    //     metadata = new String(data, StandardCharsets.UTF_8);
    //     start_template = MetaConst.METADATA_START;

    //     image.setVersion(StringUtils.substringsBetween(metadata, String.format(start_template, MetaConst.VERSION_METADATA), MetaConst.METADATA_END)[0]);

    //     image.setTitle(StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.TITLE_METADATA), MetaConst.METADATA_END)[0]);

    //     image.setNote(StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.NOTE_METADATA), MetaConst.METADATA_END)[0]);

    //     image.setImage_format(StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.FORMAT_METADATA), MetaConst.METADATA_END)[0]);

    //     value = StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.HREF_METADATA), MetaConst.METADATA_END)[0];
    //     value = JLINKConst.IMAGE_LABEL + value.replaceAll("[^0-9]", "");
    //     image.setImage_Href(value);

    //     value = StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.LINK_X_METADATA), MetaConst.METADATA_END)[0];
    //     image.setLink_region_X(value);

    //     image.setLink_region_Y(StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.LINK_Y_METADATA), MetaConst.METADATA_END)[0]);
        
    //     String[] duration = StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.DURATION_METADATA), MetaConst.METADATA_END);
    //     if (duration != null && duration.length > 0) {
    //         image.setLink_duration(duration[0]);
    //     }

    //     String[] vpid = StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.VPID_METADATA), MetaConst.METADATA_END);
    //     if(vpid != null && vpid.length > 0) {
    //         image.setLink_Vpid(Integer.parseInt(vpid[0]));
    //     }

    //     String[] values = StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.SPRITE_METADATA), MetaConst.METADATA_END);
    //     if (values != null && values.length > 0) {
    //         image.setLink_sprite(JLINKConst.SPRITE_LABEL + values[0].replaceAll("[^0-9]", ""));
    //     }
        
    //     values = StringUtils.substringsBetween(metadata,  String.format(start_template, MetaConst.TO_METADATA), MetaConst.METADATA_END);
    //     if (values != null && values.length > 0) {
    //         image.setLink_to(JLINKConst.SCENE_LABEL + value.replaceAll("[^0-9]", ""));
    //     }
    // }

        public void getImageFromXmlBox(JumbfBox xmlJumbfBox, JLINKImage img) throws Exception{
        ApplicationContext context = null;
        JlinkElement element = null;
        try{
            context = new AnnotationConfigApplicationContext(JumbfConfig.class);
            JlinkXmlValidator jlinkXmlValidator = context.getBean(JlinkXmlValidator.class);

            element = jlinkXmlValidator.validateSchema(xmlJumbfBox);
            ((ConfigurableApplicationContext) context).close();

        } catch(MipamsException e){
            e.printStackTrace();
            ((ConfigurableApplicationContext) context).close();
            throw new Exception(e);
        }

        img.setVersion(element.getScene().getVersion());
        img.setTitle(element.getScene().getTitle());
        img.setNote(element.getScene().getNote());
        img.setLabel(xmlJumbfBox.getDescriptionBox().getLabel());

        img.setImage_format(element.getScene().getImage().getFormat());
        img.setImage_Href(element.getScene().getImage().getHref());


        JlinkViewport viewport = null;
        if(element.getScene().getViewports().isEmpty()){
            viewport = new JlinkViewport();
        } else {
            viewport = element.getScene().getViewports().get(0);
        }

        if(viewport.getId() != null){
            img.setViewport_Id(viewport.getId());
        }

        img.setViewport_X(Double.toString(viewport.getX()));
        img.setViewport_Y(Double.toString(viewport.getY()));
        img.setViewport_xFov(Double.toString(viewport.getXfov()));
        img.setViewport_yFov(Double.toString(viewport.getYfov()));

        for(JlinkLink link: element.getLinks()) {
            JLINKImage linkImg = new JLINKImage();

            if(link.getRegion() != null) {
            
                if(link.getRegion().getX() != null) { 
                    linkImg.setLink_region_X(link.getRegion().getX().toString());
                }
                if(link.getRegion().getY() != null) { 
                    linkImg.setLink_region_Y(link.getRegion().getY().toString());
                }
                if(link.getRegion().getH() != null) { 
                    linkImg.setLink_region_H(link.getRegion().getH().toString());
                }
                if(link.getRegion().getW() != null) { 
                    linkImg.setLink_region_W(link.getRegion().getW().toString());
                }
                if(link.getRegion().getRotation() != null) { 
                    linkImg.setLink_region_rotation(link.getRegion().getRotation().toString());
                }
                linkImg.setLink_region_shape(link.getRegion().getShape());
            }

            linkImg.setLink_duration(Integer.toString(link.getDuration()));
            linkImg.setLink_Vpid(link.getVpid());
            linkImg.setLink_to(link.getTo());
            linkImg.setLink_sprite(link.getSprite());

            img.addLink(linkImg);
        }
    }

    public JLINKImage locateLinkImage(JLINKImage img, JumbfBox jumbfBox) throws Exception{

        for(JLINKImage aux: img.getLinked_images()) {

            int lastSlashIndex = aux.getLink_to().lastIndexOf("=");
            String descriptionBoxLabel = aux.getLink_to().substring(lastSlashIndex + 1);

            System.out.println(String.format("Comparing [%s] with param [%s]", descriptionBoxLabel, jumbfBox.getDescriptionBox().getLabel()));
        
            if(descriptionBoxLabel.equals(jumbfBox.getDescriptionBox().getLabel())){
                return aux;
            }
        }
        throw new Exception(String.format("A scene was found [%s] with no link", jumbfBox.getDescriptionBox().getLabel()));
    }

    public static void writeJp2cContentToUrl(ContiguousCodestreamBox jp2c, String targetFileUrl)
            throws MipamsException {
        try (FileOutputStream fos = new FileOutputStream(targetFileUrl)) {
            CoreUtils.writeFileContentToOutput(jp2c.getFileUrl(), fos);
        } catch (IOException e) {
            throw new MipamsException(e);
        }
    }

    public static void writeBinaryContentToUrl(BinaryDataBox binaryDataBox, String targetFileUrl)
            throws MipamsException {
        try (FileOutputStream fos = new FileOutputStream(targetFileUrl)) {
            CoreUtils.writeFileContentToOutput(binaryDataBox.getFileUrl(), fos);
        } catch (IOException e) {
            throw new MipamsException(e);
        }
    }

    public void setSecretKey(String hash) throws Exception{
        try {
            String sql = "SELECT * FROM keys WHERE id= ?";
            
            String dbName = System.getenv("KEY_DB");
            String user = System.getenv("KEY_USER");
            String password = System.getenv("KEY_PASSWORD");
            
            String db_url = String.format(AppConst.KEYDB_CONNECTION, dbName, user, password);
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(db_url);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, hash);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                byte[] secretKey = rs.getBytes("secret_key");
                String ivB64 = rs.getString("iv");
                byte[] iv = Base64.getDecoder().decode(ivB64);
                SecretKeySpec masterSK = getMasterSpecs();

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, masterSK, new IvParameterSpec(iv));
                byte[] secret_key = cipher.doFinal(secretKey);
                
                this.secretKey = new SecretKeySpec(secret_key, "AES");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        
    }

    public static void saveKey(String path, JLINKImage image) throws Exception {
        String hash = AppUtilsCommon.getFileHash(path);

        String sql = "INSERT INTO KEYS (id, secret_key, iv) VALUES (?, ?, ?)";

        SecretKeySpec masterSk = AppUtilsCommon.getMasterSpecs();
        SecretKeySpec key = image.getSecretKey();
        byte[] iv = new byte[16];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, masterSk,  ivSpec);
        byte[] data = cipher.doFinal(key.getEncoded());

        String dbName = System.getenv("KEY_DB");
        String user = System.getenv("KEY_USER");
        String password = System.getenv("KEY_PASSWORD");
        
        String db_url = String.format(AppConst.KEYDB_CONNECTION, dbName, user, password);
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(db_url);
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, hash);
        statement.setBytes(2, data);
        statement.setString(3, Base64.getEncoder().encodeToString(iv));
        statement.executeUpdate();
    }

    
    public static void setImagesSpecs(JLINKImage image, SecretKeySpec secretKey, IvParameterSpec iv) {
        for (JLINKImage aux : image.getLinked_images()) {
                if (aux.getEncryption() != null) {
                    aux.setSecretKey(secretKey);
                    aux.setIVSpec(iv);
                }
                setImagesSpecs(aux, secretKey, iv);
            }
    }

    public void checkProtection(JLINKImage image, ProtectionDescriptionBox protectionDescriptionBox, BinaryDataBox binaryDataBox, ReplacementDescriptionBox replacementDescriptionBox, XmlBox xmlBox, String constDir, String action) throws Exception{
        Boolean access = false;
        String saveDir;

        String label = image.getLabel();
        image.setDecryptionPossible(true);

        if (protectionDescriptionBox != null && protectionDescriptionBox.getArLabel() !=  null && protectionDescriptionBox.getArLabel().equals("access-rules-reference") && xmlBox != null ) {
            image.setViewAccess(new String[]{"True"});
            
            String policy = new String(xmlBox.getContent(), StandardCharsets.UTF_8);
            InputStream inputStream = AppUtilsCommon.class.getClassLoader().getResourceAsStream("xacml/request_template.xml");
            String request = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            request = request.replace("{{USERNAME}}", this.user).replace("{{ROLE}}", this.role).replace("{{ACTION}}", action);
            PDP pdp = new PDP();
            String effect = pdp.decide(policy, request);
            if(effect.equals("Permit")){
                access = true;
            }
            
            if (!access && constDir.equals(AppConst.MODIFIER_DIR)) {
                Files.writeString(Path.of(image.getAppPath() + File.separator + constDir + File.separator + "xacml_policy.txt"), policy);
            }
        } else {
            access = true;
        }

        image.setPossibleAction(access);

        if ( xmlBox == null || access || constDir.equals(AppConst.MODIFIER_DIR)) {
            if (protectionDescriptionBox != null && this.secretKey != null){
                //decrypt binaryDataBox
                String url = saveEncriptedContent(binaryDataBox, label.toLowerCase()+ "_encripted_content", image.getAppPath(), constDir);
                byte[] iv = protectionDescriptionBox.getIv();

                if (protectionDescriptionBox.isAes256CbcProtection()) {
                    image.setEncryption("AES256");
                } else if (protectionDescriptionBox.isAes256CbcWithIvProtection()) {
                    image.setEncryption("AES256IV");
                }
                
                if (image.getReplacement() != null && image.getReplacement().equals("True")) {
                    // get ReplcaementDescriptionBox and ContiguousCodestreamBox
                    saveDir = image.getAppPath() + File.separator + constDir + File.separator + label.toLowerCase() + "_replacementBox" + ".jumbf";
                    decryptContent(url, saveDir, iv);

                    ApplicationContext context = new AnnotationConfigApplicationContext(JumbfConfig.class);
                    CoreParserService coreParserService = context.getBean(CoreParserService.class);

                    List<JumbfBox> jumbfBoxList = coreParserService.parseMetadataFromFile(saveDir);

                    ((ConfigurableApplicationContext) context).close();

                    JumbfBox replacementBox = null;
                    ReplacementContentType contentType = new ReplacementContentType();
                    for (JumbfBox jumbfBox : jumbfBoxList) {
                        if (contentType.getContentTypeUuid().equals(jumbfBox.getDescriptionBox().getUuid())) {
                            replacementBox = jumbfBox;
                            break;
                        }
                    }

                    if (replacementBox != null){
                        replacementDescriptionBox = (ReplacementDescriptionBox) replacementBox.getContentBoxList().get(0);
                        ContiguousCodestreamBox jp2cBox = (ContiguousCodestreamBox) replacementBox.getContentBoxList().get(1);
                        saveContent(jp2cBox, label.toLowerCase() + "_replacement", image.getAppPath(), constDir);
                    }

                } else {
                    saveDir = image.getAppPath() + File.separator + constDir + File.separator + label.toLowerCase()+ ".jpeg";
                    Path path = Paths.get(saveDir);
                    byte[] data = Files.readAllBytes(path);

                    // Mostrar los primeros bytes en hexadecimal
                    for (int i = 0; i < Math.min(32, data.length); i++) {
                        System.out.printf("%02X ", data[i]);
                    }
                    decryptContent(url, saveDir, iv);
                }
            } else if (protectionDescriptionBox != null && this.secretKey == null){
                    image.setDecryptionPossible(false);
            }

            if (replacementDescriptionBox != null){

                int type = replacementDescriptionBox.getReplacementTypeId();
                String targetUrl = image.getAppPath() + File.separator + constDir + File.separator + label.toLowerCase()+ ".jpeg";
                String replacementUrl = image.getAppPath() + File.separator + constDir + File.separator + label.toLowerCase() + "_replacement.jpeg";

                if (ReplacementType.ROI.getId() == type) {
                    image.setReplacement("roi");

                    RoiParamHandler roiParamHandler = (RoiParamHandler) replacementDescriptionBox.getParamHandler();
                    int offsetX = roiParamHandler.getOffsetX();
                    int offsetY = roiParamHandler.getOffsetY();

                    image.setROI_region_X(offsetX);
                    image.setROI_region_Y(offsetY);

                    BufferedImage img = ImageIO.read(new File(targetUrl));
                    BufferedImage roiImage = ImageIO.read(new File(replacementUrl));

                    int width = roiImage.getWidth();
                    int height = roiImage.getHeight();

                    // enganchar R en I' para obtener I y guardar I
                    Graphics2D graph = img.createGraphics();
                    graph.drawImage(roiImage, offsetX, offsetY, width, height, null);
                    graph.dispose();
                    ImageIO.write(img, "jpeg", new File(targetUrl));
                } else if (ReplacementType.FILE.getId() == type) {
                    image.setReplacement("replace_img");
                    BufferedImage replacementImage = ImageIO.read(new File(replacementUrl));
                    ImageIO.write(replacementImage, "jpeg", new File(targetUrl));
                }
            }
        } else {
            String url = image.getAppPath() + File.separator + constDir + File.separator + label.toLowerCase() + ".jpeg";
            File file = new File(url);

            if (!file.exists() || file.length() == 0){
                InputStream inputStream = AppUtilsCommon.class.getClassLoader().getResourceAsStream(AppConst.PROTECTED_CONTENT_PATH);
                BufferedImage replacementImage = ImageIO.read(inputStream);
                ImageIO.write(replacementImage, "jpeg", new File(url));
            }
        }
    }

    public void saveContent(ContiguousCodestreamBox jp2c, String label, String appPath, String constDir)
            throws MipamsException {

        String targetUrl = appPath + File.separator + constDir + File.separator + label.toLowerCase()
                + ".jpeg";
        AppUtilsCommon.writeJp2cContentToUrl(jp2c, targetUrl);
    }

    private static String saveEncriptedContent(BinaryDataBox binaryDataBox, String label, String appPath, String constDir)
            throws MipamsException {

        String targetUrl = appPath + File.separator + constDir + File.separator + label.toLowerCase()
                + ".enc";
        AppUtilsCommon.writeBinaryContentToUrl(binaryDataBox, targetUrl);

        return targetUrl;
    }

    private void decryptContent(String url, String targetUrl, byte[] iv) throws Exception{

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        if (iv == null) {
            iv = new byte[16];
        }
        cipher.init(Cipher.DECRYPT_MODE, this.secretKey, new IvParameterSpec(iv));

        try (FileInputStream fileinputStream = new FileInputStream(url);
                FileOutputStream fileOutputStream = new FileOutputStream(targetUrl)) {
            
            byte[] bytes = new byte[4096];
            int read;
            while((read = fileinputStream.read(bytes)) != -1){
                byte[] outputBytes = cipher.update(bytes, 0, read);
                if (outputBytes != null) {
                    fileOutputStream.write(outputBytes);
                }
            }

            byte[] decryptedBytes = cipher.doFinal();
            if(decryptedBytes != null){
                fileOutputStream.write(decryptedBytes);
            }
        }
    }
}
