package com.shopcart.pages;

import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SellerAddProductPage extends BasePage {

    private By labelInput(String label) {
        return By.xpath("//label[contains(text(),'" + label + "')]/following::input[1]");
    }
    private final By addBtn = By.xpath("//button[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'ADD')]");

    public SellerAddProductPage(WebDriver driver) { super(driver); }

    public SellerAddProductPage fill(String image, String name, String desc,
                                     String mrp, String cost, String discount,
                                     String category, String subcat, String tagline) {
        WaitUtils.waitVisible(driver, labelInput("Product Image")).sendKeys(image);
        driver.findElement(labelInput("Product Name")).sendKeys(name);
        driver.findElement(labelInput("Description")).sendKeys(desc);
        driver.findElement(labelInput("MRP")).sendKeys(mrp);
        driver.findElement(labelInput("Cost")).sendKeys(cost);
        driver.findElement(labelInput("Discount")).sendKeys(discount);
        driver.findElement(labelInput("Category")).sendKeys(category);
        driver.findElement(labelInput("Subcategory")).sendKeys(subcat);
        driver.findElement(labelInput("tagline")).sendKeys(tagline);
        return this;
    }

    public SellerAddProductPage submit() {
        WaitUtils.waitClickable(driver, addBtn).click();
        return this;
    }
}
