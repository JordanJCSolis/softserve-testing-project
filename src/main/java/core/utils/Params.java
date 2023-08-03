package core.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.vimalselvam.cucumber.listener.ExtentProperties;

public class Params {

    /*
     * Extent report parameters
     */
    public static ExtentReports extentReports;
    public static ExtentProperties extentProperties;
    //    public static ExtentHtmlReporter extentHtmlReporter;
    public static ExtentSparkReporter sparkReporter;
    public static ExtentTest extentTestFeature;
    public static ExtentTest extentTestScenario;

    /*
     * Run execution parameters
     */
    public static String initialExecutionTime;
    public static String finalExecutionTime;
    public static String executionTime;
    public static String extentReportPath;
    public static String logFilePath;

}
