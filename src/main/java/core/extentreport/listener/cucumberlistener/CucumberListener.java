package core.extentreport.listener.cucumberlistener;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import core.logger.JavaLogger;
import core.logger.Logger;
import core.extentreport.ExtentReportManager;
import core.extentreport.ExtentReportPath;
import core.extentreport.ExtentTestManager;
import core.extentreport.ScreenShotProvider;
import core.extentreport.extentfiles.ExtentConfigFile;
import core.extentreport.extentfiles.ExtentPropertiesFile;
import core.utils.Constants;
import core.utils.Params;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class CucumberListener implements ConcurrentEventListener {

    // Set java slf4j logger
    private static final org.slf4j.Logger SLF4J_LOGGER = LoggerFactory.getLogger(CucumberListener.class);
    private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(CucumberListener.class.getName());

    /*
     * Cucumber feature variables
     */
    private static List<String> featuresList = new ArrayList<>();
    private static String cucumberFeatureName;
    private static String cucumberScenarioName;
    private static String cucumberKeyword;
    private static String cucumberStep;
    private static String completeCucumberStep;
    private static String cucumberStepStatus;
    private static String cucumberStepError;
    private static Duration cucumberStepDuration;

    /*
     * Global KPIs
     */
    private static List<String> cucumberTagsList = new ArrayList<>();
    private static Multimap<String, List<String>> scenariosAndStepsMap = ArrayListMultimap.create();
    private static String totalExecutionDuration;

    /*
     *
     */
    private static final String noScenarioName = "'ERROR: Test scenario name was not defined'";

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // Handle TestRun
        publisher.registerHandlerFor(TestRunStarted.class, this::handleRunStarted);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleRunFinished);
        // handle TestCase
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        // Handle TestStep
        publisher.registerHandlerFor(TestStepStarted.class, this::handleTestStepStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
    }

    /*
     * Handle Cucumber run started
     *
     * @param: TestRunStarted - Cucumber event listener that is run when
     * cucumber execution starts
     */
    private void handleRunStarted(TestRunStarted testRunStarted) {
        // Initiate logger
        JavaLogger.initiateLoggingFile();
        Logger.initateLog4JLogger();
        Params.extentReports = ExtentReportManager.createExtentReports();
    }

    /*
     * Handle Cucumber run finished
     *
     * @param: TestRunFinished - Cucumber event listener that is run when
     * cucumber execution finishes
     */
    @SneakyThrows
    private void handleRunFinished(TestRunFinished testRunFinished) {
        ExtentConfigFile.setupExtentReportConfig();
        ExtentPropertiesFile.createExtentPropertiesFile();
        Params.sparkReporter.loadXMLConfig(Constants.EXTENT_CONFIG_FILE_PATH);

        createLogSectionInExtentReport();

        Params.extentReports.flush();

        Params.finalExecutionTime = ExtentReportPath.timeStamp();

        totalExecutionDuration = DurationFormatUtils.formatDuration(
                testRunFinished.getResult().getDuration().toMillis(), "HH:mm:ss.SSS-Z");
    }

    private void createLogSectionInExtentReport() throws FileNotFoundException {
        final String LOG_FILE_REPORT_NAME = "Log file";

        // Read log file
        FileReader fileReader = new FileReader(Params.logFilePath);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // Create Log feature
        Params.extentTestFeature = CucumberListenerReportCreator.createFeatureReportInformation("Feature",
                LOG_FILE_REPORT_NAME);
        // Assign category to feature
        Params.extentTestFeature.assignCategory(LOG_FILE_REPORT_NAME);
        // Create test scenario to hold log
        Params.extentTestScenario = CucumberListenerReportCreator.createScenarioReportInformation("Scenario", "FILE ATTACHED");

        // Variable used to hold the date and time line
        String dateTimeMessage;
        // Variable used to hold the message printed
        String message;
        // Variable used to hold the next line after message
        String aux;
        // Variable of type 'com.aventstack.extentreports.Status' used to hold the
        // message status level.
        com.aventstack.extentreports.Status loggingLeveL = null;
        // Pattern contained in the date and time line
        final String dateTimePattern = "\\[(\\D{3}\\s\\d{1,2},\\s\\d{4})\\s-\\s(\\d{1,2}:\\d{2}:\\d{2}\\s\\D{2})\\].*";

        try {
            while ((dateTimeMessage = bufferedReader.readLine()) != null) {

                message = bufferedReader.readLine();
                String messageLevel = message.substring(0, message.indexOf(":"));

                if (messageLevel.equals(Level.WARN)) {
                    loggingLeveL = com.aventstack.extentreports.Status.WARNING;
                } else if (messageLevel.equals(Level.ERROR)) {
                    loggingLeveL = com.aventstack.extentreports.Status.FAIL;
                } else {
                    loggingLeveL = com.aventstack.extentreports.Status.INFO;
                }

                // Set a mark before reading next like so you can rollback
                bufferedReader.mark(1000);

                try {
                    aux = bufferedReader.readLine();
                    // if next line does not have the date-time pattern, then is part of the message
                    if (!aux.matches(dateTimePattern)) {
                        do {
                            message = message + "\n" + aux;
                            bufferedReader.mark(1000);
                        } while (!(aux = bufferedReader.readLine()).matches(dateTimePattern));

                        bufferedReader.reset();
                    } else {
                        bufferedReader.reset();
                    }

                } catch (NullPointerException nullPointerException) {
                    // Do nothing
                }

                // Add log message to extent report
                Params.extentTestScenario.createNode(dateTimeMessage).log(loggingLeveL, message);

            }

            // Close buffer reader
            bufferedReader.close();

        } catch (Exception exception) {
            // If there is an error while creatin 'Log file section', get the message, print in in the log file and stop creating section.
            String errorCause = exception.fillInStackTrace().toString();
            Params.extentTestScenario.createNode(
                            new SimpleDateFormat(Constants.TIME_STAMP_FORMAT).format(Calendar.getInstance().getTime()))
                    .log(com.aventstack.extentreports.Status.FAIL, "Error while generating Log file. " + errorCause);
        }

        // Force 'Log file section' to display PASS status
        Params.extentTestFeature.log(com.aventstack.extentreports.Status.PASS, "");
    }

    /*
     * Handle Cucumber test case start.
     *
     * @param: TestCaseStarted - Cucumber event listener that is run when
     * cucumber test/scenario/scenario outline starts
     */
    private void handleTestCaseStarted(TestCaseStarted testCaseStarted) {
        cucumberFeatureName = testCaseStarted.getTestCase().getUri().toString()
                .substring(testCaseStarted.getTestCase().getUri().toString().lastIndexOf("/") + 1);

        cucumberScenarioName = ((testCaseStarted.getTestCase().getName() == null)
                                || (testCaseStarted.getTestCase().getName().isBlank()))
                ? noScenarioName
                : testCaseStarted.getTestCase().getName();

        cucumberKeyword = testCaseStarted.getTestCase().getKeyword().trim();

        // Create feature test
        if (!featuresList.contains(cucumberFeatureName)) {
            featuresList.add(cucumberFeatureName);
            Params.extentTestFeature = CucumberListenerReportCreator.createFeatureReportInformation("Feature",
                    cucumberFeatureName);

            SLF4J_LOGGER.info("Starting scenarios in " + cucumberFeatureName);
            JAVA_LOGGER.log(java.util.logging.Level.INFO, "Starting scenarios in " + cucumberFeatureName);
        }

        // Get list of tags in current test case.
        List<String> tags = testCaseStarted.getTestCase().getTags();
        cucumberTagsList.addAll(tags);

        // Add tags
        for (String tag : testCaseStarted.getTestCase().getTags()) {
            ExtentTestManager.assignCategory(Params.extentTestFeature, tag);
        }

        // Create Scenario node
        SLF4J_LOGGER.info("Starting scenario: " + cucumberScenarioName);
        JAVA_LOGGER.log(java.util.logging.Level.INFO, "Starting scenario: " + cucumberScenarioName);

        Params.extentTestScenario = CucumberListenerReportCreator.createScenarioReportInformation("Scenario",
                cucumberScenarioName);

        // Fail scenario if it has not a name defined. Cucumber will not be executed and
        // will throw an error.
        if (((testCaseStarted.getTestCase().getName() == null))
            || ((testCaseStarted.getTestCase().getName().isBlank()))) {

            Params.extentTestScenario.fail(cucumberKeyword + " does not have a name. ");

            //
            List<PickleStepTestStep> testSteps = testCaseStarted.getTestCase().getTestSteps().stream()
                    .filter(x -> x instanceof PickleStepTestStep)
                    .map(x -> (PickleStepTestStep) x).collect(Collectors.toList());

            // Get scenario step
            for (PickleStepTestStep steps : testSteps) {
                cucumberKeyword = steps.getStep().getKeyword().trim();
                cucumberStep = steps.getStep().getText().trim();

                Params.extentTestScenario = CucumberListenerReportCreator.createStepReportInformation(cucumberKeyword,
                        cucumberStep);
                Params.extentTestScenario.log(com.aventstack.extentreports.Status.SKIP, "");
            }
        }
    }

    /*
     * Handle Cucumber test case finish.
     *
     * @param: TestCaseFinished - Cucumber event listener that is run when
     * cucumber test/scenario/scenario outline finishes
     */
    private void handleTestCaseFinished(TestCaseFinished testCaseFinished) {
        SLF4J_LOGGER.info("End scenario: " + testCaseFinished.getTestCase().getName());
        JAVA_LOGGER.log(java.util.logging.Level.INFO, "End scenario: " + testCaseFinished.getTestCase().getName());
    }

    /*
     *
     */
    private void handleTestStepStarted(TestStepStarted testStepStarted) {
        if (testStepStarted.getTestStep() instanceof PickleStepTestStep) {
            PickleStepTestStep pickleStepTestStep = (PickleStepTestStep) testStepStarted.getTestStep();
            cucumberKeyword = pickleStepTestStep.getStep().getKeyword().trim();
            cucumberStep = pickleStepTestStep.getStep().getText().trim();

            // Create scenario step on extent report.
            Params.extentTestScenario = CucumberListenerReportCreator
                    .createStepReportInformation(cucumberKeyword, cucumberStep);
            SLF4J_LOGGER.info("Start test step: " + cucumberKeyword + " " + cucumberStep);
            JAVA_LOGGER.log(java.util.logging.Level.INFO, "Start test step: " + cucumberKeyword + " " + cucumberStep);
        }
    }

    /*
     *
     */
    private void handleTestStepFinished(TestStepFinished testStepFinished) {
        if (testStepFinished.getTestStep() instanceof PickleStepTestStep) {
            cucumberStepStatus = testStepFinished.getResult().getStatus().toString().trim();

            // Assert cucumberStepError does not throw NullPointerException
            try {
                cucumberStepError = testStepFinished.getResult().getError().toString();
            } catch (NullPointerException nullPointerException) {
                // Continue
            }
            // Duration
            cucumberStepDuration = testStepFinished.getResult().getDuration();

            // Set test result for test step.
            switch (testStepFinished.getResult().getStatus()) {
                case PASSED:
                    Params.extentTestScenario.log(com.aventstack.extentreports.Status.PASS, "");
                    break;
                case FAILED:
                    Params.extentTestScenario.log(com.aventstack.extentreports.Status.FAIL, MarkupHelper.createCodeBlock(cucumberStepError));
                    ScreenShotProvider.takeScreenshot();
                    break;
                case SKIPPED:
                    Params.extentTestScenario.log(Status.SKIP, "");
                    break;
            }
            SLF4J_LOGGER.info("End Test Step: Test " + testStepFinished.getResult().getStatus().toString());
            JAVA_LOGGER.log(java.util.logging.Level.INFO, "End Test Step: Test " + testStepFinished.getResult().getStatus().toString());
            scenariosAndStepsMap.put(cucumberScenarioName, Arrays.asList(completeCucumberStep, cucumberStepStatus));
        }
    }

    /*
     * Getters and setters
     */
    public static String getCucumberFeatureName() {
        return cucumberFeatureName;
    }

    public static String getCucumberScenarioName() {
        return cucumberScenarioName;
    }

    public static String getCucumberKeyword() {
        return cucumberKeyword;
    }

    public static String getCucumberStep() {
        return cucumberStep;
    }

    public static String getCucumberStepStatus() {
        return cucumberStepStatus;
    }

    public static String getTotalExecutionDuration() {
        return totalExecutionDuration;
    }

    public static List<String> getFeaturesList() {
        return featuresList;
    }

    public static Multimap<String, List<String>> getScenariosAndStepsMap() {
        return scenariosAndStepsMap;
    }
}