package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By email = By.id("email");
    private final By password = By.id("password");
    private final By submit = By.xpath("//button[@type='submit']");
    private final By popupMsg = By.xpath("//*[contains(@class,'MuiAlert') or contains(@role,'alert')]");
    private final By emailHelper = By.xpath("//p[contains(text(),'Email is required')]");
    private final By passHelper = By.xpath("//p[contains(text(),'Password is required')]");

    public LoginPage(WebDriver driver) { super(driver); }

    public LoginPage openCustomer() { go("/Customerlogin"); return this; }
    public LoginPage openSeller()   { go("/Sellerlogin"); return this; }

    public LoginPage typeEmail(String v) {
        WaitUtils.waitVisible(driver, email).clear();
        driver.findElement(email).sendKeys(v);
        return this;
    }
    public LoginPage typePassword(String v) {
        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(v);
        return this;
    }
    public LoginPage submit() {
        WaitUtils.waitClickable(driver, submit).click();
        return this;
    }

    public boolean emailRequiredShown() {
        return !driver.findElements(emailHelper).isEmpty();
    }
    public boolean passwordRequiredShown() {
        return !driver.findElements(passHelper).isEmpty();
    }
    public String popupText() {
        try { return WaitUtils.waitVisible(driver, popupMsg).getText(); }
        catch (Exception e) { return ""; }
    }
    public boolean loggedIn() {
        return WaitUtils.waitTextPresent(driver, "Home")
                && !currentUrl().contains("login");
    }
}
