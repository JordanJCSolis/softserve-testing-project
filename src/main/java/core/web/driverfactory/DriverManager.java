package core.web.driverfactory;

import org.openqa.selenium.WebDriver;

public abstract class DriverManager {

    protected static WebDriver driver;

    protected abstract void createDriver();
    public WebDriver getWebDriver() {
        if (driver == null) {
            createDriver();
        }
        return driver;
    }

    public void quitDriver() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
