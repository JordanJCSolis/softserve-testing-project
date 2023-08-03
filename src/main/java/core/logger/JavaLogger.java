package core.logger;

import core.extentreport.ExtentReportPath;
import core.utils.Constants;
import core.utils.Params;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.*;
import java.util.logging.Logger;

public class JavaLogger extends Formatter {

    static {
        // Set the format for java.util.logging
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "[%1$tb %1$td, %1$tY - %1$tl:%1$tM:%1$tS %1$Tp] %2$s%n%4$s: %5$s%n");
    }

    public static void initiateLoggingFile() {
        FileHandler fileHandler;

        try {
            // Create 'src/test/resources/reports/REPORT_NAME' folder
            Files.createDirectories(Paths.get(Constants.REPORT_BASE_PATH +
                                              ExtentReportPath.featureReportName().concat("/")));

            // Creates log file under 'src/test/resources/reports/REPORT_NAME_FOLDER/'
            Params.logFilePath = Constants.REPORT_BASE_PATH +
                                 ExtentReportPath.featureReportName().concat("/") +
                                 ExtentReportPath.featureReportName().concat(".log");

            // Set fileHandler to write logging records into 'log file'
            fileHandler = new FileHandler(Params.logFilePath);
            // Set formatter for file Handler
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            // Add the fileHandler as the logger handler
            Logger.getLogger("").addHandler(fileHandler);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String format(LogRecord record) {
        return null;
    }
}
