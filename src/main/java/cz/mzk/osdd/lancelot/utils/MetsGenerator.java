package cz.mzk.osdd.lancelot.utils;

import cz.mzk.osdd.lancelot.ExportDirectories;
import cz.mzk.osdd.lancelot.K4Foxml;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * @author Jakub Kremlacek
 */
public class MetsGenerator {

    private Document outDoc;
    private Element root;
    private ExportDirectories exportDirectories;
    private K4Foxml rootFoxml;
    private String deviceUUID;

    private Map<String, K4Foxml> k4FoxmlMap;

    public void createMetsXML(File exportDir, K4Foxml rootFoxml, Document outDoc, ExportDirectories exportDirectories, Map<String, K4Foxml> k4FoxmlMap, String deviceUUID) throws TransformerException, IOException {
        this.outDoc = outDoc;
        this.exportDirectories = exportDirectories;
        this.k4FoxmlMap = k4FoxmlMap;
        this.deviceUUID = deviceUUID;
        this.rootFoxml = rootFoxml;

        this.root = createRootMets(outDoc);

        outDoc.appendChild(this.root);

        createMetsHdr();
        createDmdSecs();
        createFileSecs();
        createStructMaps();

        File outFile = new File(exportDir, "mets.xml");

        DocumentUtils.saveDocument(this.outDoc, outFile, false);

        String contents = FileUtils.readFileToString(outFile, Charset.defaultCharset());

        contents = contents.replaceAll("&lt;", "<");
        contents = contents.replaceAll("&gt;", ">");

        FileUtils.writeStringToFile(outFile, contents, Charset.defaultCharset());
    }

    private void createStructMaps() {
        //physical
        Element structMap = outDoc.createElement("mets:structMap");

        structMap.setAttribute("TYPE", "PHYSICAL");
        structMap.setAttribute("LABEL", "Physical Structure");

        createMetsDiv(structMap, rootFoxml);

        root.appendChild(structMap);

        //device
        Element structMapOthers = outDoc.createElement("mets:structMap");

        structMapOthers.setAttribute("TYPE", "OTHERS");
        structMapOthers.setAttribute("LABEL", "Other objects");
        structMapOthers.setTextContent(DocumentTemplates.getDeviceStructMap(deviceUUID));

        root.appendChild(structMapOthers);
    }

    private void createMetsDiv(Element parent, K4Foxml foxml) {
        Element metsDiv = outDoc.createElement("mets:div");

        metsDiv.setAttribute("ID", "div_" + foxml.model + "_" + foxml.index);
        metsDiv.setAttribute("LABEL", foxml.getLabel());
        metsDiv.setAttribute("DMD_ID", "DMD_MODS_" + foxml.getOutputName("") + " " + "DMD_DC_" + foxml.getOutputName(""));
        metsDiv.setAttribute("TYPE", "model:" + foxml.model);
        metsDiv.setAttribute("CONTENTIDS", "uuid:" + foxml.UUID);

        for (String component : ModelDefinitions.getComponents(foxml.model)) {
            Element fptr = outDoc.createElement("mets:fptr");

            fptr.setAttribute("FILEID", component + "_" + foxml.getOutputName(""));

            metsDiv.appendChild(fptr);
        }

        for (String uuid : foxml.children) {
            if (!k4FoxmlMap.containsKey(uuid)) {
                throw new IllegalStateException(Messages.FOXML_IN_MAP_NOT_FOUND);
            }

            createMetsDiv(metsDiv, k4FoxmlMap.get(uuid));
        }

        parent.appendChild(metsDiv);
    }

    private void createFileSecs() throws IOException {
        Element fileSec = outDoc.createElement("mets:fileSec");
        root.appendChild(fileSec);

        processFileGrp(ModelDefinitions.AUDIT, exportDirectories.AUDIT_DIR, fileSec, ExportDirectories.AUDIT_MIMETYPE);
        processFileGrp("RELS-EXT", exportDirectories.RELS_EXT_DIR, fileSec, ExportDirectories.RELS_EXT_MIMETYPE);
        processFileGrp("FOXML", exportDirectories.FOXML_DIR, fileSec, ExportDirectories.FOXML_MIMETYPE);
        processFileGrp("RAW", exportDirectories.RAW_DIR, fileSec, ExportDirectories.RAW_MIMETYPE);
        processFileGrp("FULL", exportDirectories.FULL_DIR, fileSec, ExportDirectories.FULL_MIMETYPE);
        processFileGrp("PREVIEW", exportDirectories.PREVIEW_DIR, fileSec, ExportDirectories.PREVIEW_MIMETYPE);
        processFileGrp("THUMBNAIL", exportDirectories.THUMBNAIL_DIR, fileSec, ExportDirectories.THUMBNAIL_MIMETYPE);
        processFileGrp("NDK_ARCHIVAL", exportDirectories.NDK_ARCHIVAL_DIR, fileSec, ExportDirectories.NDK_ARCHIVAL_MIMETYPE);
        processFileGrp("NDK_USER", exportDirectories.NDK_USER_DIR, fileSec, ExportDirectories.NDK_USER_MIMETYPE);
        processFileGrp("RAW_MIX", exportDirectories.RAW_MIX_DIR, fileSec, ExportDirectories.RAW_MIX_MIMETYPE);
        processFileGrp("NDK_ARCHIVAL_MIX", exportDirectories.NDK_ARCHIVAL_MIX_DIR, fileSec, ExportDirectories.NDK_ARCHIVAL_MIX_MIMETYPE);
        processFileGrp("DESCRIPTION", exportDirectories.DESCRIPTION_DIR, fileSec, ExportDirectories.DESCRIPTION_MIMETYPE);
    }

    private void processFileGrp(String name, File groupDir, Element fileSec, String mimetype) throws IOException {
        Element fileGrp = outDoc.createElement("mets:fileGrp");

        fileGrp.setAttribute("ID", name);

        fileSec.appendChild(fileGrp);

        File[] files = groupDir.listFiles();

        if (!groupDir.exists()) {
            throw new IllegalStateException(Messages.PROARC_EXPORT_NOT_EXISTING + " " + groupDir.getName() + "\n" + Messages.PROARC_EXPORT_NOTE);
        }

        for (File file : files) {
            Element metsFile = outDoc.createElement("mets:file");

            metsFile.setAttribute("ID",name + "_" + FilenameUtils.removeExtension(file.getName()));
            metsFile.setAttribute("MIMETYPE", mimetype);
            metsFile.setAttribute("SIZE", Long.toString(file.length()));
            metsFile.setAttribute("CREATED", rootFoxml.createdDate);

            FileInputStream fis = new FileInputStream(file);

            metsFile.setAttribute("CHECKSUM", DigestUtils.md5Hex(fis));
            metsFile.setAttribute("CHECKSUMTYPE", "MD5");

            fileGrp.appendChild(metsFile);

            Element fLocat = outDoc.createElement("mets:FLocat");

            fLocat.setAttribute("xlink:href", "./" + name + "/" + file.getName());
            fLocat.setAttribute("LOCTYPE", "URL");

            metsFile.appendChild(fLocat);
        }
    }

    private void createDmdSecs() throws IOException {
        for (Map.Entry<String, K4Foxml> foxml : k4FoxmlMap.entrySet()) {
            String mods = retreiveStringSegmentFromFoxml(foxml.getValue(), "mods:mods");

            String modsNS = " xmlns:mods=\"http://www.loc.gov/mods/v3\" ";

            if (!mods.contains(modsNS)) {
                int splitPos = "mods:mods ".length();
                mods = mods.substring(0, splitPos) + modsNS + mods.substring(splitPos, mods.length());
            }

            createDmdSecWithContent(foxml.getValue(), "text/xml", MDType.MODS, mods);

            String dc = retreiveStringSegmentFromFoxml(foxml.getValue(), "oai_dc:dc");

            createDmdSecWithContent(foxml.getValue(), "text/xml", MDType.DC, dc);
        }
    }

    private String retreiveStringSegmentFromFoxml(K4Foxml foxml, String tagName) throws IOException {
        String xml = FileUtils.readFileToString(foxml.originalSource, Charset.defaultCharset());

        int start = xml.indexOf("<" + tagName + " ");

        String endingTag = "</" + tagName + ">";

        int end = xml.indexOf(endingTag) + endingTag.length() + 1;

        return xml.substring(start, end);
    }

    private Element createRootMets(Document doc) {
        Element root = doc.createElement("mets:mets");

        root.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
        root.setAttribute("xmlns:mets", "http://www.loc.gov/METS/");

        root.setAttribute("LABEL", rootFoxml.getLabel());
        root.setAttribute("TYPE", rootFoxml.model);

        return root;
    }

    private Element createDmdSec(K4Foxml foxml, String mimetype, MDType mdType) {
        Element dmdSec = outDoc.createElement("mets:dmdSec");
        Element mdWrap = outDoc.createElement("mets:mdWrap");

        mdWrap.setAttribute("MIMETYPE", mimetype);

        switch (mdType) {
            case DC:
                dmdSec.setAttribute("ID", "DMD_DC_" + foxml.getOutputName(""));
                mdWrap.setAttribute("MDTYPE", "DC");
                break;
            case MODS:
                dmdSec.setAttribute("ID", "DMD_MODS_" + foxml.getOutputName(""));
                mdWrap.setAttribute("MDTYPE", "MODS");
                break;
            default:
                throw new IllegalArgumentException(Messages.METS_UNSUPPORTED_DMD_TYPE);
        }

        dmdSec.appendChild(mdWrap);
        dmdSec.setAttribute("CREATED", foxml.createdDate);

        root.appendChild(dmdSec);

        return mdWrap;
    }

    private Element createDmdSecWithContent(K4Foxml foxml, String mimetype, MDType mdType, String content) throws IOException {
        Element mdWrap = createDmdSec(foxml, mimetype, mdType);

        Element xmlData = outDoc.createElement("mets:xmlData");

        xmlData.setTextContent(content);

        mdWrap.appendChild(xmlData);

        return mdWrap;
    }

    private void createMetsHdr() {
        Element metsHdr = outDoc.createElement("mets:metsHdr");

        metsHdr.setAttribute("CREATEDATE", rootFoxml.createdDate);

        root.appendChild(metsHdr);

        Element metsAgent = outDoc.createElement("mets:agent");

        metsAgent.setAttribute("ROLE", "CREATOR");
        metsAgent.setAttribute("TYPE", "OTHER");

        metsHdr.appendChild(metsAgent);

        Element metsName = outDoc.createElement("mets:name");

        metsName.setTextContent("ProArc");

        metsAgent.appendChild(metsName);
    }

    enum MDType {
        MODS, DC
    }
}
