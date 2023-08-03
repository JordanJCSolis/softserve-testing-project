package core.extentreport.extentfiles;

import core.utils.Constants;
import core.utils.Params;
import io.cucumber.java.zh_cn.那么;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Level;

public class ExtentPropertiesFile {

    // Set java and slf4j logger
    private static final Logger SLF4J_LOGGER = LoggerFactory.getLogger(ExtentPropertiesFile.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(ExtentPropertiesFile.class.getName());

    /*
     * private constructor
     */
    private ExtentPropertiesFile() {
        // No data
    }

    /*
     * Methods
     */
    public static void createExtentPropertiesFile() {

        final String MSG = String.format("Creating extent.properties file in %s", Constants.EXTENT_PROPERTIES_DESTINATION);
        SLF4J_LOGGER.info(MSG);
        JAVA_LOGGER.log(Level.INFO, MSG);

        try {
            // Create 'src/test/resources/extent-report-properties/' folder
            Files.createDirectories(Paths.get(Constants.EXTENT_PROPERTIES_DESTINATION));

            // Create 'src/test/resources/extent-report-properties/extent.properties' file
            OutputStream output = new FileOutputStream(
                    Constants.EXTENT_PROPERTIES_FILE_PATH);

            Properties extentReporter = new Properties();
            Properties screenshots = new Properties();
            Properties baseFolder = new Properties();

            switch (Constants.TEST_REPORT.toLowerCase()) {
                case "html":
                default:
                    // HTML extent reports
                    extentReporter.setProperty("extent.reporter.html.start", "true");
                    extentReporter.setProperty("extent.reporter.html.config", Constants.EXTENT_CONFIG_FILE_PATH);
                    extentReporter.setProperty("extent.reporter.html.out", Params.extentReportPath);
                    break;

                case "spark":
                    // Spark extent reports
                    extentReporter.setProperty("extent.reporter.spark.start", "true");
                    extentReporter.setProperty("extent.reporter.spark.config", Constants.EXTENT_CONFIG_FILE_PATH);
                    extentReporter.setProperty("extent.reporter.spark.out", Params.extentReportPath);
                    break;

                case "avent":
                    // Avent extent report
                    extentReporter.setProperty("extent.reporter.avent.start", "true");
                    extentReporter.setProperty("extent.reporter.avent.config", Constants.EXTENT_CONFIG_FILE_PATH);
                    extentReporter.setProperty("extent.reporter.avent.out", Params.extentReportPath);
                    break;
            }
            extentReporter.store(output, " Extent report configuration");

            // Global properties
            // Screenshot properties
//            screenshots.setProperty("screenshot.dir", "target/screenshots/");
//            screenshots.setProperty("screenshot.rel.path", "target/screenshots/");
            screenshots.store(output, " Screenshots configuration");

            // Base folder properties
            baseFolder.setProperty("basefolder.name", Constants.REPORT_BASE_PATH);
            baseFolder.setProperty("basefolder.datetimepattern",Constants.TIME_STAMP_FORMAT);
            baseFolder.store(output, " Base folder configurations");
        } catch(IOException io) {
            String ERROR_MSG = "Could not create 'extent.properties' file. ";
            SLF4J_LOGGER.error(ERROR_MSG + io.getMessage());
            JAVA_LOGGER.log(Level.SEVERE, ERROR_MSG + io.getMessage());
        }
    }

    public static void deleteExtentPropertiesFile(String extentPropertiesFilePath) {
        File myFile = new File(extentPropertiesFilePath);
        myFile.delete();
    }
}
