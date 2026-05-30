package com.shopcart.utils;

import com.shopcart.base.ConfigReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private static Duration timeout() {
        return Duration.ofSeconds(ConfigReader.getInt("explicitWait"));
    }

    public static WebElement waitClickable(WebDriver driver, By locator) {
        return new WebDriverWait(driver, timeout())
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitVisible(WebDriver driver, By locator) {
        return new WebDriverWait(driver, timeout())
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static boolean waitTextPresent(WebDriver driver, String text) {
        try {
            return new WebDriverWait(driver, timeout())
                    .until(d -> d.getPageSource().contains(text));
        } catch (Exception e) {
            return false;
        }
    }
}
