package cz.mzk.osdd.lancelot.utils;

/**
 * @author Jakub Kremlacek
 */
public class DocumentTemplates {
    public static final String AUDIT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><audit:auditTrail xmlns:audit=\"info:fedora/fedora-system:def/audit#\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                    "    <audit:record ID=\"AUDREC1\">\n" +
                    "        <audit:process type=\"Fedora API-M\"/>\n" +
                    "        <audit:action>ingest</audit:action>\n" +
                    "        <audit:componentID/>\n" +
                    "        <audit:responsibility>fedoraAdmin</audit:responsibility>\n" +
                    "        <audit:date>2017-06-20T11:39:01.696Z</audit:date>\n" +
                    "        <audit:justification>Ingested locally</audit:justification>\n" +
                    "</audit:record>\n" +
                    "</audit:auditTrail>";

    public static final String DESCRIPTION =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<mix:mix xmlns:mix=\"http://www.loc.gov/mix/v20\">\n" +
                    "    <mix:ImageCaptureMetadata>\n" +
                    "        <mix:GeneralCaptureInformation>\n" +
                    "            <mix:imageProducer>Moravian Library</mix:imageProducer>\n" +
                    "            <mix:captureDevice>digital still camera</mix:captureDevice>\n" +
                    "        </mix:GeneralCaptureInformation>\n" +
                    "        <mix:ScannerCapture>\n" +
                    "            <mix:scannerManufacturer>i2S DigiBook</mix:scannerManufacturer>\n" +
                    "            <mix:ScannerModel>\n" +
                    "                <mix:scannerModelName>SupraScan Quartz A0 HD</mix:scannerModelName>\n" +
                    "                <mix:scannerModelSerialNo>320901</mix:scannerModelSerialNo>\n" +
                    "            </mix:ScannerModel>\n" +
                    "            <mix:MaximumOpticalResolution>\n" +
                    "                <mix:xOpticalResolution>600</mix:xOpticalResolution>\n" +
                    "                <mix:yOpticalResolution>600</mix:yOpticalResolution>\n" +
                    "                <mix:opticalResolutionUnit>no absolute unit</mix:opticalResolutionUnit>\n" +
                    "            </mix:MaximumOpticalResolution>\n" +
                    "            <mix:ScanningSystemSoftware>\n" +
                    "                <mix:scanningSoftwareName>YooScan</mix:scanningSoftwareName>\n" +
                    "                <mix:scanningSoftwareVersionNo>1.2.0</mix:scanningSoftwareVersionNo>\n" +
                    "            </mix:ScanningSystemSoftware>\n" +
                    "        </mix:ScannerCapture>\n" +
                    "        <mix:DigitalCameraCapture>\n" +
                    "            <mix:cameraSensor>ColorTriLinear</mix:cameraSensor>\n" +
                    "        </mix:DigitalCameraCapture>\n" +
                    "    </mix:ImageCaptureMetadata>\n" +
                    "</mix:mix>";

    public static final String MIX_CONTENT =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                    "<mix:mix xmlns:mix=\"http://www.loc.gov/mix/v20\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
            "    <mix:BasicDigitalObjectInformation>\n" +
            "        <mix:ObjectIdentifier>\n" +
            "            <mix:objectIdentifierType>JHOVE</mix:objectIdentifierType>\n" +
            "                        </mix:ObjectIdentifier>\n" +
            "        <mix:FormatDesignation>\n" +
            "            <mix:formatName>image/jp2</mix:formatName>\n" +
            "            <mix:formatVersion>1.0</mix:formatVersion>\n" +
            "                        </mix:FormatDesignation>\n" +
            "        <mix:byteOrder>big endian</mix:byteOrder>\n" +
            "        <mix:Compression>\n" +
            "            <mix:compressionScheme>JPEG 2000</mix:compressionScheme>\n" +
            "                        </mix:Compression>\n" +
            "                    </mix:BasicDigitalObjectInformation>\n" +
            "    <mix:BasicImageInformation>\n" +
            "        <mix:BasicImageCharacteristics>\n" +
            "            <mix:imageWidth>-XXX-</mix:imageWidth>\n" +
            "            <mix:imageHeight>-XXX-</mix:imageHeight>\n" +
            "                        </mix:BasicImageCharacteristics>\n" +
            "                    </mix:BasicImageInformation>\n" +
            "    <mix:ImageAssessmentMetadata>\n" +
            "        <mix:SpatialMetrics>\n" +
            "            <mix:samplingFrequencyUnit>-XXX-</mix:samplingFrequencyUnit>\n" +
            "            <mix:xSamplingFrequency>\n" +
            "                <mix:numerator>-XXX-</mix:numerator>\n" +
            "                <mix:denominator>1</mix:denominator>\n" +
            "                            </mix:xSamplingFrequency>\n" +
            "            <mix:ySamplingFrequency>\n" +
            "                <mix:numerator>-XXX-</mix:numerator>\n" +
            "                <mix:denominator>1</mix:denominator>\n" +
            "                            </mix:ySamplingFrequency>\n" +
            "                        </mix:SpatialMetrics>\n" +
            "        <mix:ImageColorEncoding>\n" +
            "            <mix:BitsPerSample>\n" +
            "                <mix:bitsPerSampleValue>-XXX-</mix:bitsPerSampleValue>\n" +
            "                <mix:bitsPerSampleValue>-XXX-</mix:bitsPerSampleValue>\n" +
            "                <mix:bitsPerSampleValue>-XXX-</mix:bitsPerSampleValue>\n" +
            "                <mix:bitsPerSampleUnit>integer</mix:bitsPerSampleUnit>\n" +
            "                            </mix:BitsPerSample>\n" +
            "            <mix:samplesPerPixel>-XXX-</mix:samplesPerPixel>\n" +
            "                        </mix:ImageColorEncoding>\n" +
            "                    </mix:ImageAssessmentMetadata>\n" +
            "                </mix:mix>";

    public static String getDeviceFoxml(String uuid) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<digitalObject xmlns=\"info:fedora/fedora-system:def/foxml#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" VERSION=\"1.1\" PID=\"device:" + uuid + "\" xsi:schemaLocation=\"info:fedora/fedora-system:def/foxml# http://www.fedora.info/definitions/1/0/foxml1-1.xsd\">\n" +
                "    <objectProperties>\n" +
                "        <property NAME=\"info:fedora/fedora-system:def/model#state\" VALUE=\"Active\"/>\n" +
                "        <property NAME=\"info:fedora/fedora-system:def/model#label\" VALUE=\"SupraScan A0 \"/>\n" +
                "        <property NAME=\"info:fedora/fedora-system:def/model#ownerId\" VALUE=\"hruskaz\"/>\n" +
                "        <property NAME=\"info:fedora/fedora-system:def/model#createdDate\" VALUE=\"2017-06-20T11:39:01.696Z\"/>\n" +
                "        <property NAME=\"info:fedora/fedora-system:def/view#lastModifiedDate\" VALUE=\"2017-06-20T11:52:29.395Z\"/>\n" +
                "    </objectProperties>\n" +
                "    <datastream ID=\"AUDIT\" CONTROL_GROUP=\"X\" STATE=\"A\" VERSIONABLE=\"false\">\n" +
                "        <datastreamVersion ID=\"AUDIT.0\" LABEL=\"Audit Trail for this object\" CREATED=\"2017-06-20T11:39:01.696Z\" MIMETYPE=\"text/xml\" FORMAT_URI=\"info:fedora/fedora-system:format/xml.fedora.audit\">\n" +
                "            <xmlContent>\n" +
                "                <audit:auditTrail xmlns:audit=\"info:fedora/fedora-system:def/audit#\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\">\n" +
                "                    <audit:record ID=\"AUDREC1\">\n" +
                "                        <audit:process type=\"Fedora API-M\"/>\n" +
                "                        <audit:action>ingest</audit:action>\n" +
                "                        <audit:componentID/>\n" +
                "                        <audit:responsibility>fedoraAdmin</audit:responsibility>\n" +
                "                        <audit:date>2017-06-20T11:39:01.696Z</audit:date>\n" +
                "                        <audit:justification>Ingested locally</audit:justification>\n" +
                "</audit:record>\n" +
                "</audit:auditTrail>\n" +
                "            </xmlContent>\n" +
                "        </datastreamVersion>\n" +
                "    </datastream>\n" +
                "    <datastream ID=\"RELS-EXT\" CONTROL_GROUP=\"X\" STATE=\"A\" VERSIONABLE=\"false\">\n" +
                "        <datastreamVersion ID=\"RELS-EXT.0\" LABEL=\"RDF Statements about this object\" CREATED=\"2017-06-20T11:39:01.693Z\" MIMETYPE=\"text/xml\" FORMAT_URI=\"info:fedora/fedora-system:FedoraRELSExt-1.0\" SIZE=\"456\">\n" +
                "            <xmlContent>\n" +
                "                <rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:fedora-model=\"info:fedora/fedora-system:def/model#\" xmlns:fedora-rels-ext=\"info:fedora/fedora-system:def/relations-external#\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xmlns:proarc-rels=\"http://proarc.lib.cas.cz/relations#\">\n" +
                "                    <rdf:Description rdf:about=\"info:fedora/device:" + uuid + "\">\n" +
                "                        <fedora-model:hasModel rdf:resource=\"info:fedora/proarc:device\"/>\n" +
                "                    </rdf:Description>\n" +
                "                </rdf:RDF>\n" +
                "            </xmlContent>\n" +
                "        </datastreamVersion>\n" +
                "    </datastream>\n" +
                "    <datastream ID=\"DC\" CONTROL_GROUP=\"X\" STATE=\"A\" VERSIONABLE=\"false\">\n" +
                "        <datastreamVersion ID=\"DC.1\" LABEL=\"Dublin Core Record for this object\" CREATED=\"2017-06-20T11:52:29.273Z\" MIMETYPE=\"text/xml\" FORMAT_URI=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" SIZE=\"443\">\n" +
                "            <xmlContent>\n" +
                "                <oai_dc:dc xmlns:oai_dc=\"http://www.openarchives.org/OAI/2.0/oai_dc/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xsi:schemaLocation=\"http://www.openarchives.org/OAI/2.0/oai_dc/ http://www.openarchives.org/OAI/2.0/oai_dc.xsd\">\n" +
                "                    <dc:title>SupraScan A0</dc:title>\n" +
                "                    <dc:type>proarc:device</dc:type>\n" +
                "                    <dc:identifier>device:" + uuid + "</dc:identifier>\n" +
                "</oai_dc:dc>\n" +
                "            </xmlContent>\n" +
                "        </datastreamVersion>\n" +
                "    </datastream>\n" +
                "    <datastream ID=\"DESCRIPTION\" CONTROL_GROUP=\"M\" STATE=\"A\" VERSIONABLE=\"false\">\n" +
                "        <datastreamVersion ID=\"DESCRIPTION.0\" LABEL=\"The device description\" CREATED=\"2017-06-20T11:52:29.341Z\" MIMETYPE=\"text/xml\" FORMAT_URI=\"http://www.loc.gov/mix/v20\" SIZE=\"1451\">\n" +
                "            <contentLocation TYPE=\"INTERNAL_ID\" REF=\"device:" + uuid + "+DESCRIPTION+DESCRIPTION.0\"/>\n" +
                "        </datastreamVersion>\n" +
                "    </datastream>\n" +
                "</digitalObject>";
    }

    public static String getDeviceRels(String deviceUUID) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:fedora-model=\"info:fedora/fedora-system:def/model#\" xmlns:fedora-rels-ext=\"info:fedora/fedora-system:def/relations-external#\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xmlns:proarc-rels=\"http://proarc.lib.cas.cz/relations#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <rdf:Description rdf:about=\"info:fedora/device:" + deviceUUID + "\">\n" +
                "        <fedora-model:hasModel rdf:resource=\"info:fedora/proarc:device:" + deviceUUID+ " \"/>\n" +
                "    </rdf:Description>\n" +
                "</rdf:RDF>";
    }

    public static String getPageRels() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:fedora-model=\"info:fedora/fedora-system:def/model#\" xmlns:fedora-rels-ext=\"info:fedora/fedora-system:def/relations-external#\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xmlns:proarc-rels=\"http://proarc.lib.cas.cz/relations#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <rdf:Description rdf:about=\"info:fedora/uuid:-XXX-\">\n" +
                "        <fedora-model:hasModel rdf:resource=\"info:fedora/model:page\"/>\n" +
                "        <proarc-rels:hasDevice rdf:resource=\"info:fedora/device:-XXX-\"/>\n" +
                "        <proarc-rels:importFile></proarc-rels:importFile>\n" +
                "  </rdf:Description>\n" +
                "</rdf:RDF>";
    }

    public static String getMapRels() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:fedora-model=\"info:fedora/fedora-system:def/model#\" xmlns:fedora-rels-ext=\"info:fedora/fedora-system:def/relations-external#\" xmlns:foxml=\"info:fedora/fedora-system:def/foxml#\" xmlns:proarc-rels=\"http://proarc.lib.cas.cz/relations#\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
                "    <rdf:Description rdf:about=\"info:fedora/uuid:-XXX-\">\n" +
                "        <fedora-model:hasModel rdf:resource=\"info:fedora/model:ndkmap\"/>\n" +
                "  </rdf:Description>\n" +
                "</rdf:RDF>";
    }

    public static String getMets(String deviceUUID) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<mets:mets xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:mets=\"http://www.loc.gov/METS/\" LABEL=\"-XXX-\" TYPE=\"-XXX-\">\n" +
                "    <mets:metsHdr CREATEDATE=\"-XXX-\">\n" +
                "        <mets:agent ROLE=\"CREATOR\" TYPE=\"OTHER\">\n" +
                "            <mets:name>ProArc</mets:name>\n" +
                "        </mets:agent>\n" +
                "    </mets:metsHdr>\n" +
                "    <mets:fileSec>-XXX-\n" +
                "    </mets:fileSec>\n" +
                "    <mets:structMap TYPE=\"PHYSICAL\" LABEL=\"Physical Structure\">-XXX-\n" +
                "    </mets:structMap>\n" +
                "    <mets:structMap TYPE=\"OTHERS\" LABEL=\"Other objects\">\n" +
                "        <mets:div ID=\"DIV_DEVICES\" LABEL=\"List of devices\">\n" +
                "            <mets:div ID=\"div_device_0001\" LABEL=\"SupraScan A0 \" DMDID=\"DMD_DC_device_0001_" + deviceUUID + "\" TYPE=\"proarc:device\" CONTENTIDS=\"device:" + deviceUUID + "\">\n" +
                "                <mets:fptr FILEID=\"FOXML_device_0001_" + deviceUUID + "\"/>\n" +
                "                <mets:fptr FILEID=\"DESCRIPTION_device_0001_" + deviceUUID + "\"/>\n" +
                "                <mets:fptr FILEID=\"AUDIT_device_0001_" + deviceUUID + "\"/>\n" +
                "                <mets:fptr FILEID=\"RELS-EXT_device_0001_" + deviceUUID + "\"/>\n" +
                "            </mets:div>\n" +
                "        </mets:div>\n" +
                "    </mets:structMap>\n" +
                "</mets:mets>";
    }
}
