package cz.mzk.osdd.lancelot.utils;

/**
 * @author Jakub Kremlacek
 */
public class ModelDefinitions {

    public static final String K4_MAP = "map";
    public static final String K4_PAGE = "page";

    public static final String PROARC_DEVICE = "device";
    public static final String PROARC_MAP = "ndkmap";
    public static final String PROARC_PAGE = "page";

    public static final String AUDIT = "AUDIT";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String FOXML = "FOXML";
    public static final String FULL = "FULL";
    public static final String NDK_ARCHIVAL = "NDK_ARCHIVAL";
    public static final String NDK_ARCHIVAL_MIX = "NDK_ARCHIVAL_MIX";
    public static final String NDK_USER = "NDK_USER";
    public static final String PREVIEW = "PREVIEW";
    public static final String RAW = "RAW";
    public static final String RAW_MIX = "RAW_MIX";
    public static final String RELS_EXT = "RELS-EXT";
    public static final String THUMBNAIL = "THUMBNAIL";

    public static final String[] MAP_COMPONENTS = {
            AUDIT,
            RELS_EXT,
            FOXML
    };

    public static final String[] PAGE_COMPONENTS = {
            AUDIT,
            RAW,
            FULL,
            PREVIEW,
            THUMBNAIL,
            NDK_ARCHIVAL,
            NDK_USER,
            RAW_MIX,
            NDK_ARCHIVAL_MIX,
            RELS_EXT,
            FOXML
    };

    public static String[] getComponents(String model) {
        if (PROARC_MAP.equals(model)) {
            return MAP_COMPONENTS;
        } else if (PROARC_PAGE.equals(model)) {
            return PAGE_COMPONENTS;
        }

        throw new IllegalArgumentException(Messages.MODEL_UNKNOWN);
    }
}
