package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ReviewPage extends BasePage {

    private final By reviewText = By.xpath("//textarea | //input[@name='review']");
    private final By star = By.xpath("(//*[contains(@class,'MuiRating')]//input)[5]");
    private final By submit = By.xpath("//button[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'SUBMIT')]");

    public ReviewPage(WebDriver driver) { super(driver); }

    public ReviewPage openForProduct(String productId) {
        navigateSPA("/order/view/" + productId);
        return this;
    }

    public ReviewPage writeReview(String text, int rating) {
        WaitUtils.waitVisible(driver, reviewText).sendKeys(text);
        try { driver.findElement(star).click(); } catch (Exception ignored) {}
        return this;
    }
    public ReviewPage submit() {
        WaitUtils.waitClickable(driver, submit).click();
        return this;
    }
}
