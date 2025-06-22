package jlink.Config;

import org.mipams.jlink.services.JlinkContentType;
import org.mipams.jlink.services.JlinkXmlGenerator;
import org.mipams.jlink.services.JlinkXmlValidator;
import org.mipams.jumbf.BmffBoxServiceDiscoveryManager;
import org.mipams.jumbf.ContentTypeDiscoveryManager;
import org.mipams.jumbf.services.CoreGeneratorService;
import org.mipams.jumbf.services.CoreParserService;
import org.mipams.jumbf.services.JpegCodestreamGenerator;
import org.mipams.jumbf.services.JpegCodestreamParser;
import org.mipams.jumbf.services.boxes.BinaryDataBoxService;
import org.mipams.jumbf.services.boxes.CborBoxService;
import org.mipams.jumbf.services.boxes.ContiguousCodestreamBoxService;
import org.mipams.jumbf.services.boxes.DescriptionBoxService;
import org.mipams.jumbf.services.boxes.EmbeddedFileDescriptionBoxService;
import org.mipams.jumbf.services.boxes.JsonBoxService;
import org.mipams.jumbf.services.boxes.JumbfBoxService;
import org.mipams.jumbf.services.boxes.PaddingBoxService;
import org.mipams.jumbf.services.boxes.PrivateBoxService;
import org.mipams.jumbf.services.boxes.UuidBoxService;
import org.mipams.jumbf.services.boxes.XmlBoxService;
import org.mipams.jumbf.services.content_types.CborContentType;
import org.mipams.jumbf.services.content_types.ContiguousCodestreamContentType;
import org.mipams.jumbf.services.content_types.EmbeddedFileContentType;
import org.mipams.jumbf.services.content_types.JsonContentType;
import org.mipams.jumbf.services.content_types.UuidContentType;
import org.mipams.jumbf.services.content_types.XmlContentType;
import org.mipams.privsec.entities.replacement.RoiParamHandler;
import org.mipams.privsec.services.boxes.ProtectionDescriptionBoxService;
import org.mipams.privsec.services.boxes.ReplacementDescriptionBoxService;
import org.mipams.privsec.services.boxes.replacement.AppReplacementHandler;
import org.mipams.privsec.services.boxes.replacement.BoxReplacementHandler;
import org.mipams.privsec.services.boxes.replacement.DataBoxHandlerFactory;
import org.mipams.privsec.services.boxes.replacement.FileReplacementHandler;
import org.mipams.privsec.services.boxes.replacement.ParamHandlerFactory;
import org.mipams.privsec.services.boxes.replacement.RoiReplacementHandler;
import org.mipams.privsec.services.content_types.ProtectionContentType;
import org.mipams.privsec.services.content_types.ReplacementContentType;
import org.mipams.privsec.entities.replacement.FileParamHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JumbfConfig {

    @Bean
    public CoreParserService coreParserService() {
        return new CoreParserService();
    }

    @Bean
    public CoreGeneratorService coreGeneratorService() {
        return new CoreGeneratorService();
    }

    @Bean
    public ContentTypeDiscoveryManager contentTypeDiscoveryManager() {
        return new ContentTypeDiscoveryManager();
    }

    @Bean
    public JsonBoxService jsonBoxService() {
        return new JsonBoxService();
    }

    @Bean
    public JsonContentType jsonContentType() {
        return new JsonContentType();
    }

    @Bean
    public XmlBoxService xmlBoxService() {
        return new XmlBoxService();
    }

    @Bean
    public XmlContentType xmlContentType() {
        return new XmlContentType();
    }

    @Bean
    public UuidBoxService uuidBoxService() {
        return new UuidBoxService();
    }

    @Bean
    public UuidContentType uuidContentType() {
        return new UuidContentType();
    }

    @Bean
    public CborBoxService cborBoxService() {
        return new CborBoxService();
    }

    @Bean
    public CborContentType cborContentType() {
        return new CborContentType();
    }

    @Bean
    public ContiguousCodestreamBoxService contiguousCodestreamBoxService() {
        return new ContiguousCodestreamBoxService();
    }

    @Bean
    public ContiguousCodestreamContentType contiguousCodestreamContentType() {
        return new ContiguousCodestreamContentType();
    }

    @Bean
    public EmbeddedFileDescriptionBoxService embeddedFileDescriptionBoxService() {
        return new EmbeddedFileDescriptionBoxService();
    }

    @Bean
    public BinaryDataBoxService binaryDataBoxService() {
        return new BinaryDataBoxService();
    }

    @Bean
    public EmbeddedFileContentType embeddedFileContentType() {
        return new EmbeddedFileContentType();
    }

    @Bean
    public DescriptionBoxService descriptionBoxService() {
        return new DescriptionBoxService();
    }

    @Bean
    public JumbfBoxService jumbfBoxService() {
        return new JumbfBoxService();
    }

    @Bean
    public PaddingBoxService paddingBoxService() {
        return new PaddingBoxService();
    }

    @Bean
    public JpegCodestreamParser jpegCodestreamParser() {
        return new JpegCodestreamParser();
    }

    @Bean
    public JpegCodestreamGenerator jpegCodestreamGenerator() {
        return new JpegCodestreamGenerator();
    }

    @Bean
    public BmffBoxServiceDiscoveryManager bmffBoxServiceDiscoveryManager() {
        return new BmffBoxServiceDiscoveryManager();
    }

    @Bean
    public PrivateBoxService privateBoxService() {
        return new PrivateBoxService();
    }

        @Bean
    public JlinkContentType jlinkContentType() {
        return new JlinkContentType();
    }

    @Bean
    public JlinkXmlGenerator schemaGenerator() {
        return new JlinkXmlGenerator();
    }

    @Bean
    public JlinkXmlValidator schemaValidator() {
        return new JlinkXmlValidator();
    }

    // @Bean
    // public ParamHandlerFactory paramHandlerFactory(){
    //     return new ParamHandlerFactory();
    // }

    @Bean
    public RoiParamHandler roiParamHandler(){
        return new RoiParamHandler();
    }

    @Bean
    public FileParamHandler fileParamHandler(){
        return new FileParamHandler();
    }

    //
    @Bean
    public ProtectionContentType protectionContentType() {
        return new ProtectionContentType();
    }

    @Bean
    public ProtectionDescriptionBoxService protectionDescriptionBoxService() {
        return new ProtectionDescriptionBoxService();
    }

    @Bean
    public ReplacementContentType replacementContentType() {
        return new ReplacementContentType();
    }

    @Bean
    public ReplacementDescriptionBoxService replacementDescriptionBoxService() {
        return new ReplacementDescriptionBoxService();
    }

    @Bean
    public ParamHandlerFactory paramHandlerFactory() {
        return new ParamHandlerFactory();
    }

    @Bean
    public DataBoxHandlerFactory dataBoxHandlerFactory() {
        return new DataBoxHandlerFactory();
    }

    @Bean
    public BoxReplacementHandler boxReplacementHandler() {
        return new BoxReplacementHandler();
    }

    @Bean
    public AppReplacementHandler appReplacementHandler() {
        return new AppReplacementHandler();
    }

    @Bean
    public RoiReplacementHandler roiReplacementHandler() {
        return new RoiReplacementHandler();
    }

    @Bean
    public FileReplacementHandler fileReplacementHandler() {
        return new FileReplacementHandler();
    }
}