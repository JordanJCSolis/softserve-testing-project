package core.web.driverfactory.driverManager;

import core.utils.Constants;
import core.web.driverfactory.DriverManager;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxDriverManager extends DriverManager {

    private final String driverProperty = "webdriver.gecko.driver";
    @Override
    protected void createDriver() {
        if (Constants.OS.toLowerCase().contains(Constants.MAC_OS)) {
            System.setProperty(driverProperty, Constants.DRIVER_PATH + "geckodriver");
        } else {
            System.setProperty(driverProperty, Constants.DRIVER_PATH + "geckodriver.exe");
        }

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");

        driver = new FirefoxDriver(firefoxOptions);
    }
}
