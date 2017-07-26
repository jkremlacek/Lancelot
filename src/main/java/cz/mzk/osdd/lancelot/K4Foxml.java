package cz.mzk.osdd.lancelot;

import cz.mzk.osdd.lancelot.utils.Messages;
import cz.mzk.osdd.lancelot.utils.ModelDefinitions;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Jakub Kremlacek
 */
public class K4Foxml {

    public static final List<String> MODELS_WITH_IMAGES = new ArrayList<String>() {{
        add(ModelDefinitions.K4_PAGE);
    }};

    public static final String OUTPUT_FILE_SUFFIX = ".xml";

    public final Document document;
    public final Integer index;

    //String representing model transformed into Proarc variant
    public final String model;

    public final String UUID;
    public final String createdDate;

    public final File originalSource;

    public final List<String> children;

    //foxmltype, # of occurences
    private static Map<String, Integer> counters = new HashMap<>();

    public K4Foxml(File file) throws ParserConfigurationException, IOException, SAXException {
        this.originalSource = file;
        this.UUID = file.getName().substring(0, file.getName().lastIndexOf('.'));

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.parse(file);

        //model name

        String modelName = null;
        NodeList modelList = doc.getElementsByTagName("hasModel");

        if (modelList.getLength() != 1) {
            throw new IllegalStateException(Messages.INVALID_K4_FORMAT_MODEL + " File: " + file.getName());
        }

        Node n = modelList.item(0).getAttributes().getNamedItem("rdf:resource");

        if (n == null) {
            throw new IllegalArgumentException(Messages.INVALID_K4_FORMAT_MODEL_RDF_RESOURCE_ATTRIBUTE + " File: " + file.getName());
        }

        String textContent = n.getNodeValue();

        if (textContent.contains(":")) {
            modelName = textContent.substring(textContent.lastIndexOf(':') + 1, textContent.length());
            modelName = transformModelName(modelName, false);
        }

        if (modelName == null) {
            throw new NullPointerException(Messages.INVALID_K4_FORMAT_MODEL_CONTENT + " File: " + file.getName());
        }

        //date

        NodeList properties = doc.getElementsByTagName("foxml:property");
        String createdDate = null;

        if (properties.getLength() < 1) {
            properties = doc.getElementsByTagName("property");
        }

        for (int i = 0; i < properties.getLength() ; i++) {
            Element property = (Element) properties.item(i);

            if (property.getAttribute("NAME").equals("info:fedora/fedora-system:def/model#createdDate")) {
                createdDate = property.getAttribute("VALUE");
                break;
            }
        }

        if (createdDate == null) {
            throw new IllegalArgumentException(Messages.INVALID_K4_FORMAT_MODEL_CREATED_DATE + " File: " + file.getName());
        }

        this.createdDate = createdDate;

        //index

        Integer counter;

        if (counters.containsKey(modelName)) {
            counter = counters.get(modelName);
        } else {
            counter = 0;
        }

        counter = counter + 1;
        counters.put(modelName, counter);

        this.document = doc;
        this.index = counter;
        this.model = modelName;

        NodeList hasPages = doc.getElementsByTagName("hasPage");

        List<String> pages = new LinkedList<>();

        for (int i = 0; i < hasPages.getLength(); i++) {
            String rdfResource = ((Element) hasPages.item(i)).getAttribute("rdf:resource");

            pages.add(rdfResource.substring(rdfResource.lastIndexOf(":") + 1));
        }

        this.children = Collections.unmodifiableList(pages);
    }

    /**
     * changes model name from K4 variant into ProArc variant
     *
     * @param modelName name in K4 variant
     * @param hasPrefix true if contains "model:" prefix
     * @return modelName in ProArc variant
     */
    public static String transformModelName(String modelName, Boolean hasPrefix) {

        if (hasPrefix) {
            modelName = modelName.substring(modelName.lastIndexOf(':') + 1, modelName.length());

            return "model:" + transformModelName(modelName, false);
        }

        if (modelName.equals(ModelDefinitions.K4_PAGE)) return ModelDefinitions.PROARC_PAGE;
        if (modelName.equals(ModelDefinitions.K4_MAP)) return ModelDefinitions.PROARC_MAP;

        return null;
    }

    /**
     * produces item model, counter and uuid in format "Model_Counter_UuidSuffix"
     *
     * @param suffix suffix to be put at the end of the name
     * @return complete name
     */
    public String getOutputName(String suffix) {
        return model + "_" + String.format("%04d", index) + "_" + UUID + suffix;
    }

    /**
     * produces item model, counter and uuid in format "Model_Counter_Uuid" with default suffix defined in OUTPUT_FILE_SUFFIX
     *
     * @return complete name
     */
    public String getOutputName() {
        return getOutputName(OUTPUT_FILE_SUFFIX);
    }

    public String getLabel() {
        NodeList titles = document.getElementsByTagName("mods:titleInfo");

        for (int i = 0; i < titles.getLength(); i++) {
            Element title = (Element) titles.item(i);

            //received label
            if (!"alternative".equals(title.getAttribute("model"))) {
                return title.getElementsByTagName("mods:title").item(0).getTextContent();
            }
        }

        //no label was present, attempt combine page title

        String pageNumber = null;

        NodeList modsNumbers = document.getElementsByTagName("mods:number");

        for (int i = 0; i < modsNumbers.getLength(); i++) {
            if ("pageNumber".equals(((Element) modsNumbers.item(i).getParentNode()).getAttribute("type"))) {
                pageNumber = modsNumbers.item(i).getTextContent();
            }
        }

        //attempt to receive page title
        String modsPartType = null;

        try {
            modsPartType = ((Element) document.getElementsByTagName("mods:part").item(0)).getAttribute("type");
        } catch (NullPointerException e) {
            throw new IllegalStateException(Messages.METS_PAGE_LABEL_NOT_FOUND);
        }

        if (pageNumber != null && modsPartType != null) {
            return pageNumber + ", " + modsPartType;
        }

        throw new IllegalArgumentException(Messages.METS_LABEL_NOT_FOUND + " File: " + UUID);
    }
}
