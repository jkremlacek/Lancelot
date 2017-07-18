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

        proarcArchivePack.processAudit();
        proarcArchivePack.processDescription();
        proarcArchivePack.processFoxml();
        proarcArchivePack.processFull();
        proarcArchivePack.processNDKArchival();
        proarcArchivePack.processNDKUser();
        proarcArchivePack.processPreview();
        proarcArchivePack.processRaw();
//        proarcArchivePack.processRelsExt();
        proarcArchivePack.processThumbnail();
//        proarcArchivePack.processMets();
    }
}
