package core.extentreport;

import core.web.driverfactory.WebDriverProvider;

import core.utils.Constants;
import core.utils.Params;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.*;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ExtentReportManager {
    private static final Capabilities capabilities =
            ((RemoteWebDriver) WebDriverProvider.getDriver(Constants.BROWSER).getWebDriver()).getCapabilities();

    public synchronized static ExtentReports createExtentReports() {
        final ExtentReports extentReports = new ExtentReports();

        // Spark reporter
        Params.sparkReporter = new ExtentSparkReporter(Params.extentReportPath);
        extentReports.attachReporter(Params.sparkReporter);

        // HTML reporter
//        Params.extentHtmlReporter = new ExtentHtmlReporter(ExtentReportPath.setupExtentReportPath());
//        extentReports.attachReporter(Params.extentHtmlReporter);

        // Set browser name and browser version
        Constants.BROWSER_NAME = capabilities.getBrowserName();
        Constants.BROWSER_VERSION = capabilities.getBrowserVersion();

        // Set system info
        extentReports.setSystemInfo("Author", Constants.USER_NAME);
        extentReports.setSystemInfo("OS", Constants.OS);
        extentReports.setSystemInfo("Env", Constants.ENV);
        extentReports.setSystemInfo("Browser", Constants.BROWSER_NAME);
        extentReports.setSystemInfo("Browser version", Constants.BROWSER_VERSION);

        return extentReports;
    }

    public static void createSystemInfoCondition(ExtentReports report, String key, String value) {
        report.setSystemInfo(key,value);
    }
}
