package cz.mzk.osdd.lancelot;

import cz.mzk.osdd.lancelot.device.DeviceMock;
import cz.mzk.osdd.lancelot.utils.DocumentTemplates;
import cz.mzk.osdd.lancelot.utils.DocumentUtils;
import cz.mzk.osdd.lancelot.utils.ImageMetadataLoader;
import cz.mzk.osdd.lancelot.utils.Messages;
import cz.mzk.osdd.lancelot.utils.MetsGenerator;
import cz.mzk.osdd.lancelot.utils.ModelDefinitions;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Transforms K4 (5.3.6) Export (with IS) into ProArc (3.3) archive pack
 *
 * @author Jakub Kremlacek
 */
public class ProarcArchivePack {

    private static final String FULL_FILE_SUFFIX = ".jpg";
    private static final String NDK_ARCHIVAL_FILE_SUFFIX = ".jp2";
    private static final String NDK_USER_FILE_SUFFIX = ".jp2";
    private static final String PREVIEW_FILE_SUFFIX = ".jpg";
    private static final String RAW_FILE_SUFFIX = ".jp2";
    private static final String THUMBNAIL_FILE_SUFFIX = ".jpg";
    private static final String XML_FILE_SUFFIX = ".xml";

    private static final String FULL_URL_SUFFIX = "/full/full/0/native.jpg";
    private static final String NDK_ARCHIVAL_URL_SUFFIX = "/original";
    private static final String NDK_USER_URL_SUFFIX = "/original";
    private static final String PREVIEW_URL_SUFFIX = "/preview.jpg";
    private static final String RAW_URL_SUFFIX = "/original";
    private static final String THUMBNAIL_URL_SUFFIX = "/thumb.jpg";

    private static final String RAW_LABEL = "Original digital content of this object";
    private static final String FULL_LABEL = "Presentable version of RAW";
    private static final String PREVIEW_LABEL = "Preview of this object";
    private static final String THUMBNAIL_LABEL = "Thumbnail of this object";
    private static final String NDK_ARCHIVAL_LABEL = "NDK archive copy of RAW";
    private static final String NDK_USER_LABEL = "NDK user copy of RAW";
    private static final String RAW_MIX_LABEL = "Technical metadata for RAW stream.";
    private static final String NDK_ARCHIVAL_MIX_LABEL = "Technical metadata for NDK_ARCHIVAL stream.";
    private static final String RELS_EXT_LABEL = "RDF Statements about this object";

    private final File krameriusExportLocation;
    private final File proarcArchiveLocation;

    private final ExportDirectories exportDirectories;
    private final K4Foxml rootFoxml;

    private String deviceUUID = "11111111-1111-1111-1111-111111111111";

    //uuid, foxml
    private Map<String, K4Foxml> k4FoxmlMap = new HashMap<>();

    private DocumentBuilder documentBuilder;

    /**
     * Loads K4 export into memory preparing the pack for further processing via process* methods
     *
     * @param proarcArchiveLocation output location
     * @param krameriusExportLocation input location
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public ProarcArchivePack(File proarcArchiveLocation, File krameriusExportLocation) throws ParserConfigurationException, SAXException, IOException {
        this(proarcArchiveLocation, krameriusExportLocation, null);
    }

    /**
     * Loads K4 export into memory preparing the pack for further processing via "processXXX" methods. Enables specifying scanning device UUID.
     *
     * @param proarcArchiveLocation output location
     * @param krameriusExportLocation input location
     * @param deviceUUID specific UUID of scanning device to be mocked for processed documents
     * @throws IOException when reading K4 fails
     * @throws ParserConfigurationException note DOM exceptions for parsing XML
     * @throws SAXException note DOM exceptions for parsing XML
     */
    public ProarcArchivePack(File proarcArchiveLocation, File krameriusExportLocation, String deviceUUID) throws IOException, ParserConfigurationException, SAXException {
        if (proarcArchiveLocation == null) throw new IllegalArgumentException(Messages.NULL_ARGUMENT_PROARC);
        if (krameriusExportLocation == null) throw new IllegalArgumentException(Messages.NULL_ARGUMENT_KRAMERIUS);

        this.krameriusExportLocation = krameriusExportLocation;
        this.proarcArchiveLocation = new File(proarcArchiveLocation, krameriusExportLocation.getName());
        
        this.exportDirectories = new ExportDirectories(this.proarcArchiveLocation);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        documentBuilder = dbf.newDocumentBuilder();

        if (deviceUUID != null) {
            this.deviceUUID = deviceUUID;
        }

        loadFoxmls();

        this.rootFoxml = k4FoxmlMap.get(krameriusExportLocation.getName());

        if (rootFoxml == null) {
            throw new IllegalStateException(Messages.FOXML_IN_MAP_NOT_FOUND);
        }
    }

    public void processAudit() throws TransformerException, IOException {
        exportDirectories.AUDIT_DIR.mkdirs();

        for (Map.Entry<String, K4Foxml> foxml : k4FoxmlMap.entrySet()) {
            Document inDoc = foxml.getValue().document;
            Document outDoc = documentBuilder.newDocument();

            outDoc.setXmlStandalone(true);

            NodeList atList = inDoc.getElementsByTagName("audit:auditTrail");

            if (atList.getLength() != 1) {
                throw new IllegalStateException(Messages.INVALID_K4_AUDIT_TRAIL_COUNT + " File: " + foxml.getKey());
            }

            Node newNode = outDoc.importNode(atList.item(0), true);

            ((Element) newNode).setAttribute("xmlns:foxml", "info:fedora/fedora-system:def/foxml#");
            ((Element) newNode).setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");

            outDoc.appendChild(newNode);

            DocumentUtils.saveDocument(outDoc, new File(exportDirectories.AUDIT_DIR, foxml.getValue().getOutputName()), false);
        }

        //add fake device AUDIT file
        DeviceMock.generateAUDIT(exportDirectories.AUDIT_DIR, deviceUUID);
    }

    public void processDescription() throws IOException {
        DeviceMock.generateDESCRIPTION(exportDirectories.DESCRIPTION_DIR, deviceUUID);
    }

    public void processFoxml() throws IOException, TransformerException, ParserConfigurationException, SAXException {
        exportDirectories.FOXML_DIR.mkdirs();

        for (Map.Entry<String, K4Foxml> foxml : k4FoxmlMap.entrySet()) {
            Document inDoc = foxml.getValue().document;
            Document outDoc = documentBuilder.newDocument();

            //copy entire foxml
            Node rootElement = outDoc.importNode(inDoc.getDocumentElement(), true);
            outDoc.appendChild(rootElement);

            //save file
            File outFile = new File(exportDirectories.FOXML_DIR, foxml.getValue().getOutputName());
            DocumentUtils.saveDocument(outDoc, outFile, false);

            //remove foxml prefix

            String contents = FileUtils.readFileToString(outFile, Charset.defaultCharset());

            if (contents.contains("&lt;") || contents.contains("&gt;")) {
                throw new IllegalArgumentException(Messages.K4_CONTAINS_LTGT);
            }

            contents = contents.replaceAll("foxml:", "");
            contents = contents.replaceFirst(":foxml", "");
            FileUtils.writeStringToFile(outFile, contents, Charset.defaultCharset());

            //load file

            outDoc = new K4Foxml(outFile).document;

            //auditTrail add xmlns

            NodeList auditTrailList = outDoc.getElementsByTagName("audit:auditTrail");
            ((Element) auditTrailList.item(0)).setAttribute("xmlns:foxml", "info:fedora/fedora-system:def/foxml#");

            //remove mods:modsCollection while keeping its children

            NodeList modsCollectionList = outDoc.getElementsByTagName("mods:modsCollection");

            Node newParent = modsCollectionList.item(0).getParentNode();
            Node oldParent = modsCollectionList.item(0);

            while (oldParent.getChildNodes().getLength() > 0) {
                newParent.appendChild(oldParent.getFirstChild());
            }

            newParent.removeChild(oldParent);

            //mods:mods add xmlns foxml and mods

            Element modsMods = (Element) outDoc.getElementsByTagName("mods:mods").item(0);

            modsMods.setAttribute("xmlns:foxml", "info:fedora/fedora-system:def/foxml#");
            modsMods.setAttribute("xmlns:mods", "http://www.loc.gov/mods/v3");

            //change modeltype

            NodeList dcTypeList = outDoc.getElementsByTagName("dc:model");

            for (int i = 0; i < dcTypeList.getLength(); i++) {

                String textContent = dcTypeList.item(i).getTextContent();

                if (textContent.contains("model:")) {
                    dcTypeList.item(i).setTextContent(K4Foxml.transformModelName(textContent,true));
                }
            }

            //rdf:RDF add xmlns

            Element rdfRDF = (Element) outDoc.getElementsByTagName("rdf:RDF").item(0);

            rdfRDF.setAttribute("xmlns:fedora-model", "info:fedora/fedora-system:def/model#");
            rdfRDF.setAttribute("xmlns:fedora-rels-ext", "info:fedora/fedora-system:def/relations-external#");
            rdfRDF.setAttribute("xmlns:foxml", "info:fedora/fedora-system:def/foxml#");
            rdfRDF.setAttribute("xmlns:proarc-rels", "http://proarc.lib.cas.cz/relations#");

            //oai_dc:dc add xmlns

            Element oaiDC = (Element) outDoc.getElementsByTagName("oai_dc:dc").item(0);

            oaiDC.setAttribute("xmlns:foxml", "info:fedora/fedora-system:def/foxml#");
            oaiDC.removeAttribute("xsi:schemaLocation");

            //hasModel rename ,remove attribute xmlns, change model

            Element hasModel = (Element) outDoc.getElementsByTagName("hasModel").item(0);

            hasModel.getAttributes().removeNamedItem("xmlns");

            //hasModel.removeAttribute("xmlns");
            hasModel.setAttribute("rdf:resource", "info:fedora/" + K4Foxml.transformModelName(hasModel.getAttribute("rdf:resource"), true));

            outDoc.renameNode(hasModel, "", "fedora-model:hasModel");

            //hasPage rename, remove xmlns - not mandatory, pages do not contain it

            Element hasPage = (Element) outDoc.getElementsByTagName("hasPage").item(0);

            if (hasPage != null) {
                hasPage.removeAttribute("xmlns");
                outDoc.renameNode(hasPage, "", "fedora-rels-ext:hasMember");
            }

            //remove policy since proarc does not recognize it

            removeElementWithName(outDoc, "policy");
            removeElementWithName(outDoc, "dc:rights");

            //itemID remove

            removeElementWithName(outDoc, "itemID");

            //datastream ID=POLICY remove (note that DOM getElementWithId does not work)

            removeElementWithID(outDoc, "POLICY", "datastream");

            //modify page-specific parts in foxml

            if (foxml.getValue().model.equals(ModelDefinitions.PROARC_PAGE)) {

                //remove datastream IMG_FULL and IMG_PREVIEW

                NodeList datastreams = outDoc.getElementsByTagName("datastream");

                for (int i = 1; i < datastreams.getLength(); i++) {
                    String id = ((Element) datastreams.item(i)).getAttribute("ID");

                    if (id.equals("IMG_FULL") || id.equals("IMG_PREVIEW") || id.equals("IMG_THUMB") || id.equals("RELS-EXT")) {
                        Node parent = datastreams.item(i).getParentNode();
                        parent.removeChild(datastreams.item(i));
                        i--;
                    }
                }

                //insert ProArc specific datastreams

                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.RAW,
                        ExportDirectories.RAW_MIMETYPE,
                        RAW_LABEL,
                        Long.toString(new File(exportDirectories.RAW_DIR, foxml.getValue().getOutputName(RAW_FILE_SUFFIX)).length()),
                        "M"
                );

                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.FULL,
                        ExportDirectories.FULL_MIMETYPE,
                        FULL_LABEL,
                        Long.toString(new File(exportDirectories.FULL_DIR, foxml.getValue().getOutputName(FULL_FILE_SUFFIX)).length()),
                        "M"
                );

                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.PREVIEW,
                        ExportDirectories.PREVIEW_MIMETYPE,
                        PREVIEW_LABEL,
                        Long.toString(new File(exportDirectories.PREVIEW_DIR, foxml.getValue().getOutputName(PREVIEW_FILE_SUFFIX)).length()),
                        "M"
                );

                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.THUMBNAIL,
                        ExportDirectories.THUMBNAIL_MIMETYPE,
                        THUMBNAIL_LABEL,
                        Long.toString(new File(exportDirectories.THUMBNAIL_DIR, foxml.getValue().getOutputName(THUMBNAIL_FILE_SUFFIX)).length()),
                        "M"
                );

                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.NDK_ARCHIVAL,
                        ExportDirectories.NDK_ARCHIVAL_MIMETYPE,
                        NDK_ARCHIVAL_LABEL,
                        Long.toString(new File(exportDirectories.NDK_ARCHIVAL_DIR, foxml.getValue().getOutputName(NDK_ARCHIVAL_FILE_SUFFIX)).length()),
                        "M"
                );

                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.NDK_USER,
                        ExportDirectories.NDK_USER_MIMETYPE,
                        NDK_USER_LABEL,
                        Long.toString(new File(exportDirectories.NDK_USER_DIR, foxml.getValue().getOutputName(NDK_USER_FILE_SUFFIX)).length()),
                        "M"
                );

                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.RAW_MIX,
                        ExportDirectories.RAW_MIX_MIMETYPE,
                        RAW_MIX_LABEL,
                        Long.toString(new File(exportDirectories.RAW_MIX_DIR, foxml.getValue().getOutputName(XML_FILE_SUFFIX)).length()),
                        "X",
                        "http://www.loc.gov/mix/v20"
                );

                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.NDK_ARCHIVAL_MIX,
                        ExportDirectories.NDK_ARCHIVAL_MIX_MIMETYPE,
                        NDK_ARCHIVAL_MIX_LABEL,
                        Long.toString(new File(exportDirectories.NDK_ARCHIVAL_MIX_DIR, foxml.getValue().getOutputName(XML_FILE_SUFFIX)).length()),
                        "X",
                        "http://www.loc.gov/mix/v20"
                );
                
                createDatastream(
                        outDoc,
                        foxml.getValue(),
                        ModelDefinitions.RELS_EXT,
                        ExportDirectories.RELS_EXT_MIMETYPE,
                        RELS_EXT_LABEL,
                        Long.toString(new File(exportDirectories.RELS_EXT_DIR, foxml.getValue().getOutputName(XML_FILE_SUFFIX)).length()),
                        "X",
                        "info:fedora/fedora-system:FedoraRELSExt-1.0"
                );
            }


            //save file

            outDoc.setXmlStandalone(true);
            DocumentUtils.saveDocument(outDoc, outFile, false);

            //remove empty xmlns

            contents = FileUtils.readFileToString(outFile, Charset.defaultCharset());

            contents = contents.replaceAll("xmlns=\"\"", "");
            contents = contents.replaceAll("&lt;", "<");
            contents = contents.replaceAll("&gt;", ">");

            FileUtils.writeStringToFile(outFile, contents, Charset.defaultCharset());

            if (contents.contains("-XXX-")) {
                throw new IllegalArgumentException(Messages.INVALID_OUTPUT_FORMAT + " File: " + outFile.getName());
            }
        }

        DeviceMock.generateFOXML(exportDirectories.FOXML_DIR, deviceUUID);
    }

    public void processFull() throws IOException {
        downloadImages(
                FULL_URL_SUFFIX,
                exportDirectories.FULL_DIR,
                FULL_FILE_SUFFIX
        );
    }

    public void processNDKArchival() throws IOException, TransformerException, ParserConfigurationException, SAXException {
        List<File> images = downloadImages(
                NDK_ARCHIVAL_URL_SUFFIX,
                exportDirectories.NDK_ARCHIVAL_DIR,
                NDK_ARCHIVAL_FILE_SUFFIX
        );

        generateMix(exportDirectories.NDK_ARCHIVAL_MIX_DIR, images);
    }

    public void processNDKUser() throws IOException {
        downloadImages(
                NDK_USER_URL_SUFFIX,
                exportDirectories.NDK_USER_DIR,
                NDK_USER_FILE_SUFFIX
        );
    }

    public void processPreview() throws IOException {
        downloadImages(
                PREVIEW_URL_SUFFIX,
                exportDirectories.PREVIEW_DIR,
                PREVIEW_FILE_SUFFIX
        );
    }

    public void processRaw() throws IOException, TransformerException, ParserConfigurationException, SAXException {
        List<File> images = downloadImages(
                RAW_URL_SUFFIX,
                exportDirectories.RAW_DIR,
                RAW_FILE_SUFFIX
        );

        generateMix(exportDirectories.RAW_MIX_DIR, images);
    }

    public void processThumbnail() throws IOException {
        downloadImages(
                THUMBNAIL_URL_SUFFIX,
                exportDirectories.THUMBNAIL_DIR,
                THUMBNAIL_FILE_SUFFIX
        );
    }

    public void processRelsExt() throws TransformerException, ParserConfigurationException, SAXException, IOException {
        exportDirectories.RELS_EXT_DIR.mkdirs();

        for (Map.Entry<String, K4Foxml> foxml : k4FoxmlMap.entrySet()) {
            Document relsDoc = processFoxmlRels(foxml.getValue());

            DocumentUtils.saveDocument(relsDoc, new File(exportDirectories.RELS_EXT_DIR, foxml.getValue().getOutputName()), false);
        }

        DeviceMock.generateRELS(exportDirectories.RELS_EXT_DIR, deviceUUID);
    }
    
    public void processMets() throws TransformerException, IOException {
        MetsGenerator m = new MetsGenerator();
        
        m.createMetsXML(exportDirectories.METS_DIR, rootFoxml, documentBuilder.newDocument(), exportDirectories ,k4FoxmlMap , deviceUUID);
    }

    private Document processFoxmlRels(K4Foxml foxml) throws IOException, SAXException, ParserConfigurationException {
        Document outDoc;

        if (foxml.model.equals(ModelDefinitions.PROARC_PAGE)) {
            outDoc = DocumentUtils.loadDocumentFromString(DocumentTemplates.getPageRels());

            DocumentUtils.setElementTextContent(outDoc, "Kramerius_Export", "proarc-rels:importFile");
            DocumentUtils.setElementAttributeContent(outDoc, "info:fedora/device:" + deviceUUID, "proarc-rels:hasDevice", "rdf:resource");

        } else if (foxml.model.equals(ModelDefinitions.PROARC_MAP)) {
            outDoc = DocumentUtils.loadDocumentFromString(DocumentTemplates.getMapRels());
        } else {
            throw new IllegalArgumentException(Messages.RELS_UNSUPPORTED_MODEL + " Model: " + foxml.model + " File: " + foxml.UUID);
        }

        DocumentUtils.setElementAttributeContent(outDoc, "info:fedora/uuid:" + foxml.UUID, "rdf:Description", "rdf:about");

        //set pages for all models that can contain pages
        if (foxml.model.equals(ModelDefinitions.PROARC_MAP)) {
            processMembers(foxml, outDoc, "hasPage");
        }

        //set members for all models that have children
//        if (foxml.model.equals(K4Foxml.PROARC_YEAR)) {
//            processMembers(foxml, outDoc, );
//        }

        outDoc.setXmlStandalone(true);

        return outDoc;
    }

    private Element createDatastream(
            Document outDoc,
            K4Foxml foxml,
            String datastreamId,
            String mimeType,
            String label,
            String size,
            String controlGroup) throws IOException, SAXException {
        return createDatastream(outDoc, foxml, datastreamId, mimeType, label, size, controlGroup, null);
    }

    private Element createDatastream(
            Document outDoc,
            K4Foxml foxml,
            String datastreamId,
            String mimeType,
            String label,
            String size,
            String controlGroup,
            String formatUri
    ) throws IOException, SAXException {
        if (controlGroup.length() != 1) throw new IllegalArgumentException(Messages.INVALID_PROARC_FORMAT_MODEL_CONTROL_GROUP);

        Element parent = (Element) outDoc.getElementsByTagName("digitalObject").item(0);

        Element datastream = outDoc.createElement("datastream");

        datastream.setAttribute("CONTROL_GROUP", controlGroup.toUpperCase());
        datastream.setAttribute("ID", datastreamId);
        datastream.setAttribute("STATE", "A");
        datastream.setAttribute("VERSIONABLE", "false");

        parent.appendChild(datastream);

        Element datastreamVersion = outDoc.createElement("datastreamVersion");

        if (formatUri != null) {
            datastreamVersion.setAttribute("FORMAT_URI", formatUri);
        }

        datastreamVersion.setAttribute("ID", datastreamId + ".0");
        datastreamVersion.setAttribute("LABEL", label);
        datastreamVersion.setAttribute("CREATED", foxml.createdDate);
        datastreamVersion.setAttribute("MIMETYPE", mimeType);
        datastreamVersion.setAttribute("SIZE", size);

        datastream.appendChild(datastreamVersion);

        if (isWithinImageMimeTypes(mimeType)) {
            Element contentLocation = outDoc.createElement("contentLocation");

            contentLocation.setAttribute("TYPE", "INTERNAL_ID");
            contentLocation.setAttribute("REF", "uuid:" + foxml.UUID + "+" + datastreamId + "+" + datastreamId + ".0");

            datastreamVersion.appendChild(contentLocation);
        } else if (isWithinXMLMimeTypes(mimeType)) {
            Element xmlContent = outDoc.createElement("xmlContent");

            File source;

            if (datastreamId.equals(ModelDefinitions.RAW_MIX)) {
                source = new File(exportDirectories.RAW_MIX_DIR, foxml.getOutputName(XML_FILE_SUFFIX));
            } else if (datastreamId.equals(ModelDefinitions.NDK_ARCHIVAL_MIX)) {
                source = new File(exportDirectories.NDK_ARCHIVAL_MIX_DIR, foxml.getOutputName(XML_FILE_SUFFIX));
            } else if (datastreamId.equals(ModelDefinitions.RELS_EXT)) {
                source = new File(exportDirectories.RELS_EXT_DIR, foxml.getOutputName(XML_FILE_SUFFIX));
            } else {
                throw new IllegalArgumentException(Messages.INVALID_PROARC_FORMAT_MODEL_XML_TYPE + " datastreamId: " + datastreamId + " File: " + foxml.UUID);
            }

            String textContent = FileUtils.readFileToString(source, Charset.defaultCharset());

            textContent = textContent.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>","");
            textContent = textContent.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>","");

            xmlContent.setTextContent(textContent);

            datastreamVersion.appendChild(xmlContent);
        }

        return datastream;
    }

    private boolean isWithinXMLMimeTypes(String mimeType) {
        return mimeType.equals(ExportDirectories.XML_MIMETYPE);
    }

    private boolean isWithinImageMimeTypes(String mimeType) {
        return mimeType.equals(ExportDirectories.JPEG_MIMETYPE) || mimeType.equals(ExportDirectories.JP2_MIMETYPE);
    }

    private void processMembers(K4Foxml foxml, Document outDoc, String K4ElementTagName) {
        NodeList pages = foxml.document.getElementsByTagName(K4ElementTagName);
        Element desc = (Element) outDoc.getElementsByTagName("rdf:Description").item(0);

        for (int i = 0; i < pages.getLength(); i++) {
            String resource = ((Element) pages.item(i)).getAttribute("rdf:resource");

            Element member = outDoc.createElement("fedora-rels-ext:hasMember");
            member.setAttribute("rdf:resource", resource);

            desc.appendChild(member);
        }
    }

    private void generateMix(File rawMixDir, List<File> images) throws TransformerException, ParserConfigurationException, SAXException, IOException {
        rawMixDir.mkdirs();

        ImageMetadataLoader iml = new ImageMetadataLoader();

        for (File image : images) {
            Document mixDoc = generateMixDocument(iml.getMetadataFromImage(image));

            DocumentUtils.saveDocument(mixDoc, new File(rawMixDir, FilenameUtils.removeExtension(image.getName()) + XML_FILE_SUFFIX), true);
        }
    }

    private void loadFoxmls() throws IllegalArgumentException, ParserConfigurationException, IOException, SAXException {
        File[] files = krameriusExportLocation.listFiles();

        for (File file : files) {
            if (file.isDirectory()) {
                throw new IllegalArgumentException(Messages.INVALID_K4_FORMAT_DIRECTORY);
            }

            //put processed k4foxml to map

            K4Foxml k4Foxml = new K4Foxml(file);
            k4FoxmlMap.put(k4Foxml.UUID, k4Foxml);
        }
    }

    private void removeElement(Element element) {
        if (element != null) {
            Node parent = element.getParentNode();
            parent.removeChild(element);
        }
    }

    private void removeElementWithID(Document doc, String id, String name) {
        NodeList list = doc.getElementsByTagName(name);

        for (int i = 0; i < list.getLength(); i++) {
            if (((Element) list.item(i)).getAttribute("ID").equals(id)) {
                removeElement((Element) list.item(i));
            }
        }
    }

    private void removeElementWithName(Document doc, String tagName) {
        Element element = (Element) doc.getElementsByTagName(tagName).item(0);

        removeElement(element);
    }

    private Document generateMixDocument(Document imageData) throws IOException, SAXException, ParserConfigurationException {
        Document outDoc = DocumentUtils.loadDocumentFromString(DocumentTemplates.MIX_CONTENT);

        Element data = (Element) imageData.getElementsByTagName("rdf:Description") .item(0);

        DocumentUtils.setElementTextContent(outDoc, data.getAttribute("Jpeg2000:ImageWidth"), "mix:imageWidth");
        DocumentUtils.setElementTextContent(outDoc, data.getAttribute("Jpeg2000:ImageHeight"), "mix:imageHeight");

        DocumentUtils.setElementTextContent(outDoc, data.getAttribute("Jpeg2000:CaptureXResolution"), "mix:numerator", "mix:xSamplingFrequency");
        DocumentUtils.setElementTextContent(outDoc, data.getAttribute("Jpeg2000:CaptureYResolution"), "mix:numerator", "mix:ySamplingFrequency");

        DocumentUtils.setElementTextContent(outDoc, data.getAttribute("Jpeg2000:CaptureXResolutionUnit"), "mix:samplingFrequencyUnit");

        if (!data.getAttribute("Jpeg2000:CaptureXResolutionUnit").equals(data.getAttribute("Jpeg2000:CaptureYResolutionUnit"))) {
            throw new IllegalArgumentException(Messages.IMAGE_RESOLUTION_UNIT_NOT_EQUAL + " Image: " + data.getAttribute("rdf:about"));
        }

        String[] bitsPerComponentString = data.getAttribute("Jpeg2000:BitsPerComponent").split("\\s+");

        String numberType = bitsPerComponentString[2].toLowerCase();

        if (!numberType.equals("integer") && !numberType.equals("unsigned")) {
            throw new IllegalArgumentException(Messages.IMAGE_SAMPLING_TYPE_NOT_SUPPORTED + "Image: " + data.getAttribute("rdf:about") + " Number model: " + numberType);
        }

        Integer samplesPerPixel = Integer.parseInt(data.getAttribute("Jpeg2000:NumberOfComponents"));

        DocumentUtils.setElementTextContent(outDoc, samplesPerPixel.toString(), "mix:samplesPerPixel");

        Integer bitCount = Integer.parseInt(bitsPerComponentString[0]);

        NodeList bpsv = outDoc.getElementsByTagName("mix:bitsPerSampleValue");

        for (int i = 0; i < bpsv.getLength(); i++) {
            bpsv.item(i).setTextContent(bitCount.toString());
        }

        return outDoc;
    }

    private List<File> downloadImages(String url, File fileDir, String fileSuffix) throws IOException {
        List<File> imageFiles = new ArrayList();

        for (Map.Entry<String, K4Foxml> foxml : k4FoxmlMap.entrySet()) {
            String imageUrl = loadImageInfo(foxml.getValue());

            //if foxml is not containing image links skip it
            if (imageUrl == null) continue;

            URL fullUrl = new URL(imageUrl + url);

            System.out.print(Messages.DOWNLOAD_STARTED + " " + fullUrl.toString() + " ");

            try {
                File image = new File(fileDir, foxml.getValue().getOutputName(fileSuffix));

                FileUtils.copyURLToFile(fullUrl, image);

                imageFiles.add(image);
            } catch (IOException e) {
                System.out.println(Messages.DOWNLOAD_FAILED);
                System.out.flush();

                throw new IOException(e.getMessage());
            }

            System.out.println(Messages.DOWNLOAD_FINISHED);
        }

        return imageFiles;
    }

    private String loadImageInfo(K4Foxml foxml) {
        Document doc = foxml.document;

        if (!K4Foxml.MODELS_WITH_IMAGES.contains(foxml.model)) {
            return null;
        }

        NodeList tilesUrlList = doc.getElementsByTagName("tiles-url");

        if (tilesUrlList.getLength() != 1) {
            throw new IllegalArgumentException(Messages.INVALID_K4_FORMAT_MODEL_TILES_URL_COUNT);
        }

        return tilesUrlList.item(0).getTextContent();
    }
}
