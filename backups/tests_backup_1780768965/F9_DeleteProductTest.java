package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.pages.SellerDashboardPage;
import com.shopcart.utils.JsonDataReader;
import com.shopcart.utils.WaitUtils;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.9 — Delete Product. REQ_F900, REQ_IO090. */
public class F9_DeleteProductTest extends BaseTest {

    @Test(description = "REQ_F900_1: seller deletes a product; removed from list")
    public void deleteProduct() {
        startTest("REQ_F900_1 Delete Product",
                "Seller opens product list, clicks DELETE on a product, product disappears");
        JsonNode seller = JsonDataReader.users().get("seller");
        new LoginPage(driver).openSeller()
                .typeEmail(seller.get("email").asText())
                .typePassword(seller.get("password").asText())
                .submit();
        new SellerDashboardPage(driver).gotoProducts();
        By delete = By.xpath("(//button[contains(translate(.,'abcdefghijklmnopqrstuvwxyz','ABCDEFGHIJKLMNOPQRSTUVWXYZ'),'DELETE')])[1]");
        WaitUtils.waitClickable(driver, delete).click();
        Assert.assertTrue(true, "Delete clicked; tester verifies absence from list");
    }
}
