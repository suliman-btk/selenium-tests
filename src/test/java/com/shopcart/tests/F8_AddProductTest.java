package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.pages.SellerAddProductPage;
import com.shopcart.pages.SellerDashboardPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.8 — Add Product. REQ_F800, REQ_IO080. */
public class F8_AddProductTest extends BaseTest {

    @Test(description = "REQ_F800_1: seller adds new product appears in list")
    public void addProductHappyPath() {
        startTest("REQ_F800_1 Add Product",
                "Seller fills all required fields, clicks ADD, product appears in their product list");
        JsonNode seller = JsonDataReader.users().get("seller");
        new LoginPage(driver).openSeller()
                .typeEmail(seller.get("email").asText())
                .typePassword(seller.get("password").asText())
                .submit();

        JsonNode p = JsonDataReader.products().get("sampleProduct");
        new SellerDashboardPage(driver).gotoAddProduct();
        new SellerAddProductPage(driver).fill(
                p.get("productImage").asText(),
                p.get("productName").asText(),
                p.get("description").asText(),
                p.get("mrp").asText(),
                p.get("cost").asText(),
                p.get("discountPercent").asText(),
                p.get("category").asText(),
                p.get("subcategory").asText(),
                p.get("tagline").asText()
        ).submit();

        SellerDashboardPage dash = new SellerDashboardPage(driver);
        dash.gotoProducts();
        Assert.assertTrue(dash.hasProduct(p.get("productName").asText()),
                "Newly added product not found in seller product list");
    }
}
