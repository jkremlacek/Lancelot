package cz.mzk.osdd.lancelot.utils;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * class for calling external process for retrieving metadata from image
 *
 * note that external process must be able to output data in XML format into std:out
 *
 * @author Jakub Kremlacek
 */
public class ImageMetadataLoader {

    public static String processName;
    public static String[] args;

    /**
     * creates default loader using exiftool
     */
    public ImageMetadataLoader() {
        processName = "exiftool";
        args = new String[] {"-X", "-s"};
    }

    public ImageMetadataLoader(String processName, String[] args) {
        this.processName = processName;
        this.args = args;
    }

    public Document getMetadataFromImage(File image) throws IOException, ParserConfigurationException, SAXException {
        String result = execCmd(getCMD(image));

        return DocumentUtils.loadDocumentFromString(result);
    }

    private String getCMD(File image) {
        String cmd = processName;

        for (String arg : args) {
            cmd += " " + arg;
        }

        cmd += " " + image.getAbsoluteFile();

        return cmd;
    }

    private static String execCmd(String cmd) throws IOException {
        java.util.Scanner s = new java.util.Scanner(Runtime.getRuntime().exec(cmd).getInputStream()).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
