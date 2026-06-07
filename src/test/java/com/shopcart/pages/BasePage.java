package com.shopcart.pages;

import com.shopcart.base.ConfigReader;
import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class BasePage {
    protected final WebDriver driver;
    protected static final String BASE_URL = ConfigReader.get("baseUrl");

    protected BasePage(WebDriver driver) { this.driver = driver; }

    /** Full page load. Auth is persisted in localStorage, so the session survives. */
    public void go(String path) {
        driver.get(BASE_URL + path);
        settle();
    }

    /**
     * Previously used history.pushState which React Router v6 ignores, so target
     * pages never rendered. A full navigation works because login is kept in
     * localStorage and Redux re-hydrates on load.
     */
    public void navigateSPA(String path) {
        driver.get(BASE_URL + path);
        settle();
    }

    private void settle() {
        try { Thread.sleep(800); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    /** Robust clear for React controlled inputs (Ctrl+A, Delete, then type). */
    protected void clearAndType(By locator, String value) {
        WebElement el = WaitUtils.waitVisible(driver, locator);
        el.click();
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
        el.sendKeys(value);
    }

    public String currentUrl() { return driver.getCurrentUrl(); }
    public String pageSource() { return driver.getPageSource(); }
}
