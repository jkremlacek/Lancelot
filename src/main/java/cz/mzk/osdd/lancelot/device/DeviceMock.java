package cz.mzk.osdd.lancelot.device;

import cz.mzk.osdd.lancelot.K4Foxml;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.commons.io.FileUtils;

/**
 * @author Jakub Kremlacek
 */
public class DeviceMock {
    public static void generateAUDIT(File auditDir, String deviceUUID) throws IOException {
        File deviceAudit = getFile(auditDir, deviceUUID);
        FileUtils.writeStringToFile(deviceAudit, DeviceTextContentConstants.AUDIT , Charset.defaultCharset());
    }

    public static void generateDESCRIPTION(File descriptionDir, String deviceUUID) throws IOException {
        File deviceDescription = getFile(descriptionDir, deviceUUID);
        FileUtils.writeStringToFile(deviceDescription, DeviceTextContentConstants.DESCRIPTION, Charset.defaultCharset());
    }

    public static void generateFOXML(File foxmlDir, String deviceUUID) throws IOException {
        File deviceFoxml = getFile(foxmlDir, deviceUUID);
        FileUtils.writeStringToFile(deviceFoxml, DeviceTextContentConstants.getFoxml(deviceUUID), Charset.defaultCharset());
    }

    private static File getFile(File descriptionDir, String deviceUUID) {
        return new File(descriptionDir, K4Foxml.PROARC_DEVICE + "_0001_" + deviceUUID + K4Foxml.OUTPUT_FILE_SUFFIX);
    }
}
