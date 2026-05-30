package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutShippingPage extends BasePage {

    private By labelInput(String label) {
        return By.xpath("//label[contains(text(),'" + label + "')]/following::input[1]");
    }

    private final By next = By.xpath("//button[contains(text(),'Next') or contains(text(),'NEXT')]");

    public CheckoutShippingPage(WebDriver driver) { super(driver); }

    public CheckoutShippingPage open() { go("/Checkout"); return this; }

    public CheckoutShippingPage fillAll(String address, String city, String pin,
                                        String country, String state, String phone) {
        WaitUtils.waitVisible(driver, labelInput("Address")).sendKeys(address);
        driver.findElement(labelInput("City")).sendKeys(city);
        driver.findElement(labelInput("Zip")).sendKeys(pin);
        driver.findElement(labelInput("Country")).sendKeys(country);
        driver.findElement(labelInput("State")).sendKeys(state);
        driver.findElement(labelInput("Phone")).sendKeys(phone);
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
