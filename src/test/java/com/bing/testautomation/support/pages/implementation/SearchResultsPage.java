package com.bing.testautomation.support.pages.implementation;

import com.bing.testautomation.support.pages.Page;
import com.bing.testautomation.support.utils.Constants;
import com.bing.testautomation.support.utils.GenericUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class SearchResultsPage extends Page {

    // Set slf4j logger
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchResultsPage.class);
    private WebDriver driver;
    private GenericUtils utils;

    /*
     * Constructor
     */

    public SearchResultsPage() {
        this.driver = super.getWebDriver();
        this.utils = super.utils;
        PageFactory.initElements(this.driver, this);
    }

    /*
     * Locators
     */

    // scope bar
    @FindBy(xpath = "//*[@class='b_scopebar']//li[contains(@class,'active')]")
    private WebElement currentActiveScope;

    // Chat page
    @FindBy(id = "searchbox")
    private WebElement startConversationBox;

    /*
     * Methods
     */

    public boolean isUserRedirectToExpectedScopePage(final String scope) {
        utils.waitUrlContains("/search");

        String attributeValue = "b-scopeListItem-";
        switch (scope.toLowerCase().trim()) {
            case "chat":
                attributeValue = attributeValue.concat("conv");
                break;
            case "maps":
                attributeValue = attributeValue.concat("local");
                break;
            default:
                attributeValue = attributeValue.concat(scope.toLowerCase().trim());
                break;
        }
        utils.attributeToBe(currentActiveScope, "id", attributeValue);
        if (!currentActiveScope.getText().trim().equalsIgnoreCase(scope.trim())) {
            return false;
        }
        return true;
    }

    public boolean isUserRedirectedToSearchResults(String search) {
        utils.waitUrlContains("/search");
        utils.waitUrlContains(search.replaceAll("[\\s]","+"));
        return true;
    }
}
