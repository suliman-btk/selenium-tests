package com.shopcart.pages;

import com.shopcart.base.ConfigReader;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public abstract class BasePage {
    protected final WebDriver driver;
    protected static final String BASE_URL = ConfigReader.get("baseUrl");

    protected BasePage(WebDriver driver) { this.driver = driver; }

    /** Full page load — use only for initial app boot or public routes. */
    public void go(String path) { driver.get(BASE_URL + path); }

    /** SPA navigation — preserves Redux state by using pushState + popstate. */
    public void navigateSPA(String path) {
        ((JavascriptExecutor) driver).executeScript(
            "window.history.pushState(null, '', arguments[0]); " +
            "window.dispatchEvent(new Event('popstate'));", path);
        try { Thread.sleep(300); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String currentUrl() { return driver.getCurrentUrl(); }
    public String pageSource() { return driver.getPageSource(); }
}
