package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SellerDashboardPage extends BasePage {

    public SellerDashboardPage(WebDriver driver) { super(driver); }

    private void waitForLogin() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(20))
                    .until(ExpectedConditions.not(ExpectedConditions.urlContains("login")));
        } catch (Exception ignored) {}
    }

    public SellerDashboardPage gotoAddProduct() {
        waitForLogin();
        go("/Seller/addproduct");
        return this;
    }

    public SellerDashboardPage gotoProducts() {
        waitForLogin();
        go("/Seller/products");
        return this;
    }

    public SellerDashboardPage gotoOrders() {
        waitForLogin();
        go("/Seller/orders");
        return this;
    }

    public boolean hasProduct(String name) {
        return WaitUtils.waitTextPresent(driver, name);
    }
}
