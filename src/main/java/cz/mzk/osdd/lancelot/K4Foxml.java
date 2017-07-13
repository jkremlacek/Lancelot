package cz.mzk.osdd.lancelot;

import cz.mzk.osdd.lancelot.utils.Messages;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Jakub Kremlacek
 */
public class K4Foxml {
    public static final String K4_MAP = "map";
    public static final String K4_PAGE = "page";

    public static final String PROARC_DEVICE = "device";
    public static final String PROARC_MAP = "ndkmap";
    public static final String PROARC_PAGE = "page";

    public static final List<String> MODELS_WITH_IMAGES = new ArrayList<String>() {{
        add(K4Foxml.K4_PAGE);
    }};

    public static final String OUTPUT_FILE_SUFFIX = ".xml";

    public final Document document;
    public final Integer index;
    public final String type;
    public final String filename;

    //foxmltype, # of occurences
    private static Map<String, Integer> counters = new HashMap<>();

    public K4Foxml(File file) throws ParserConfigurationException, IOException, SAXException {
        this.filename = file.getName().substring(0, file.getName().lastIndexOf('.'));

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

        if (modelName == null) throw new NullPointerException(Messages.INVALID_K4_FORMAT_MODEL_CONTENT + " File: " + file.getName());

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
        this.type = modelName;
    }

    public static String transformModelName(String modelName, Boolean hasPrefix) {

        if (hasPrefix) {
            modelName = modelName.substring(modelName.lastIndexOf(':') + 1, modelName.length());

            return "model:" + transformModelName(modelName, false);
        }

        if (modelName.equals(K4_PAGE)) return PROARC_PAGE;
        if (modelName.equals(K4_MAP)) return PROARC_MAP;

        return null;
    }

    public String getOutputFilename(String suffix) {
        return type + "_" + String.format("%04d", index) + "_" + filename + suffix;
    }

    public String getOutputFilename() {
        return getOutputFilename(OUTPUT_FILE_SUFFIX);
    }

    public K4Foxml load(File file) throws IOException, SAXException, ParserConfigurationException {
        return new K4Foxml(file);
    }
}
