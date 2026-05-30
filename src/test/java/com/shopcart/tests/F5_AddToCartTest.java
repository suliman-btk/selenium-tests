package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.pages.ProductListPage;
import com.shopcart.pages.ViewProductPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.5 — Add to Cart. REQ_F500, REQ_IO050. */
public class F5_AddToCartTest extends BaseTest {

    @Test(description = "REQ_F500_1: logged-in customer can add product to cart")
    public void addAsCustomer() {
        startTest("REQ_F500_1 Add To Cart",
                "Customer logs in, opens a product, clicks ADD TO CART successfully");
        JsonNode u = JsonDataReader.users().get("customer");
        new LoginPage(driver).openCustomer()
                .typeEmail(u.get("email").asText())
                .typePassword(u.get("password").asText())
                .submit();
        new ProductListPage(driver).open().openFirstProduct();
        ViewProductPage vp = new ViewProductPage(driver);
        Assert.assertTrue(vp.addToCartVisible(), "Add To Cart button not visible");
        vp.addToCart();
    }
}
