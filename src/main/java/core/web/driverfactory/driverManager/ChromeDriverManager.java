package core.web.driverfactory.driverManager;

import core.utils.Constants;
import core.web.driverfactory.DriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverManager extends DriverManager {

    private final String driverProperty = "webdriver.chrome.driver";

    @Override
    protected void createDriver() {
        if (Constants.OS.toLowerCase().contains(Constants.MAC_OS)) {
            System.setProperty(driverProperty, Constants.DRIVER_PATH + "chromedriver");
        } else {
            System.setProperty(driverProperty, Constants.DRIVER_PATH + "chromedriver.exe");
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }
}
