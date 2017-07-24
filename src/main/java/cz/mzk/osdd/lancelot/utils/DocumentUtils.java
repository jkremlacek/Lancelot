package cz.mzk.osdd.lancelot.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Jakub Kremlacek
 */
public class DocumentUtils {

    public static Document loadDocumentFromString(String documentString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();

        return db.parse(new ByteArrayInputStream(documentString.getBytes()));
    }

    public static void saveDocument(Document doc, File outputFile, Boolean omitXMLStandalone) throws TransformerException {
        Source domSource = new DOMSource(doc);
        Result output = new StreamResult(outputFile);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        Transformer transformer = transformerFactory.newTransformer();

        //TODO: outputProperty se neprojevuji ve vysledku
        transformer.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXMLStandalone ? "yes" : "no");

        transformer.transform(domSource, output);
    }

    public static void setElementTextContent(Document outDoc, String content, String elementTag) {
        if (content.equals(null) || content.equals("")) {
            throw new IllegalArgumentException(Messages.EMPTY_CONTENT);
        }

        outDoc.getElementsByTagName(elementTag).item(0).setTextContent(content);
    }

    public static void setElementTextContent(Document outDoc, String content, String elementTag, String parentTag) {
        if (content.equals(null) || content.equals("")) {
            throw new IllegalArgumentException(Messages.EMPTY_CONTENT);
        }

        Element parent = (Element) outDoc.getElementsByTagName(parentTag).item(0);

        NodeList children = parent.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName() != null && children.item(i).getNodeName().equals(elementTag)) {
                children.item(i).setTextContent(content);
                return;
            }
        }

        throw new IllegalArgumentException(Messages.ELEMENT_NOT_FOUND + "Requested element: " + elementTag);
    }

    public static void setElementAttributeContent(Document outDoc, String content, String elementTag, String attributeTag) {
        ((Element) outDoc.getElementsByTagName(elementTag).item(0)).setAttribute(attributeTag, content);
    }
}
