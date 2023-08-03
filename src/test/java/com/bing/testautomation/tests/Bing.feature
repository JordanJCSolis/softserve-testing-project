@bing
Feature: Bing

  Scenario: User searches for Weather in Lviv
    Given an entity
    And user access bing page
    When user searches for "weather in Lviv" using the search box
    Then user should see information related to their entry

  Scenario: User clicks on Bing Chat button
    Given an entity
    And user access bing page
    When user clicks on "chat" from the header options
    Then user should be redirected to expected scope page

  Scenario: User clicks on More Settings
    Given an entity
    And user access bing page
    When user expands the Settings and Quick Links menu
    And user clicks on "settings" option
    And user clicks on "more" submenu
    Then user should be redirected to settings page