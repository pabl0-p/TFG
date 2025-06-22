package jlink.XacmlPDP;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import javax.xml.parsers.*;
import java.io.StringReader;

public class PDP {
    public Document parsefromString(String xacml) throws Exception{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xacml)));
    }

    public String getRequestAttribute(Document requestDoc, String attributeId) {
        NodeList requestAtributes = requestDoc.getElementsByTagName("Attribute");

        for (int i = 0; i < requestAtributes.getLength(); i++) {
            Element attribute = (Element) requestAtributes.item(i);
            if (attributeId.equals(attribute.getAttribute("AttributeId"))) {
                NodeList values = attribute.getElementsByTagName("AttributeValue");
                if (values.getLength() > 0) {
                    return values.item(0).getTextContent();
                }
            }
        }
        return null;
    }
    public String decide(String policy, String request) throws Exception {
        String effect = "NotApplicable";
        
        Document policyDoc = parsefromString(policy);
        Document requestDoc = parsefromString(request);

        NodeList rules = policyDoc.getElementsByTagName("Rule");

        for (int i = 0; i < rules.getLength();i++) {
            Element rule = (Element) rules.item(i);
            effect = rule.getAttribute("Effect");
            NodeList allOfs = rule.getElementsByTagName("AllOf");
            Boolean anyOfMatch = true;

            for (int j = 0; j < allOfs.getLength();j++) {
                Element allOf = (Element) allOfs.item(j);
                NodeList matches = allOf.getElementsByTagName("Match");

                Boolean allMatches = true;

                for (int z = 0; z < matches.getLength();z++) {
                    Element match = (Element) matches.item(z);

                    String policyValue = match.getElementsByTagName("AttributeValue").item(0).getTextContent();
                    Element attributeDesignator = (Element) match.getElementsByTagName("AttributeDesignator").item(0);
                    String attributeId = attributeDesignator.getAttribute("AttributeId");
                    String requestValue = getRequestAttribute(requestDoc, attributeId);
                    if(!policyValue.equals(requestValue)){
                        allMatches = false;
                        break;
                    }  
                }

                if(!allMatches) {
                    anyOfMatch = false;
                }else {
                    anyOfMatch = true;
                    break;
                }
            }

            if (anyOfMatch) {
                break;
            }
        }
        return effect;
    }
}
