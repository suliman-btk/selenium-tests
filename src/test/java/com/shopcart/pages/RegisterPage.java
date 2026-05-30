package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        WaitUtils.waitVisible(driver, userName).clear();
        driver.findElement(userName).sendKeys(v);
        return this;
    }
    public RegisterPage fillShopName(String v) {
        driver.findElement(shopName).clear();
        driver.findElement(shopName).sendKeys(v);
        return this;
    }
    public RegisterPage fillEmail(String v) {
        driver.findElement(email).clear();
        driver.findElement(email).sendKeys(v);
        return this;
    }
    public RegisterPage fillPassword(String v) {
        driver.findElement(password).clear();
        driver.findElement(password).sendKeys(v);
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
        return !currentUrl().contains("register");
    }
}
