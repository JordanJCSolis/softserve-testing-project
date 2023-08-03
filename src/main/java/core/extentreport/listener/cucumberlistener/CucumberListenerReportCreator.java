package core.extentreport.listener.cucumberlistener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.gherkin.model.*;
import core.utils.Params;
import org.testng.ITestListener;

public class CucumberListenerReportCreator implements ITestListener {

    private static ExtentReports extentReport = Params.extentReports;
    private static ExtentTest feature = Params.extentTestFeature;
    private static ExtentTest scenario = Params.extentTestScenario;

    /*
     + private constructor
     */
    private CucumberListenerReportCreator() {
        // No data
    }

    /*
     *
     */
    public static ExtentTest createFeatureReportInformation(String cucumberKeyword, String cucumberStep) {
        return feature = extentReport.createTest(gherkinClass(cucumberKeyword), cucumberStep);
    }

    public static ExtentTest createScenarioReportInformation(String cucumberKeyword, String cucumberStep) {
        return scenario = feature.createNode(gherkinClass(cucumberKeyword), cucumberKeyword + ": " + cucumberStep);
    }

    public static ExtentTest createStepReportInformation(String cucumberKeyword, String cucumberStep) {
        return scenario.createNode(gherkinClass(cucumberKeyword), cucumberKeyword + " " + cucumberStep);
    }

    /*
     *
     */
    private static Class<? extends IGherkinFormatterModel> gherkinClass(String cucumberKeyword) {
        switch (cucumberKeyword.toLowerCase()) {
            case "feature":
                return Feature.class;
            case "background":
                return Background.class;
            case "scenario":
                return Scenario.class;
            case "scenario outline":
                return ScenarioOutline.class;
            case "given":
                return Given.class;
            case "when":
                return When.class;
            case "then":
                return Then.class;
            case "and":
                return And.class;
            case "*":
                return Asterisk.class;
            case "but":
                return But.class;
            default:
                throw new RuntimeException();
        }
    }
}