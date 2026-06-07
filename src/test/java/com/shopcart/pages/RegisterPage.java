package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegisterPage extends BasePage {

    private final By userName = By.id("userName");
    private final By shopName = By.id("shopName");
    private final By email = By.id("email");
    private final By password = By.id("password");
    private final By submit = By.xpath("//button[@type='submit']");
    private final By nameHelper = By.xpath("//p[contains(text(),'Name is required')]");
    private final By shopHelper = By.xpath("//p[contains(text(),'Shop name is required')]");
    private final By emailHelper = By.xpath("//p[contains(text(),'Email is required')]");
    private final By passHelper = By.xpath("//p[contains(text(),'Password is required')]");

    public RegisterPage(WebDriver driver) { super(driver); }

    public RegisterPage openCustomer() { go("/Customerregister"); return this; }
    public RegisterPage openSeller()   { go("/Sellerregister"); return this; }

    public RegisterPage fillName(String v) {
        clearAndType(userName, v);
        return this;
    }
    public RegisterPage fillShopName(String v) {
        clearAndType(shopName, v);
        return this;
    }
    public RegisterPage fillEmail(String v) {
        clearAndType(email, v);
        return this;
    }
    public RegisterPage fillPassword(String v) {
        clearAndType(password, v);
        return this;
    }
    public RegisterPage submit() {
        WaitUtils.waitClickable(driver, submit).click();
        return this;
    }

    public boolean nameRequiredShown()  { return !driver.findElements(nameHelper).isEmpty(); }
    public boolean shopRequiredShown()  { return !driver.findElements(shopHelper).isEmpty(); }
    public boolean emailRequiredShown() { return !driver.findElements(emailHelper).isEmpty(); }
    public boolean passRequiredShown()  { return !driver.findElements(passHelper).isEmpty(); }

    public boolean registeredSuccessfully() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.not(
                            ExpectedConditions.urlContains("register")));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
