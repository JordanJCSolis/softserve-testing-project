package core.web.page;

import core.utils.Constants;
import core.web.driverfactory.WebDriverProvider;
import org.openqa.selenium.WebDriver;

public class AbstractPage {

    private WebDriver driver;

    public WebDriver getWebDriver() {
        return WebDriverProvider.getDriver(Constants.BROWSER).getWebDriver();
    }

    public void setWebDriver(final WebDriver driver) {
        this.driver = driver;
    }
}
