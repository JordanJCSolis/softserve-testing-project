package com.bing.testautomation.support.utils;

import core.extentreport.ScreenShotProvider;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.Status;

public class Hooks {

    @After(order = 1)
    public void takeScreenShot(Scenario scenario) {
        if (scenario.getStatus().equals(Status.PASSED)) {
            ScreenShotProvider.takeScreenshot();
        }
    }
}
