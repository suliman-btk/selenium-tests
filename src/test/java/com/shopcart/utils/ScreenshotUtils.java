package com.shopcart.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final String DIR = "target/screenshots";

    public static String capture(WebDriver driver, String testName) {
        try {
            Files.createDirectories(Paths.get(DIR));
            String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + ts + ".png";
            Path dest = Paths.get(DIR, fileName);
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), dest);
            return dest.toAbsolutePath().toString();
        } catch (Exception e) {
            return null;
        }
    }
}
