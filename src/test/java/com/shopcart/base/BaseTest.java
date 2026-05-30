package com.shopcart.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.shopcart.utils.ScreenshotUtils;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected static ExtentReports extent;
    protected ExtentTest test;

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        if (extent == null) {
            ExtentSparkReporter spark = new ExtentSparkReporter("target/ShopCartTestReport.html");
            spark.config().setDocumentTitle("ShopCart SVV Test Report");
            spark.config().setReportName("Selenium E2E — SRS Functional Tests");
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("App", "ShopCart MERN");
            extent.setSystemInfo("Base URL", ConfigReader.get("baseUrl"));
            extent.setSystemInfo("Browser", ConfigReader.get("browser"));
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.create();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getInt("implicitWait")));
        driver.get(ConfigReader.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (test != null) {
            if (result.getStatus() == ITestResult.FAILURE) {
                String shot = ScreenshotUtils.capture(driver, result.getName());
                test.log(Status.FAIL, result.getThrowable());
                if (shot != null) test.addScreenCaptureFromPath(shot);
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.log(Status.PASS, "Test passed");
            } else {
                test.log(Status.SKIP, "Test skipped");
            }
        }
        if (driver != null) driver.quit();
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        if (extent != null) extent.flush();
    }

    protected ExtentTest startTest(String name, String description) {
        test = extent.createTest(name, description);
        return test;
    }
}
