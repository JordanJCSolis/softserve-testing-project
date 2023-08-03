package core.extentreport;

import core.utils.Constants;
import core.utils.Params;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExtentReportPath {
    /*
     * Private constructor
     */
    private ExtentReportPath() {
        // No info
    }

    /*
     * Builds extent report generation path.
     */
    public static String setExtentReportPath() {
        return Constants.REPORT_BASE_PATH +
               featureReportName().concat("/") +
               featureReportName().concat(".html");
    }

    public static String featureReportName() {
        return Constants.CONFIG_FILE.concat("-")
                .concat(Constants.ENV).concat("-").concat("feature-test-report")
                .concat("-").concat(Params.executionTime);
    }

    public static String timeStamp() {
        return new SimpleDateFormat(Constants.TIME_STAMP_FORMAT,
                Locale.getDefault(Locale.Category.FORMAT)).format(new Date());
    }
}
