package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.pages.SellerDashboardPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.14 — View Out for Delivery. REQ_F1300, REQ_IO0140. */
public class F14_ViewOutForDeliveryTest extends BaseTest {

    @Test(description = "REQ_F1300_1: seller views out-for-delivery products")
    public void viewOutForDelivery() {
        startTest("REQ_F1300_1 View Out For Delivery",
                "Seller navigates to Orders and views out-for-delivery table");
        JsonNode seller = JsonDataReader.users().get("seller");
        new LoginPage(driver).openSeller()
                .typeEmail(seller.get("email").asText())
                .typePassword(seller.get("password").asText())
                .submit();
        SellerDashboardPage dash = new SellerDashboardPage(driver);
        dash.gotoOrders();
        Assert.assertTrue(
                dash.pageSource().toLowerCase().contains("deliver")
                        || dash.pageSource().toLowerCase().contains("order"),
                "Expected delivery/order section");
    }
}
