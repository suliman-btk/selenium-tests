package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutShippingPage extends BasePage {

    private final By address = By.id("address");
    private final By city    = By.id("city");
    private final By pin     = By.id("pinCode");
    private final By country = By.id("country");
    private final By state   = By.id("state");
    private final By phone   = By.id("phoneNo");
    private final By next = By.xpath(
        "//button[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'NEXT')]");

    public CheckoutShippingPage(WebDriver driver) { super(driver); }

    public CheckoutShippingPage open() { navigateSPA("/Checkout"); return this; }

    public CheckoutShippingPage fillAll(String addr, String cty, String zip,
                                        String ctry, String st, String ph) {
        clearAndType(address, addr);
        clearAndType(city, cty);
        clearAndType(pin, zip);
        clearAndType(country, ctry);
        clearAndType(state, st);
        clearAndType(phone, ph);
        return this;
    }

    public CheckoutShippingPage submitEmpty() {
        WaitUtils.waitClickable(driver, next).click();
        return this;
    }

    public boolean error(String text) {
        return WaitUtils.waitTextPresent(driver, text);
    }
}
