package cz.mzk.osdd.lancelot;

import cz.mzk.osdd.lancelot.utils.Messages;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 * @author Jakub Kremlacek
 */

public class ExportConverter {

    public static final String OUTPUT_ARGUMENT = "-o";
    public static final String INPUT_ARGUMENT = "-i";
    public static final String DEVICE_UUID_ARGUMENT = "-d";
    public static final String MULTIPLE_OBJECTS_ARGUMENT = "-b";

    private boolean batchJob = false;

    private File krameriusExportLocation;
    private File proarcArchiveLocation;

    private String deviceUUID;

    /**
     * Processes commandline arguments and initializes convertor.
     *
     * Mandatory arguments OUTPUT_ARGUMENT and INPUT_ARGUMENT
     * Optional argument DEVICE_UUID_ARGUMENT
     *
     * @param args argument array from commandline
     */
    public ExportConverter(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(OUTPUT_ARGUMENT)) {
                try {
                    proarcArchiveLocation = new File(args[i+1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException(Messages.MISSING_OUTPUT_ARG);
                }

                i++;

                if (!proarcArchiveLocation.exists()) {
                    proarcArchiveLocation.mkdirs();
                }
            } else if (args[i].equals(INPUT_ARGUMENT)) {
                try {
                    krameriusExportLocation = new File(args[i+1]);
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException(Messages.MISSING_INPUT_ARG);
                }

                i++;

                if (!krameriusExportLocation.exists()) {
                    throw new IllegalArgumentException(Messages.MISSING_INPUT);
                }
            } else if (args[i].equals(DEVICE_UUID_ARGUMENT)) {
                try {
                    deviceUUID = args[i+1];
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new IllegalArgumentException(Messages.MISSING_UUID_ARG);
                }

                i++;
            } else if (args[i].equals(MULTIPLE_OBJECTS_ARGUMENT)) {
                batchJob = true;
            }
        }
    }

    /**
     * Processes input directory containing Kramerius export and transforms it into Proarc Archive pack
     */
    public void run() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        if (batchJob) {
            for (File krameriusExportObject : krameriusExportLocation.listFiles()) {
                runPackTransformation(proarcArchiveLocation, krameriusExportObject);
            }
        } else {
            runPackTransformation(proarcArchiveLocation, krameriusExportLocation);
        }
    }

    private void runPackTransformation(File proarcLocation, File krameriusLocation) throws ParserConfigurationException, SAXException, IOException, TransformerException {
        ProarcArchivePack proarcArchivePack = new ProarcArchivePack(proarcLocation, krameriusLocation, deviceUUID);

        Messages.reportProcessState(Messages.PROCESS_AUDIT_NAME, true);
        proarcArchivePack.processAudit();
        Messages.reportProcessState(Messages.PROCESS_AUDIT_NAME, false);

        Messages.reportProcessState(Messages.PROCESS_DESCRIPTION_NAME, true);
        proarcArchivePack.processDescription();
        Messages.reportProcessState(Messages.PROCESS_DESCRIPTION_NAME, false);

        //Messages.reportProcessState(Messages.PROCESS_FULL_NAME, true);
        //proarcArchivePack.processFull();
        //Messages.reportProcessState(Messages.PROCESS_FULL_NAME, false);

        Messages.reportProcessState(Messages.PROCESS_NDK_ARCHIVAL_NAME, true);
        proarcArchivePack.processNDKArchival();
        Messages.reportProcessState(Messages.PROCESS_NDK_ARCHIVAL_NAME, false);

        Messages.reportProcessState(Messages.PROCESS_NDK_USER_NAME, true);
        proarcArchivePack.processNDKUser();
        Messages.reportProcessState(Messages.PROCESS_NDK_USER_NAME, false);

        Messages.reportProcessState(Messages.PROCESS_PREVIEW_NAME, true);
        proarcArchivePack.processPreview();
        Messages.reportProcessState(Messages.PROCESS_PREVIEW_NAME, false);

        Messages.reportProcessState(Messages.PROCESS_RAW_NAME, true);
        proarcArchivePack.processRaw();
        Messages.reportProcessState(Messages.PROCESS_RAW_NAME, false);

        Messages.reportProcessState(Messages.PROCESS_RELS_EXT_NAME, true);
        proarcArchivePack.processRelsExt();
        Messages.reportProcessState(Messages.PROCESS_RELS_EXT_NAME, false);

        Messages.reportProcessState(Messages.PROCESS_THUMBNAIL_NAME, true);
        proarcArchivePack.processThumbnail();
        Messages.reportProcessState(Messages.PROCESS_THUMBNAIL_NAME, false);

        Messages.reportProcessState(Messages.PROCESS_FOXML_NAME, true);
        proarcArchivePack.processFoxml();
        Messages.reportProcessState(Messages.PROCESS_FOXML_NAME, false);

        //Messages.reportProcessState(Messages.PROCESS_METS_NAME, true);
        //proarcArchivePack.processMets();
        //Messages.reportProcessState(Messages.PROCESS_METS_NAME, false);
    }
}
