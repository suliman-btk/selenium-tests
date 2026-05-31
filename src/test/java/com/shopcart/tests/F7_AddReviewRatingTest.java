package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.pages.ProductListPage;
import com.shopcart.pages.ReviewPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.annotations.Test;

/** SRS 3.2.7 — Add Review & Rating. REQ_F700, REQ_IO070.
 *  Review form lives at /order/view/:productId (ViewOrder), not ViewProduct. */
public class F7_AddReviewRatingTest extends BaseTest {

    @Test(description = "REQ_F700_1: customer adds review + 5-star rating on owned order")
    public void addReviewHappyPath() {
        startTest("REQ_F700_1 Add Review",
                "Customer with prior order writes review + rating and submits");
        JsonNode u = JsonDataReader.users().get("customer");
        LoginPage login = new LoginPage(driver).openCustomer()
                .typeEmail(u.get("email").asText())
                .typePassword(u.get("password").asText())
                .submit();
        login.loggedIn();

        // Navigate to products, click first product to land on /product/view/:id
        new ProductListPage(driver).open().openFirstProduct();

        // Extract product ID from current URL (/product/view/:id)
        String url = driver.getCurrentUrl();
        String productId = url.substring(url.lastIndexOf("/") + 1);

        // ViewOrder (/order/view/:productId) has the review textarea — ViewProduct does not
        new ReviewPage(driver).openForProduct(productId).writeReview("Great product!", 5);
    }
}
