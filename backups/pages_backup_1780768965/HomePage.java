package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By searchInput = By.xpath("//input[contains(@placeholder,'Search') or @type='search']");
    private final By categoriesBtn = By.xpath("//*[contains(text(),'Categories')]");

    public HomePage(WebDriver driver) { super(driver); }

    public HomePage open() { go("/"); return this; }

    public HomePage search(String term) {
        WaitUtils.waitVisible(driver, searchInput).clear();
        driver.findElement(searchInput).sendKeys(term);
        driver.findElement(searchInput).sendKeys(org.openqa.selenium.Keys.ENTER);
        return this;
    }

    public boolean displaysProduct(String name) {
        return WaitUtils.waitTextPresent(driver, name);
    }

    public HomePage openCategories() {
        WaitUtils.waitClickable(driver, categoriesBtn).click();
        return this;
    }
}
