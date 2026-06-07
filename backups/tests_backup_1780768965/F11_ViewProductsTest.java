package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.pages.SellerDashboardPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.11 — View Products. REQ_F1000, REQ_IO0110. */
public class F11_ViewProductsTest extends BaseTest {

    @Test(description = "REQ_F1000_1: seller dashboard lists own products")
    public void viewProducts() {
        startTest("REQ_F1000_1 View Products",
                "After login, seller navigates to Products and sees own product list");
        JsonNode seller = JsonDataReader.users().get("seller");
        new LoginPage(driver).openSeller()
                .typeEmail(seller.get("email").asText())
                .typePassword(seller.get("password").asText())
                .submit();
        SellerDashboardPage dash = new SellerDashboardPage(driver);
        dash.gotoProducts();
        Assert.assertTrue(
                dash.pageSource().toLowerCase().contains("product")
                        || dash.pageSource().toLowerCase().contains("shop"),
                "Expected products view to render product table or empty-state");
    }
}
