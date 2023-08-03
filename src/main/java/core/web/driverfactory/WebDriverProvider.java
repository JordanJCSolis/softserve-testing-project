package core.web.driverfactory;

import core.web.driverfactory.driverManager.ChromeDriverManager;
import core.web.driverfactory.driverManager.EdgeDriverManager;
import core.web.driverfactory.driverManager.FirefoxDriverManager;

public class WebDriverProvider {

    private static DriverManager driverManager = null;

    /*
     * Private constructor
     */
    private WebDriverProvider() {
    }

    /*
     * Factory pattern to get the web driver as per the browser on which
     * Web Application Under Test (WAUT) is to be loaded
     *
     * @param browser : the browser name
     *
     * @return : the web driver which represents an idealised web browser
     */
    public static DriverManager getDriver (String browser) {

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverProvider.driverManager = new ChromeDriverManager();
                break;
            case "edge":
                WebDriverProvider.driverManager = new EdgeDriverManager();
                break;
            case "firefox":
                WebDriverProvider.driverManager = new FirefoxDriverManager();
                break;
            case "safari":
                break;
            default:
                WebDriverProvider.driverManager = new ChromeDriverManager();
                break;
        }
        return driverManager;
    }
}
