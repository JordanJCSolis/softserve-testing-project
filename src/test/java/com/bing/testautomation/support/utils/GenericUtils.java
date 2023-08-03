package com.bing.testautomation.support.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GenericUtils {

    // Set sl4j logger
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericUtils.class);

    private WebDriver driver;
    private WebDriverWait wait;

    /*
     * Constructor
     */
    public GenericUtils(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Constants.WEB_DRIVER_WAIT_TIME);
    }

    public void hardPause(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException interruptedException) {
            LOGGER.error(interruptedException.getLocalizedMessage());
            throw new RuntimeException(interruptedException.getLocalizedMessage());
        }
    }

    public void openWebPage(String webPageURL) {
        this.driver.get(webPageURL);
    }

    public boolean waitUrlContains(String url) {
        try {
            this.wait.until(ExpectedConditions.urlContains(url));
            return true;
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(timeoutException.getRawMessage());
        }
    }

    public boolean waitUrlToBe(final String url) {
        try {
            this.wait.until(ExpectedConditions.urlToBe(url));
            return true;
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(timeoutException.getRawMessage());
        }
    }

    public boolean attributeToBe(final WebElement element, final String attributeName, final String attributeValue) {
        try {
            this.wait.until(ExpectedConditions.attributeToBe(element, attributeName, attributeValue));
            return true;
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(timeoutException.getRawMessage());
        }
    }

    public boolean isElementVisible(WebElement element) {
        try {
            this.wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (NoSuchElementException noSuchElementException) {
            LOGGER.error(noSuchElementException.getLocalizedMessage());
            throw new NoSuchElementException(noSuchElementException.getLocalizedMessage());
        } catch (TimeoutException timeoutException) {
            LOGGER.error(timeoutException.getLocalizedMessage());
            throw new TimeoutException(timeoutException.getLocalizedMessage());
        }
    }

    public boolean isElementVisible(List<WebElement> elements) {
        try {
            this.wait.until(ExpectedConditions.visibilityOfAllElements(elements));
            return true;
        } catch (NoSuchElementException noSuchElementException) {
            this.LOGGER.error(noSuchElementException.getLocalizedMessage());
            throw new NoSuchElementException(noSuchElementException.getLocalizedMessage());
        } catch (TimeoutException timeoutException) {
            this.LOGGER.error(timeoutException.getLocalizedMessage());
            throw new TimeoutException(timeoutException.getLocalizedMessage());
        }
    }

    public boolean isElementNotVisible(WebElement element) {
        try {
            this.wait.until(ExpectedConditions.invisibilityOf(element));
            return true;
        } catch (NoSuchElementException noSuchElementException) {
            LOGGER.error(noSuchElementException.getLocalizedMessage());
            throw new NoSuchElementException(noSuchElementException.getLocalizedMessage());
        } catch (TimeoutException timeoutException) {
            LOGGER.error(timeoutException.getLocalizedMessage());
            throw new TimeoutException(timeoutException.getLocalizedMessage());
        }
    }

    public boolean isElementClickable(WebElement element) {
        try {
            this.wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (NoSuchElementException noSuchElementException) {
            throw new NoSuchElementException(noSuchElementException.getLocalizedMessage());
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(timeoutException.getRawMessage());
        } catch (ElementClickInterceptedException elementClickInterceptedException) {
            throw new ElementClickInterceptedException(elementClickInterceptedException.getLocalizedMessage());
        }
    }

    public boolean isElementFullyLoaded(WebElement element) {
        By elementXpath = By.xpath(getElementXpath(element));
        return isElementFullyLoaded(elementXpath);
    }

    public boolean isElementFullyLoaded(By elementLocator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
            return true;
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(timeoutException.getRawMessage());
        }
    }

    public boolean isElementVisibleAndPresentInDom(WebElement element) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(getElementXpath(element))));
            return true;
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(timeoutException.getRawMessage());
        }
    }

    public boolean isElementVisibleAndPresentInDom(List<WebElement> elements) {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(getElementXpath(elements.get(0)))));
            return true;
        } catch (TimeoutException timeoutException) {
            throw new TimeoutException(timeoutException.getRawMessage());
        }
    }

    public void typeOnInputTextField(WebElement element, String text) {
        element.click();
        // element.clear();
        element.sendKeys(Keys.CONTROL + "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
    }

    public String getElementXpath(List<WebElement> listElements) {
        return getElementXpath(listElements.get(0));
    }

    public String getElementXpath(WebElement element) {
        final String XPATH_KEYWORD = "xpath:";
        return element.toString().
                substring(element.toString().lastIndexOf(XPATH_KEYWORD) + XPATH_KEYWORD.length()
                        ,element.toString().length() - 1)
                .trim();
    }
    public void hoverMouseToElement (WebElement element){
        // create an Actions object
        Actions action = new Actions(this.driver);
        // perform the hover action on the element
        action.moveToElement(element).perform();
    }
}
