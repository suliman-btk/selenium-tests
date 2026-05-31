package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SellerDashboardPage extends BasePage {

    public SellerDashboardPage(WebDriver driver) { super(driver); }

    private void waitForLogin() {
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(ExpectedConditions.not(ExpectedConditions.urlContains("login")));
    }

    public SellerDashboardPage gotoAddProduct() {
        waitForLogin();
        navigateSPA("/Seller/addproduct");
        return this;
    }

    public SellerDashboardPage gotoProducts() {
        waitForLogin();
        navigateSPA("/Seller/products");
        return this;
    }

    public SellerDashboardPage gotoOrders() {
        waitForLogin();
        navigateSPA("/Seller/orders");
        return this;
    }

    public boolean hasProduct(String name) {
        return WaitUtils.waitTextPresent(driver, name);
    }
}
