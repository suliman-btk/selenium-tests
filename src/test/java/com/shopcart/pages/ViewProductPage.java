package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ViewProductPage extends BasePage {

    private final By addToCart = By.xpath("//button[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ADD TO CART')]");
    private final By buyNow = By.xpath("//button[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'BUY NOW')]");

    public ViewProductPage(WebDriver driver) { super(driver); }

    public ViewProductPage addToCart() {
        WaitUtils.waitClickable(driver, addToCart).click();
        return this;
    }
    public ViewProductPage buyNow() {
        WaitUtils.waitClickable(driver, buyNow).click();
        return this;
    }
    public boolean addToCartVisible() {
        try {
            WaitUtils.waitVisible(driver, addToCart);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
