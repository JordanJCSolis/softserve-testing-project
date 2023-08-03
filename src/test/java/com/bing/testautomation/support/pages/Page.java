package com.bing.testautomation.support.pages;

import com.bing.testautomation.support.utils.GenericUtils;
import core.web.page.AbstractPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * Accepts driver instance from DI container. Parent page class that all pages
 * should inherit from. This class constructor takes a web-driver instance;
 * Make sure you pass it one when you call <code> super(driver); </code>
 */
public abstract class Page extends AbstractPage {

    // Set slf4j logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Page.class);
    // Define Generic Utils object
    protected GenericUtils utils;


    /*
     * Initialize web page elements with the web driver for respective browser
     */
    public Page() {
        final String msg = String.format("Initialized web elements of page class '%s'",
                this.getClass().getCanonicalName());
        Page.LOGGER.info(msg);
        this.utils = new GenericUtils(this.getWebDriver());
    }


}
