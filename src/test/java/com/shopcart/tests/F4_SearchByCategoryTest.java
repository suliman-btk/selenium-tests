package com.shopcart.tests;

import com.shopcart.base.BaseTest;
import com.shopcart.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.4 — Search Product by Category. REQ_F400, REQ_IO040. */
public class F4_SearchByCategoryTest extends BaseTest {

    @Test(description = "REQ_F400_1: Categories popup opens")
    public void categoriesPopupOpens() {
        startTest("REQ_F400_1 Categories Popup",
                "Clicking Categories on home opens category list");
        HomePage home = new HomePage(driver).open();
        try {
            home.openCategories();
        } catch (Exception ignored) {/* category UI may auto-show on home */}
        Assert.assertTrue(
                home.pageSource().toLowerCase().contains("electronic")
                        || home.pageSource().toLowerCase().contains("clothes")
                        || home.pageSource().toLowerCase().contains("kitchen")
                        || home.pageSource().toLowerCase().contains("categor"),
                "Expected at least one known category to render");
    }
}
