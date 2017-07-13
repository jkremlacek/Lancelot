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
    public static final String INVALID_K4_FORMAT_MODEL_ATTRIBUTE = "Kramerius export must contain rdf:resource attribute within hasModel element.";

    public static final String NULL_ARGUMENT_KRAMERIUS = "KrameriusExportLocation cannot be null.";
    public static final String NULL_ARGUMENT_PROARC = "ProarcArchiveLocation cannot be null.";

}
