/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jlink.Viewer;

import jlink.Common.AppUtilsCommon;
import jlink.Config.JumbfConfig;
import jlink.Const.AppConst;
import jlink.Const.JLINKConst;
import jlink.JLINKImage.JLINKImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

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
import org.mipams.jlink.entities.JlinkElement;
import org.mipams.jlink.entities.JlinkLink;
import org.mipams.jlink.entities.JlinkViewport;
import org.mipams.jlink.services.JlinkContentType;
import org.mipams.jlink.services.JlinkXmlValidator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * @author Victor Ojeda and Pablo Pascual
 */
public class AppUtilsViewer {

    public Boolean decryptionPossible = true;

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
        XmlBox xmlBox = null;
        ContiguousCodestreamBox jp2cBoxR = null;

        JumbfBox xmlContentJumbfBox = (JumbfBox) jlinkBox.getContentBoxList().get(0);
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
                aux.setPrevious_image(img.getImage_Href());
            }

            label = contentJumbfBox.getDescriptionBox().getLabel();

            if (jp2c.getContentTypeUuid().equals(uuid)) {

                ContiguousCodestreamBox jp2cBox = (ContiguousCodestreamBox) contentJumbfBox
                        .getContentBoxList().get(0);

                if (label != null) {
                    utils.saveContent(jp2cBox, label, img.getAppPath(), AppConst.VIEW_DIR);

                    if(label.contains("image")) {
                        img.setLabel(label);
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
                    utils.saveContent(jp2cBoxR, label, img.getAppPath(), AppConst.VIEW_DIR);
                }
            }

            if (xmlContentType.getContentTypeUuid().equals(uuid)) {
                if (label.equals("access-rules-reference")){
                    xmlBox = (XmlBox) contentJumbfBox.getContentBoxList().get(0);
                }
            }
        }

        utils.checkProtection(img, protectionDescriptionBox, binaryDataBox, replacementDescriptionBox, xmlBox, AppConst.VIEW_DIR, "view");

        return img;
    }

    public void viewerScript(JLINKImage image) throws FileNotFoundException, IOException {

        StringBuilder sb;
        OutputStream out;
        File f;

        sb = new StringBuilder();
        this.initViewer(image, sb);
        this.resizeViewer(image, sb);
        this.jumpIn(image, sb);
        this.jumpOut(image, sb);
        this.spriteMouseOverAndOut(image, sb);
        this.backButtonMouseOverAndOut(image, sb);
        this.imageTextInformation(sb);
        f = new File(image.getAppPath() + File.separator + AppConst.JS_VIEWER_PATH);
        out = new FileOutputStream(f);
        out.write(sb.toString().getBytes());
    }

    public void initViewer(JLINKImage image, StringBuilder sb) {

        sb.append("function initViewer() {\n");
        sb.append("var view = document.getElementById('view');\n");
        this.initImg(image, sb);
        sb.append("if (img0.height*100/document.documentElement.clientHeight > 75) {\n");
        sb.append("img0.style.height = 75 + 'vh';\n");
        sb.append("img0.style.width = 'auto';\n");
        sb.append("}\n");
        sb.append("if (img0.width*100/document.documentElement.clientWidth > 45) {\n");
        sb.append("img0.style.width = 45 + 'vw';\n");
        sb.append("img0.style.height = 'auto';\n");
        sb.append("}\n");
        sb.append("view.style.height = img0.height + 'px';\n");
        sb.append("view.style.width = img0.width + 'px';\n");
        sb.append("img0.style.top = 0 + 'px';\n");
        sb.append("img0.style.left = 0 + 'px';\n");
        sb.append(
                "document.getElementById('scene-title').innerHTML = '" + image.getTitle().replace("'", "\\'") + "';\n");
        sb.append("document.getElementById('scene-description').innerHTML = '" + image.getNote().replace("'", "\\'")
                + "';\n");
        this.initSprite(image, sb);
        sb.append("view.style.visibility = 'visible'\n");
        sb.append("document.getElementById('description-region').style.maxHeight = img0.height - 150 + 'px';\n");
        sb.append("document.getElementById('back-button').style.visibility = 'visible'\n");
        sb.append("}\n");
    }

    public void initImg(JLINKImage image, StringBuilder sb) {

        String index;

        sb.append("var img");
        index = image.getImage_Href();
        index = index.replaceAll("[^0-9]", "");
        sb.append(index + " = document.getElementById('" + JLINKConst.IMAGE_LABEL + index + "');\n");

        for (JLINKImage aux : image.getLinked_images()) {
            this.initImg(aux, sb);
        }
    }

    public void initSprite(JLINKImage image, StringBuilder sb) {

        String index, sprite;

        String imageRef = image.getImage_Href().substring(image.getImage_Href().lastIndexOf("=") + 1);

        if (!image.getLinked_images().isEmpty()) {
            for (JLINKImage aux : image.getLinked_images()) {
                String auxRef = aux.getImage_Href().substring(aux.getImage_Href().lastIndexOf("=") + 1);
                index = auxRef;
                index = index.replaceAll("[^0-9]", "");
                sprite = JLINKConst.SPRITE_LABEL + index;
                if (imageRef.equals(JLINKConst.IMAGE_LABEL + 0)) {
                    sb.append("document.getElementById('" + sprite + "').style.display = 'block';\n");
                } else {
                    sb.append("document.getElementById('" + sprite + "').style.display = 'none';\n");
                }
                sb.append("document.getElementById('" + sprite + "').style.left = " + aux.getLink_region_X()
                        + "/100*img0.width + 'px';\n");
                sb.append("document.getElementById('" + sprite + "').style.top = " + aux.getLink_region_Y()
                        + "/100*img0.height + 'px';\n");
                sb.append("img" + index + ".style.top =  " + aux.getLink_region_Y() + "/100*img0.height + 'px';\n");
                sb.append("img" + index + ".style.left =  " + aux.getLink_region_X() + "/100*img0.width + 'px';\n");
                sb.append("img" + index + ".style.height = 0 + 'px';\n");
                sb.append("img" + index + ".style.width = 0 + 'px';\n");
                this.initSprite(aux, sb);
            }
        }
    }

    public void jumpIn(JLINKImage image, StringBuilder sb) {

        String index, sprite;
        String imageRef = image.getImage_Href().substring(image.getImage_Href().lastIndexOf("=") + 1);

        if (!image.getLinked_images().isEmpty()) {
            for (JLINKImage aux : image.getLinked_images()) {
                String auxRef = aux.getImage_Href().substring(aux.getImage_Href().lastIndexOf("=") + 1);

                index = auxRef;
                index = index.replaceAll("[^0-9]", "");
                sprite = JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').addEventListener('click', function () {\n");
                sb.append("var source = document.getElementById('" + imageRef + "');\n");
                sb.append("var destination = document.getElementById('" + auxRef + "');\n");
                sb.append("var view = document.getElementById('view');\n");
                sb.append("const linkageRegion = {xPercent: " + aux.getLink_region_X() + ", yPercent: "
                        + aux.getLink_region_Y() + "};\n");
                sb.append("const duration = " + aux.getLink_duration() + ";\n");
                sb.append("document.getElementById('scene-title').innerHTML = '" + aux.getTitle().replace("'", "\\'")
                        + "';\n");
                sb.append("document.getElementById('scene-description').innerHTML = '"
                        + aux.getNote().replace("'", "\\'") + "';\n");
                sb.append("view.setAttribute('data-scene', '" + auxRef + "');\n");
                this.hideSprites(image, sb);
                sb.append("document.getElementById('back-button').style.opacity = '1';\n");
                sb.append("const ImageSize = {width: source.width, height: source.height};\n");
                sb.append("const xRegion = linkageRegion.xPercent/100*ImageSize.width;\n");
                sb.append("const yRegion = linkageRegion.yPercent/100*ImageSize.height;\n");
                sb.append("const scale = 3;\n");
                sb.append("source.style.height = ImageSize.height + 'px';\n");
                sb.append("source.style.width = ImageSize.width + 'px';\n");
                sb.append("const zoomInSource = source.animate([{\n");
                sb.append("left: (ImageSize.width * 0.5 - xRegion * scale) + 'px',\n");
                sb.append("top: (ImageSize.height * 0.5 - yRegion * scale) + 'px',\n");
                sb.append("width: ImageSize.width * scale + 'px',\n");
                sb.append("height: ImageSize.height * scale + 'px',\n");
                sb.append("opacity: 0 + '%',\n");
                sb.append("easing: 'ease-in-out'\n");
                sb.append("}], duration);\n");
                sb.append("const zoomInDestination = destination.animate([{\n");
                sb.append("left: 0 + 'px',\n");
                sb.append("top: 0 + 'px',\n");
                sb.append("width: ImageSize.width + 'px',\n");
                sb.append("height: ImageSize.height + 'px',\n");
                sb.append("easing: 'ease-in-out'\n");
                sb.append("}], duration);\n");
                sb.append("zoomInDestination.onfinish = () => {\n");
                sb.append("source.style.height = ImageSize.width + 'px';\n");
                sb.append("source.style.width = ImageSize.height + 'px';\n");
                sb.append("source.style.left = 0 + 'px';\n");
                sb.append("source.style.top = 0 + 'px';\n");
                sb.append("source.style.opacity = 0 + '%';\n");
                sb.append("destination.style.opacity = 100 + '%';\n");
                sb.append("destination.style.left = 0 + 'px';\n");
                sb.append("destination.style.top = 0 + 'px';\n");
                sb.append("destination.style.width = ImageSize.width + 'px';\n");
                sb.append("destination.style.height = ImageSize.height + 'px';\n");
                for (JLINKImage auxiliar : aux.getLinked_images()) {
                    index = auxiliar.getImage_Href();
                    index = index.replaceAll("[^0-9]", "");
                    sprite = JLINKConst.SPRITE_LABEL + index;
                    sb.append("document.getElementById('" + sprite + "').style.display = 'block';\n");
                }
                sb.append("};\n");
                sb.append("});\n");
                this.jumpIn(aux, sb);
            }
        }
    }

    public void hideSprites(JLINKImage image, StringBuilder sb) {

        String index, sprite;

        if (!image.getLinked_images().isEmpty()) {
            for (JLINKImage aux : image.getLinked_images()) {
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite = JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').style.display = 'none';\n");
                this.hideSprites(aux, sb);
            }
        }
    }

    public void jumpOut(JLINKImage image, StringBuilder sb) {

        sb.append("document.getElementById('back-button').addEventListener('click', function () {\n");
        sb.append("var view = document.getElementById('view');\n");
        sb.append("if (!(view.getAttribute('data-scene').toString() === '" + JLINKConst.IMAGE_LABEL + "0')) {\n");
        sb.append("var source = document.getElementById(view.getAttribute('data-scene').toString());\n");
        sb.append("var destination = document.getElementById(source.getAttribute('link-to').toString());\n");
        sb.append("var duration;\n");
        this.jumpOutDuration(image, sb);
        sb.append("const ImageSize = {width: source.width, height: source.height};\n");
        sb.append("var linkageRegion;\n");
        this.jumpOutLinkageregion(image, sb);
        this.hideSprites(image, sb);
        sb.append("this.style.pointerEvents = 'none';\n");
        sb.append("const xRegion = linkageRegion.xPercent/100*ImageSize.width;\n");
        sb.append("const yRegion = linkageRegion.yPercent/100*ImageSize.height;\n");
        sb.append("const scale = 3;\n");
        sb.append("const zoomOutSource = source.animate([{\n");
        sb.append("left: xRegion + 'px',\n");
        sb.append("top: yRegion + 'px',\n");
        sb.append("width: 0 + 'px',\n");
        sb.append("height: 0 + 'px',\n");
        sb.append("easing: 'ease-in-out'\n");
        sb.append("}], duration);\n");
        sb.append("const zoomOutDestination = destination.animate([{\n");
        sb.append("left: (ImageSize.width * 0.5 - xRegion * scale) + 'px',\n");
        sb.append("top: (ImageSize.height * 0.5 - yRegion * scale) + 'px',\n");
        sb.append("width: ImageSize.width * scale + 'px',\n");
        sb.append("height: ImageSize.height * scale + 'px'\n");
        sb.append("},{\n");
        sb.append("left: 0 + 'px',\n");
        sb.append("top: 0 + 'px',\n");
        sb.append("width: ImageSize.width + 'px',\n");
        sb.append("height: ImageSize.height + 'px',\n");
        sb.append("opacity: 100 + '%',");
        sb.append("easing: 'ease-in-out'\n");
        sb.append("}], duration);\n");
        sb.append("zoomOutDestination.onfinish = ()=>{\n");
        sb.append("view.setAttribute('data-scene', source.getAttribute('link-to').toString());\n");
        sb.append("destination.style.opacity = 100 + '%';\n");
        sb.append("destination.style.left = 0 + 'px';\n");
        sb.append("destination.style.top = 0 + 'px';\n");
        sb.append("destination.style.width = ImageSize.width + 'px';\n");
        sb.append("destination.style.height = ImageSize.height + 'px';\n");
        sb.append("destination.style.opacity = 100 + '%';\n");
        this.jumpOutOnFinish(image, sb);
        sb.append("source.style.top =  yRegion + 'px';\n");
        sb.append("source.style.left =  xRegion + 'px';\n");
        sb.append("source.style.height = 0 + 'px';\n");
        sb.append("source.style.width = 0 + 'px';\n");
        sb.append("source.style.display = 'block';\n");
        sb.append("this.style.cursor = 'auto';\n");
        sb.append("this.style.pointerEvents = 'auto';\n");
        sb.append("};\n");
        sb.append("}\n");
        sb.append("});\n");
    }

    public void jumpOutDuration(JLINKImage image, StringBuilder sb) {

        for (JLINKImage aux : image.getLinked_images()) {
            String imageRef = aux.getImage_Href().substring(aux.getImage_Href().lastIndexOf("=") + 1);

            sb.append("if (view.getAttribute('data-scene').toString() === '" + imageRef + "') {\n");
            sb.append("duration = " + aux.getLink_duration() + ";\n");
            sb.append("}\n");
            this.jumpOutDuration(aux, sb);
        }
    }

    public void jumpOutLinkageregion(JLINKImage image, StringBuilder sb) {

        for (JLINKImage aux : image.getLinked_images()) {
            String imageRef = aux.getImage_Href().substring(aux.getImage_Href().lastIndexOf("=") + 1);

            sb.append("if(view.getAttribute('data-scene').toString() === '" + imageRef + "'){\n");
            sb.append("linkageRegion = {xPercent: " + aux.getLink_region_X() + ", yPercent: " + aux.getLink_region_Y()
                    + "};\n");
            sb.append("document.getElementById('scene-title').innerHTML = '" + image.getTitle().replace("'", "\\'")
                    + "';\n");
            sb.append("document.getElementById('scene-description').innerHTML = '" + image.getNote().replace("'", "\\'")
                    + "';\n");
            sb.append("}\n");
            this.jumpOutLinkageregion(aux, sb);
        }
    }

    public void jumpOutOnFinish(JLINKImage image, StringBuilder sb) {

        String index, sprite;
        String imageRef = image.getImage_Href().substring(image.getImage_Href().lastIndexOf("=") + 1);

        if (!image.getLinked_images().isEmpty()) {
            sb.append("if (source.getAttribute('link-to').toString() === '" + imageRef + "') {\n");
            if (image.getImage_Href().equals(JLINKConst.IMAGE_LABEL + 0)) {
                sb.append("this.style.opacity = '0.5';\n");
            }
            for (JLINKImage aux : image.getLinked_images()) {

                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite = JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').style.display = 'block';\n");
            }
            sb.append("}\n");
        }
        for (JLINKImage aux : image.getLinked_images()) {
            this.jumpOutOnFinish(aux, sb);
        }
    }

    public void spriteMouseOverAndOut(JLINKImage image, StringBuilder sb) {

        String index, sprite;

        for (JLINKImage aux : image.getLinked_images()) {
            if (!image.getLinked_images().isEmpty()) {
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite = JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').addEventListener('mouseover', function () {\n");
                sb.append("this.style.width = this.width + this.width * 5 / 100 + 'px';\n");
                sb.append("});\n");
                sb.append("document.getElementById('" + sprite + "').addEventListener('mouseout', function () {\n");
                sb.append("this.style.width = this.width - this.width * 5 / 100 + 'px';\n");
                sb.append("});\n");
                this.spriteMouseOverAndOut(aux, sb);
            }
        }
    }

    public void backButtonMouseOverAndOut(JLINKImage image, StringBuilder sb) {

        sb.append("document.getElementById('back-button').addEventListener('mouseover', function () {\n" +
                "if (!(document.getElementById('view').getAttribute('data-scene').toString() === 'image0')) {\n" +
                "this.style.height = 5 + 'vh';\n" +
                "this.style.cursor = 'pointer';\n" +
                "}\n" +
                "});\n" +
                "document.getElementById('back-button').addEventListener('mouseout', function () {\n" +
                "if (!(document.getElementById('view').getAttribute('data-scene').toString() === 'image0')) {\n" +
                "this.style.height = 4 + 'vh';\n" +
                "this.style.cursor = 'auto';\n" +
                "}\n" +
                "});\n");
    }

    public void resizeViewer(JLINKImage image, StringBuilder sb) {

        sb.append("window.onresize = function(){\n");
        sb.append("var view = document.getElementById('view');\n");
        this.initImg(image, sb);
        sb.append("var source = document.getElementById(view.getAttribute('data-scene').toString());\n");
        sb.append("source.style.height = img0.naturalHeight + 'px';\n");
        sb.append("source.style.width = img0.naturalWidth + 'px';\n");
        sb.append("if (source.height*100/document.documentElement.clientHeight > 75) {\n");
        sb.append("source.style.height = 75 + 'vh';\n");
        sb.append("source.style.width = 'auto';\n");
        sb.append("}\n");
        sb.append("if (source.width*100/document.documentElement.clientWidth > 45) {\n");
        sb.append("source.style.width = 45 + 'vw';\n");
        sb.append("source.style.height = 'auto';\n");
        sb.append("}\n");
        sb.append("view.style.height = source.height + 'px';\n");
        sb.append("view.style.width = source.width + 'px';\n");
        this.resizeSprite(image, sb);
        this.resizeImg(image, image, sb);
        sb.append("document.getElementById('description-region').style.maxHeight = source.height - 150 + 'px';\n");
        sb.append("};\n");
    }

    public void resizeSprite(JLINKImage image, StringBuilder sb) {

        String index, sprite;

        if (!image.getLinked_images().isEmpty()) {
            for (JLINKImage aux : image.getLinked_images()) {
                index = aux.getImage_Href();
                index = index.replaceAll("[^0-9]", "");
                sprite = JLINKConst.SPRITE_LABEL + index;
                sb.append("document.getElementById('" + sprite + "').style.left = " + aux.getLink_region_X()
                        + "/100*source.width + 'px';\n");
                sb.append("document.getElementById('" + sprite + "').style.top = " + aux.getLink_region_Y()
                        + "/100*source.height + 'px';\n");
                this.resizeSprite(aux, sb);
            }
        }
    }

    public void resizeImg(JLINKImage image, JLINKImage all, StringBuilder sb) {

        String index;

        index = image.getImage_Href();
        index = index.replaceAll("[^0-9]", "");
        sb.append(
                "if(view.getAttribute('data-scene').toString() === '" + JLINKConst.IMAGE_LABEL + index + "'){\n");
        sb.append("img" + index + ".style.height = source.height + 'px';\n");
        sb.append("img" + index + ".style.width = source.width + 'px';\n");
        this.resizeLinkedImg(image, all, sb);
        sb.append("}\n");

        for (JLINKImage aux : image.getLinked_images()) {
            this.resizeImg(aux, all, sb);
        }
    }

    public void resizeLinkedImg(JLINKImage image, JLINKImage all, StringBuilder sb) {

        String index;

        for (JLINKImage aux : all.getLinked_images()) {
            if (!aux.getImage_Href().equals(image.getImage_Href())) {
                String imageRef = aux.getImage_Href().substring(aux.getImage_Href().lastIndexOf("=") + 1);

                if (!imageRef.equals(JLINKConst.IMAGE_LABEL + 0)) {
                    index = aux.getImage_Href();
                    index = index.replaceAll("[^0-9]", "");
                    sb.append(
                            "img" + index + ".style.top =  " + aux.getLink_region_Y() + "/100*source.height + 'px';\n");
                    sb.append(
                            "img" + index + ".style.left =  " + aux.getLink_region_X() + "/100*source.width + 'px';\n");
                }
            }
            this.resizeLinkedImg(image, aux, sb);
        }
    }

    public void imageTextInformation(StringBuilder sb) {

        sb.append("document.getElementById('text-button').addEventListener('click', function () {\n" +
                "                this.style.opacity = '0.5'; \n" +
                "                document.getElementById('mask').style.opacity = '1';\n" +
                "                document.getElementById('mask').style.visibility = 'visible';\n" +
                "                document.getElementById('back-button').style.opacity = '0.5';  \n" +
                "                document.getElementById('back-button').style.pointerEvents = 'none';\n" +
                "            });\n" +
                "            \n" +
                "            document.getElementById('close-icon').addEventListener('click', function () {\n" +
                "                document.getElementById('text-button').style.opacity = '1';\n" +
                "                document.getElementById('mask').style.opacity = '0';\n" +
                "                document.getElementById('mask').style.visibility = 'hidden';\n" +
                "                document.getElementById('back-button').style.pointerEvents = 'auto';\n" +
                "                if(!(view.getAttribute('data-scene').toString() === '" + JLINKConst.IMAGE_LABEL
                + "0')){\n" +
                "                    document.getElementById('back-button').style.opacity = '1';  \n" +
                "                }\n" +
                "            });\n");
    }
}