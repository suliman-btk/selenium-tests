package com.shopcart.pages;

import com.shopcart.base.ConfigReader;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    protected final WebDriver driver;
    protected static final String BASE_URL = ConfigReader.get("baseUrl");

    protected BasePage(WebDriver driver) { this.driver = driver; }

    public void go(String path) { driver.get(BASE_URL + path); }
    public String currentUrl() { return driver.getCurrentUrl(); }
    public String pageSource() { return driver.getPageSource(); }
}
