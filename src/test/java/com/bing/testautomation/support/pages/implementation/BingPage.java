package com.bing.testautomation.support.pages.implementation;

import com.bing.testautomation.support.pages.Page;
import com.bing.testautomation.support.utils.GenericUtils;
import core.utils.TestContextProvider;
import org.jsoup.Jsoup;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;

import java.util.List;
import java.util.stream.Collectors;

public class BingPage extends Page {

    private WebDriver driver;
    private GenericUtils utils;

    /*
     * Constructor
     *
     * Initialize elements of the Page Factory Pattern
     */
    public BingPage() {
        this.driver = super.getWebDriver();
        this.utils = super.utils;
        PageFactory.initElements(this.driver, this);
    }

    /*
     * Locators
     */

    // Header elements
    @FindBy(xpath = "//*[@id='hdr']//*[@class='scopes ']/li")
    private List<WebElement> headerScopes;

    // Id
    @FindBy(xpath = "//*[@id='id_h']//*[@id='id_sc']")
    private WebElement settingsAndQuickLinksCollapsedButton;

    @FindAll({
            @FindBy(xpath = "//*[@id='HBContent']/*[contains(@class,'hb')]/div[@class='hb_titlerow']/div[@class='hb_title_col']"),
            @FindBy(xpath = "//*[@id='HBContent']/*[@class='hb_sect_container']/div[contains(@class,'hb')]/div[@class='hb_titlerow']/div[@class='hb_title_col']"),
            @FindBy(xpath = "//*[@id='HBContent']//div[@data-bm='51']/*[@class='hb_section']/div[@class='hb_titlerow']/div[@class='hb_title_col']"),
            @FindBy(xpath = "//*[@id='HBContent']//div[@data-bm='51']/*[contains(@class,'hb_sect_container')]/div[@class='hb_section']/div[@class='hb_titlerow']/div[@class='hb_title_col']")
    })
    private List<WebElement> settingsAndQuickLinksOptions;

    @FindBy(xpath = "//*[@id='HBContent']//*[@class='hb_sect_container']/div[contains(@class,'hb_expanded')]//following-sibling::div[@role='menu']//*[@class='hb_titlerow']")
    private List<WebElement> activeSubMenuOptions;

    // Search box elements
    @FindBy(id = "sb_form_q")
    private WebElement searchBox;

    @FindBy(id = "search_icon")
    private WebElement searchIcon;

    // Settings page
    @FindBy(css = "#profileSet .me_section_title")
    private List<WebElement> profileSections;

    /*
     * Methods
     */
    public boolean userAccessBingPage() {
        driver.get(TestContextProvider.getElementDefinedInTestContext("baseUrl"));
        utils.isElementVisible(searchBox);
        return true;
    }

    public boolean userClicksOnHeaderScope(final String scope) {
        utils.isElementVisible(headerScopes);
        for (WebElement element : headerScopes) {
            if (element.getText().trim().equalsIgnoreCase(scope.trim())) {
                utils.isElementClickable(element);
                element.click();
                break;
            }
        }
        return true;
    }

    public boolean userTypesOnSearchBox(final String entry) {
        utils.isElementVisible(searchBox);
        utils.typeOnInputTextField(searchBox, entry);
        Actions actions = new Actions(driver);
        actions.keyDown(Keys.ENTER).build().perform();
        return true;
    }

    public boolean userExpandsSettingsAndQuickLinksMenu() {
        utils.isElementClickable(settingsAndQuickLinksCollapsedButton);
        settingsAndQuickLinksCollapsedButton.click();
        return true;
    }

    public boolean userClicksOnSetting(final String setting) {
        utils.isElementVisible(settingsAndQuickLinksOptions);
        for (WebElement element: settingsAndQuickLinksOptions) {
            if (element.getText().trim().equalsIgnoreCase(setting.trim())) {
                element.click();
                return true;
            }
        }
        return false;
    }

    public boolean userSelectsSubMenuOption(final String subMenu) {
        utils.isElementVisible(activeSubMenuOptions);
        for (WebElement element: activeSubMenuOptions) {
            if (element.getText().trim().equalsIgnoreCase(subMenu.trim())) {
                utils.isElementClickable(element);
                element.click();
                return true;
            }
        }
        return false;
    }

    public boolean isUserRedirectedToSettingsPage() {
        utils.waitUrlContains("account/general");
        utils.isElementVisible(profileSections);
        return true;
    }
}
