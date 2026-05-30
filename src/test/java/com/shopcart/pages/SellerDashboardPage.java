package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SellerDashboardPage extends BasePage {

    public SellerDashboardPage(WebDriver driver) { super(driver); }

    public SellerDashboardPage gotoAddProduct() {
        WaitUtils.waitClickable(driver,
                By.xpath("//*[contains(text(),'Add Product') or contains(text(),'ADD PRODUCT')]")).click();
        return this;
    }

    public SellerDashboardPage gotoProducts() {
        WaitUtils.waitClickable(driver,
                By.xpath("//*[contains(text(),'Products') or contains(text(),'Show Products')]")).click();
        return this;
    }

    public SellerDashboardPage gotoOrders() {
        WaitUtils.waitClickable(driver,
                By.xpath("//*[contains(text(),'Orders')]")).click();
        return this;
    }

    public boolean hasProduct(String name) {
        return WaitUtils.waitTextPresent(driver, name);
    }
}
