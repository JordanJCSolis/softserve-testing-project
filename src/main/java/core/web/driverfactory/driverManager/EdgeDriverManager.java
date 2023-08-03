package core.web.driverfactory.driverManager;

import core.utils.Constants;
import core.web.driverfactory.DriverManager;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeDriverManager extends DriverManager {
    private final String driverProperty = "webdriver.edge.driver";

    @Override
    protected void createDriver() {
        if (Constants.OS.toLowerCase().contains(Constants.MAC_OS)) {
            System.setProperty(driverProperty, Constants.DRIVER_PATH + "msedgedriver");
        } else {
            System.setProperty(driverProperty, Constants.DRIVER_PATH + "msedgedriver.exe");
        }

        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--remote-allow-origins=*");

        driver = new EdgeDriver(edgeOptions);
    }
}
