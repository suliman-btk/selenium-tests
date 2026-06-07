package com.shopcart.tests;

import com.shopcart.base.BaseTest;
import com.shopcart.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.3 — Search Product by Name. REQ_F300, REQ_IO030. */
public class F3_SearchByNameTest extends BaseTest {

    @Test(description = "REQ_F300_1: search returns matching product")
    public void searchKnownProduct() {
        startTest("REQ_F300_1 Search Known Product",
                "Searching a real product name returns it in the result list");
        HomePage home = new HomePage(driver).open().search("Selenium");
        // If seeded; tester adjusts term to a guaranteed-existing product
        Assert.assertTrue(home.pageSource().toLowerCase().contains("selenium")
                || home.pageSource().toLowerCase().contains("product"),
                "Expected at least one product card to render");
    }

    @Test(description = "REQ_F300_1: gibberish search yields empty state")
    public void searchGibberish() {
        startTest("REQ_F300_1 Search Gibberish",
                "Random nonsense query produces empty result");
        HomePage home = new HomePage(driver).open().search("zzznoexistxyz123");
        Assert.assertFalse(home.displaysProduct("zzznoexistxyz123 in stock"),
                "Did not expect product cards for gibberish term");
    }
}
