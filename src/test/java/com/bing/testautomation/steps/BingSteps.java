package com.bing.testautomation.steps;

import com.beust.ah.A;
import com.bing.testautomation.support.pages.implementation.BingPage;
import com.bing.testautomation.support.utils.testcontext.Context;
import com.bing.testautomation.support.utils.testcontext.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en_scouse.An;
import org.testng.Assert;

public class BingSteps {

    BingPage bingPage = new BingPage();

    @And("^user access bing page$")
    public void user_access_bing_page() throws Throwable {
        Assert.assertTrue(bingPage.userAccessBingPage(),
                "Could not access Bing page");
    }

    @When("^user searches for \"([^\"]*)\" using the search box$")
    public void user_searches_entry_using_the_search_box(final String entry) throws Throwable {
        Assert.assertTrue(bingPage.userTypesOnSearchBox(entry),
                "");
        TestContext.scenarioContext.setContext(Context.SEARCH, entry);
    }

    @When("^user clicks on \"([^\"]*)\" from the header options$")
    public void user_clicks_on_scope_from_the_header_options(final String scope) throws Throwable {
        Assert.assertTrue(bingPage.userClicksOnHeaderScope(scope),
                "");
        TestContext.scenarioContext.setContext(Context.SCOPE, scope);
    }

    @When("^user expands the Settings and Quick Links menu$")
    public void user_expands_the_settings_and_quick_links_menu() throws Throwable {
        Assert.assertTrue(bingPage.userExpandsSettingsAndQuickLinksMenu(),
                "Could not expand Settings and Quick Links menu");
    }

    @An("^user clicks on \"([^\"]*)\" option$")
    public void user_clicks_on_settings(final String settingsOption) throws Throwable {
        Assert.assertTrue(bingPage.userClicksOnSetting(settingsOption),
                "");
    }

    @And("^user clicks on \"([^\"]*)\" submenu$")
    public void user_clicks_on_more_option(final String submenuOption) throws Throwable {
        Assert.assertTrue(bingPage.userSelectsSubMenuOption(submenuOption),
                "");
    }

    @Then("^user should be redirected to settings page$")
    public void user_should_be_redirected_to_page() throws Throwable {
        Assert.assertTrue(bingPage.isUserRedirectedToSettingsPage(),
                "");
    }

}
