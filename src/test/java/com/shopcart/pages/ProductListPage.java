package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductListPage extends BasePage {

    public ProductListPage(WebDriver driver) { super(driver); }

    public ProductListPage open() { go("/Products"); return this; }

    public boolean hasResults() {
        return !driver.findElements(By.xpath("//img | //*[contains(@class,'product')]")).isEmpty();
    }

    public ProductListPage openFirstProduct() {
        WaitUtils.waitClickable(driver,
                By.xpath("(//a[contains(@href,'/product/view/')])[1]")).click();
        return this;
    }
}
