package cz.mzk.osdd.lancelot;

/**
 * @author Jakub Kremlacek
 */

public class Main {

    public static void main(String[] args) {
        ExportConverter exportConverter;

        try {
            exportConverter = new ExportConverter(args);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            System.exit(-1);
            return;
        }

        try {
            exportConverter.run();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.exit(0);
    }
}
