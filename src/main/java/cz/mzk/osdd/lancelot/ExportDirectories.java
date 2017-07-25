package cz.mzk.osdd.lancelot;

import cz.mzk.osdd.lancelot.utils.ModelDefinitions;
import java.io.File;

/**
 * @author Jakub Kremlacek
 */
public class ExportDirectories {

    public static final String JP2_MIMETYPE = "image/jp2";

    public static final String NDK_USER_MIMETYPE = JP2_MIMETYPE;
    public static final String NDK_ARCHIVAL_MIMETYPE = JP2_MIMETYPE;
    public static final String RAW_MIMETYPE = JP2_MIMETYPE;

    public static final String JPEG_MIMETYPE = "image/jpeg";

    public static final String THUMBNAIL_MIMETYPE = JPEG_MIMETYPE;
    public static final String PREVIEW_MIMETYPE = JPEG_MIMETYPE;
    public static final String FULL_MIMETYPE = JPEG_MIMETYPE;

    public static final String XML_MIMETYPE = "text/xml";

    public static final String AUDIT_MIMETYPE = XML_MIMETYPE;
    public static final String FOXML_MIMETYPE = XML_MIMETYPE;
    public static final String NDK_ARCHIVAL_MIX_MIMETYPE = XML_MIMETYPE;
    public static final String RAW_MIX_MIMETYPE = XML_MIMETYPE;
    public static final String RELS_EXT_MIMETYPE = XML_MIMETYPE;
    public static final String DESCRIPTION_MIMETYPE = XML_MIMETYPE;

    public final File AUDIT_DIR;
    public final File DESCRIPTION_DIR;
    public final File FOXML_DIR;
    public final File FULL_DIR;
    public final File NDK_ARCHIVAL_DIR;
    public final File NDK_ARCHIVAL_MIX_DIR;
    public final File NDK_USER_DIR;
    public final File PREVIEW_DIR;
    public final File RAW_DIR;
    public final File RAW_MIX_DIR;
    public final File RELS_EXT_DIR;
    public final File THUMBNAIL_DIR;
    public final File METS_DIR;

    public ExportDirectories(File proarcArchiveLocation) {
        AUDIT_DIR = new File(proarcArchiveLocation, ModelDefinitions.AUDIT);
        DESCRIPTION_DIR = new File(proarcArchiveLocation, ModelDefinitions.DESCRIPTION);
        FOXML_DIR = new File(proarcArchiveLocation, ModelDefinitions.FOXML);
        FULL_DIR = new File(proarcArchiveLocation, ModelDefinitions.FULL);
        NDK_ARCHIVAL_DIR = new File(proarcArchiveLocation, ModelDefinitions.NDK_ARCHIVAL);
        NDK_ARCHIVAL_MIX_DIR = new File(proarcArchiveLocation, ModelDefinitions.NDK_ARCHIVAL_MIX);
        NDK_USER_DIR = new File(proarcArchiveLocation, ModelDefinitions.NDK_USER);
        PREVIEW_DIR = new File(proarcArchiveLocation, ModelDefinitions.PREVIEW);
        RAW_DIR = new File(proarcArchiveLocation, ModelDefinitions.RAW);
        RAW_MIX_DIR = new File(proarcArchiveLocation, ModelDefinitions.RAW_MIX);
        RELS_EXT_DIR = new File(proarcArchiveLocation, ModelDefinitions.RELS_EXT);
        THUMBNAIL_DIR = new File(proarcArchiveLocation, ModelDefinitions.THUMBNAIL);
        METS_DIR = proarcArchiveLocation;
    }
}
