package core.extentreport.extentfiles;

import core.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public class ExtentConfigFile {

    // Set java and slf4j logger
    private static final Logger SLF4J_LOGGER = LoggerFactory.getLogger(ExtentConfigFile.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(ExtentConfigFile.class.getName());

    private static FileInputStream in;
    private static FileOutputStream out;

    /*
     * private constructor
     */
    private ExtentConfigFile() {
        // No data
    }

    /*
     * Set-up extent report configuration
     */
    public static void setupExtentReportConfig() {

        Path path = Path.of(Constants.EXTENT_CONFIG_FILE_PATH);

        if (!Files.exists(path)) {
            try {
                // Create 'src/test/resources/extent-report-properties/' folder
                Files.createDirectories(Paths.get(Constants.EXTENT_PROPERTIES_DESTINATION));
                // Create 'src/test/resources/extent-report-properties/extent.properties' file
                final File extentConfigFile = new File(Constants.EXTENT_CONFIG_FILE_PATH);
                copyConfigFile();
            } catch (IOException io) {
                // Do nothing
            }
        } else {
            SLF4J_LOGGER.info("'extent-config.xml' file already exists");
        }
    }

    private static void copyConfigFile() {
        final String msg =
                String.format("Copying source config file '%s' to destination '%s'...",
                        Constants.EXTENT_CONFIG_SOURCE_FILE_PATH, Constants.EXTENT_CONFIG_FILE_PATH);
        SLF4J_LOGGER.info(msg);
        JAVA_LOGGER.log(Level.INFO, msg);

        try {
            in = new FileInputStream(Constants.EXTENT_CONFIG_SOURCE_FILE_PATH);
            out = new FileOutputStream(Constants.EXTENT_CONFIG_FILE_PATH);
            int n;
            while ((n = in.read()) != -1) {
                out.write(n);
            }
            if (in != null) {
                in.close();
            }
            if(out != null) {
                out.close();
            }
            SLF4J_LOGGER.info("File copied");
            JAVA_LOGGER.log(Level.INFO, "File copied");
        } catch (NullPointerException nullPointerException) {
            SLF4J_LOGGER.error("Could not copy 'extent-config.xml' file. NullPointerException caught");
            JAVA_LOGGER.log(Level.SEVERE, "Could not copy 'extent-config.xml' file. NullPointerException caught");
            throw new NullPointerException();
        } catch (IOException fileNotFoundException) {
            SLF4J_LOGGER.error(fileNotFoundException.getMessage());
            JAVA_LOGGER.log(Level.SEVERE, fileNotFoundException.getMessage());
        }
    }
}
