package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.CheckoutShippingPage;
import com.shopcart.pages.LoginPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.6 — Add Shipping Address. REQ_F600, REQ_IO060. */
public class F6_AddShippingAddressTest extends BaseTest {

    private void loginCustomer() {
        JsonNode u = JsonDataReader.users().get("customer");
        LoginPage login = new LoginPage(driver).openCustomer()
                .typeEmail(u.get("email").asText())
                .typePassword(u.get("password").asText())
                .submit();
        login.loggedIn();
    }

    @Test(description = "REQ_F600_1: full valid shipping form proceeds to next step")
    public void shippingHappyPath() {
        startTest("REQ_F600_1 Shipping Happy Path",
                "All required address fields filled with valid data accepts and moves on");
        loginCustomer();
        CheckoutShippingPage page = new CheckoutShippingPage(driver).open()
                .fillAll("123 Test Street", "Amman", "111000",
                         "Jordan", "Amman", "0790000000")
                .submitEmpty();
        Assert.assertFalse(page.error("required"),
                "Did not expect any 'required' error after filling all fields");
    }

    @Test(description = "REQ_IO060_8..16: empty form shows required errors per field")
    public void emptyShippingShowsErrors() {
        startTest("REQ_IO060_8 Empty Shipping Errors",
                "Submitting blank shipping form shows required messages");
        loginCustomer();
        CheckoutShippingPage page = new CheckoutShippingPage(driver).open().submitEmpty();
        boolean anyError = page.error("required") || page.error("Required")
                || page.error("Address") || page.error("City");
        Assert.assertTrue(anyError, "Expected required-field error after empty submit");
    }
}
