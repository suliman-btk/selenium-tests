package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.pages.ReviewPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.annotations.Test;

/** SRS 3.2.7 — Add Review & Rating. REQ_F700, REQ_IO070.
 *  Requires a prior order on the test customer's account. */
public class F7_AddReviewRatingTest extends BaseTest {

    @Test(description = "REQ_F700_1: customer adds review + 5-star rating on owned order")
    public void addReviewHappyPath() {
        startTest("REQ_F700_1 Add Review",
                "Customer with prior order writes review + rating and submits");
        JsonNode u = JsonDataReader.users().get("customer");
        new LoginPage(driver).openCustomer()
                .typeEmail(u.get("email").asText())
                .typePassword(u.get("password").asText())
                .submit();
        // Navigation to /Orders → order detail → review widget left as project-specific.
        // Tester completes this once seed data confirmed.
        new ReviewPage(driver).writeReview("Great product!", 5);
    }
}
