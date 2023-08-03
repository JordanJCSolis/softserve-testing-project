package com.bing.testautomation.steps;

import com.bing.testautomation.support.pages.implementation.SearchResultsPage;
import com.bing.testautomation.support.utils.testcontext.Context;
import com.bing.testautomation.support.utils.testcontext.TestContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.testng.Assert;

public class SearchResultsPageSteps {

    SearchResultsPage searchResultsPage = new SearchResultsPage();

    @And("^user should be redirected to expected scope page$")
    public void user_should_be_redirected_to_expected_scope_page() {
        String scope = TestContext.scenarioContext.getContext(Context.SCOPE).toString();
        Assert.assertTrue(searchResultsPage.isUserRedirectToExpectedScopePage(scope), "");
    }

    @Then("^user should see information related to their entry$")
    public void user_should_see_information_related_to_the_weather_in_lviv() throws Throwable {
        String search = TestContext.scenarioContext.getContext(Context.SEARCH).toString();
        Assert.assertTrue(searchResultsPage.isUserRedirectedToSearchResults(search));
    }

}
