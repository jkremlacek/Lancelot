package cz.mzk.osdd.lancelot.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Jakub Kremlacek
 */
public class Messages {
    public static final String INVALID_ARGUMENT_COUNT = "Invalid argument count.";

    public static final String MISSING_INPUT = "Input directory must exist!";

    public static final String PROCESS_AUDIT_NAME = "AUDIT";
    public static final String PROCESS_DESCRIPTION_NAME = "DESCRIPTION";
    public static final String PROCESS_FOXML_NAME = "FOXML";
    public static final String PROCESS_FULL_NAME = "FULL";
    public static final String PROCESS_NDK_ARCHIVAL_NAME = "NDK_ARCHIVAL";
    public static final String PROCESS_NDK_USER_NAME = "NDK_USER";
    public static final String PROCESS_PREVIEW_NAME = "PREVIEW";
    public static final String PROCESS_RAW_NAME = "RAW";
    public static final String PROCESS_RELS_EXT_NAME = "RELS-EXT";
    public static final String PROCESS_THUMBNAIL_NAME = "THUMBNAIL";
    public static final String PROCESS_METS_NAME = "METS";

    public static final String MISSING_INPUT_ARG = Messages.INVALID_ARGUMENT_COUNT + " Missing input path argument.";
    public static final String MISSING_OUTPUT_ARG = Messages.INVALID_ARGUMENT_COUNT + " Missing output path argument.";
    public static final String MISSING_UUID_ARG = Messages.INVALID_ARGUMENT_COUNT + " Missing device UUID.";

    public static final String INVALID_K4_AUDIT_TRAIL_COUNT = "Kramerius export cannot contain more than one auditTrail element within foxml.";
    public static final String INVALID_K4_FORMAT_DIRECTORY = "Kramerius export cannot contain directories.";
    public static final String INVALID_K4_FORMAT_MODEL = "Kramerius export must contain hasModel element with \"model:****\" value.";
    public static final String INVALID_K4_FORMAT_MODEL_CONTENT = "Receiving K4 model from foxml failed.";
    public static final String INVALID_K4_FORMAT_MODEL_RDF_RESOURCE_ATTRIBUTE = "Kramerius export must contain rdf:resource attribute within hasModel element.";
    public static final String INVALID_K4_FORMAT_MODEL_TILES_URL_COUNT = "Kramerius export must contain single tiles-url element.";
    public static final String INVALID_K4_FORMAT_MODEL_CREATED_DATE = "Kramerius export must contain created date in foxml:property";

    public static final String NULL_ARGUMENT_KRAMERIUS = "KrameriusExportLocation cannot be null.";

    public static final String NULL_ARGUMENT_PROARC = "ProarcArchiveLocation cannot be null.";
    public static final String DOWNLOAD_STARTED = "Started downloading file:";
    public static final String DOWNLOAD_FINISHED = " ... done";

    public static final String DOWNLOAD_FAILED = " ... failed";
    public static final String IMAGE_RESOLUTION_UNIT_NOT_EQUAL = "Image does not contain same resolution unit for x and y axis.";

    public static final String IMAGE_SAMPLING_TYPE_NOT_SUPPORTED = "Image contains BitsPerComponent with unsupported number model.";
    public static final String RELS_UNSUPPORTED_MODEL = "Requested export model is not supported for generating RELS-EXT file.";

    public static final String INVALID_PROARC_FORMAT_MODEL_CONTROL_GROUP = "Unsupported datastream control group.";
    public static final String INVALID_PROARC_FORMAT_MODEL_XML_TYPE = "Unsupported xml model datastream.";

    public static final String K4_CONTAINS_LTGT = "K4 export contains \"&lt;\" and/or \"&gt;\" characters, these characters are later replaced by their </> counterparts. Conversion terminated.";

    public static final String EMPTY_CONTENT = "Content attribute cannot be null or empty.";
    public static final String ELEMENT_NOT_FOUND = "Requested element was not found";

    public static final String INVALID_OUTPUT_FORMAT = "Conversion was unable to retrieve some values.";

    public static final String METS_LABEL_NOT_FOUND = "Mets label within (mods:titleInfo) or (mods:number + mods:part type) not found.";
    public static final String METS_UNSUPPORTED_DMD_TYPE = "Unsupported DMD model.";
    public static final String METS_PAGE_LABEL_NOT_FOUND = "Mets label for page type not found.";

    public static final String MODEL_UNKNOWN = "Unknown ProArc model.";
    public static final String FOXML_IN_MAP_NOT_FOUND = "UUID not found within k4 map.";

    public static final String PROARC_EXPORT_NOT_EXISTING = "ProArc export directory does not exist.";

    public static final String PROARC_EXPORT_NOTE = "Note that processing METS requires rest of the export conversion completed.";


    private static String getProcessMessage(String processName, Boolean start) {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

        return sdf.format(c.getTime()) + " " + processName + " processing" + (start ? " started" : " finished");
    }

    public static void reportProcessState(String processName, Boolean start) {
        System.out.println(getProcessMessage(processName, start));
    }
}
