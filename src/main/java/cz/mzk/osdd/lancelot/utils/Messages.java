package cz.mzk.osdd.lancelot.utils;

/**
 * @author Jakub Kremlacek
 */
public class Messages {
    public static final String INVALID_ARGUMENT_COUNT = "Invalid argument count.";

    public static final String MISSING_INPUT = "Input directory must exist!";

    public static final String MISSING_INPUT_ARG = Messages.INVALID_ARGUMENT_COUNT + " Missing input path argument.";
    public static final String MISSING_OUTPUT_ARG = Messages.INVALID_ARGUMENT_COUNT + " Missing output path argument.";
    public static final String MISSING_UUID_ARG = Messages.INVALID_ARGUMENT_COUNT + " Missing device UUID.";

    public static final String INVALID_K4_FORMAT_DIRECTORY = "Kramerius export cannot contain directories.";
    public static final String INVALID_K4_AUDIT_TRAIL_COUNT = "Kramerius export cannot contain more than one auditTrail element within foxml.";
    public static final String INVALID_K4_FORMAT_MODEL = "Kramerius export must contain hasModel element with \"model:****\" value.";
    public static final String INVALID_K4_FORMAT_MODEL_CONTENT = "Receiving K4 model from foxml failed.";
    public static final String INVALID_K4_FORMAT_MODEL_RDF_RESOURCE_ATTRIBUTE = "Kramerius export must contain rdf:resource attribute within hasModel element.";
    public static final String INVALID_K4_FORMAT_MODEL_TILES_URL_COUNT = "Kramerius export must contain single tiles-url element.";

    public static final String NULL_ARGUMENT_KRAMERIUS = "KrameriusExportLocation cannot be null.";
    public static final String NULL_ARGUMENT_PROARC = "ProarcArchiveLocation cannot be null.";

    public static final String DOWNLOAD_STARTED = "Started downloading file:";
    public static final String DOWNLOAD_FINISHED = " ... done";
    public static final String DOWNLOAD_FAILED = " ... failed";

    public static final String IMAGE_RESOLUTION_UNIT_NOT_EQUAL = "Image does not contain same resolution unit for x and y axis.";
    public static final String IMAGE_SAMPLING_TYPE_NOT_SUPPORTED = "Image contains BitsPerComponent with unsupported number type.";
}
