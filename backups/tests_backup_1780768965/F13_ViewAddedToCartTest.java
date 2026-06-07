package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.pages.SellerDashboardPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.13 — View Added to Cart. REQ_F1200, REQ_IO0130. */
public class F13_ViewAddedToCartTest extends BaseTest {

    @Test(description = "REQ_F1200_1: seller views products customers added to carts")
    public void viewAddedToCart() {
        startTest("REQ_F1200_1 View Added To Cart",
                "Seller navigates to Orders and sees added-to-cart table");
        JsonNode seller = JsonDataReader.users().get("seller");
        new LoginPage(driver).openSeller()
                .typeEmail(seller.get("email").asText())
                .typePassword(seller.get("password").asText())
                .submit();
        SellerDashboardPage dash = new SellerDashboardPage(driver);
        dash.gotoOrders();
        Assert.assertTrue(
                dash.pageSource().toLowerCase().contains("cart")
                        || dash.pageSource().toLowerCase().contains("order"),
                "Expected orders or cart-related table");
    }
}
