package runner;

import core.domainentity.config.ConfigProvider;
import core.extentreport.ExtentReportPath;
import core.utils.Constants;
import core.utils.Params;
import core.web.driverfactory.WebDriverProvider;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.util.Locale;
import java.util.logging.Level;

@CucumberOptions(
        features = {"src/test/java/com/bing/testautomation/tests/"},
        glue = {"com.bing.testautomation.steps",
                "com.bing.testautomation.support.utils"},
        snippets = CucumberOptions.SnippetType.UNDERSCORE,
        monochrome = true,
        dryRun = false,
        plugin = {
                "json:target/default-report/cucumber-report.json",
                "junit:target/default-report/cucumber-report.xml",
                "html:target/default-report/cucumber-report.html",
                "core.extentreport.listener.cucumberlistener.CucumberListener",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class TestRunner extends AbstractTestNGCucumberTests {

        private static final Logger SLF4J_LOGGER = LoggerFactory.getLogger(TestRunner.class);
        private static final java.util.logging.Logger JAVA_LOGGER = java.util.logging.Logger.getLogger(TestRunner.class.getName());

        @BeforeSuite
        public void verifySystemProperties() {

                Params.initialExecutionTime = ExtentReportPath.timeStamp();
                Params.executionTime = Params.initialExecutionTime;
                /*
                 * Params.finalExecutionTime = ExtentReportPath.timeStamp();
                 * should be executed in @afterSuite annotation to get the same time as the report.
                 */

                // Assert config.file system variable is not null or empty
                if (Constants.CONFIG_FILE == null || "".equals(Constants.CONFIG_FILE)) {
                        SLF4J_LOGGER.error("Test parameter 'config.file' cannot be null or empty");
                        JAVA_LOGGER.log(Level.SEVERE, "Test parameter 'config.file' cannot be null or empty");
                        throw new IllegalArgumentException();
                }

                // Assert if config file exists
                final String otherConfigFileName =
                        ConfigProvider.CONFIG_BASE_PATH + Constants.CONFIG_FILE.toLowerCase(Locale.ENGLISH)
                        + ConfigProvider.CONFIG_FILE_EXTENSION;
                final File otherConfigFile = new File(otherConfigFileName);
                if (!otherConfigFile.exists()) {
                        final String illegalMsg = String.format("Config file '%s.conf' was nof found in base path '%s'",
                                Constants.CONFIG_FILE, ConfigProvider.CONFIG_BASE_PATH);
                        SLF4J_LOGGER.error(illegalMsg);
                        JAVA_LOGGER.log(Level.SEVERE, illegalMsg);
                        throw new IllegalArgumentException(illegalMsg);
                }
                Params.extentReportPath = ExtentReportPath.setExtentReportPath();
        }

        @BeforeMethod
        public void initializeWebDriver() throws IllegalArgumentException {
                SLF4J_LOGGER.info("Starting driver");
                JAVA_LOGGER.log(Level.INFO, "Starting driver");
                WebDriver driver = WebDriverProvider.getDriver(Constants.BROWSER).getWebDriver();
                driver.manage().window().maximize();
        }

        @AfterMethod
        public void endWebDriverSession() {
                SLF4J_LOGGER.info("Ending driver session");
                JAVA_LOGGER.log(Level.INFO, "Ending driver session");
                WebDriverProvider.getDriver(Constants.BROWSER).quitDriver();
        }
}
