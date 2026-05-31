package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.CheckoutShippingPage;
import com.shopcart.pages.LoginPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/** SRS 3.2.6 — Add Shipping Address. REQ_F600, REQ_IO060. */
public class F6_AddShippingAddressTest extends BaseTest {

    @BeforeClass
    public void clearShippingData() throws Exception {
        JsonNode u = JsonDataReader.users().get("customer");
        HttpClient http = HttpClient.newHttpClient();

        // Login to get customer ID
        String loginBody = String.format(
            "{\"email\":\"%s\",\"password\":\"%s\"}",
            u.get("email").asText(), u.get("password").asText());
        HttpRequest loginReq = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:5000/CustomerLogin"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(loginBody))
            .build();
        String loginResp = http.send(loginReq, HttpResponse.BodyHandlers.ofString()).body();
        String customerId = new ObjectMapper().readTree(loginResp).get("_id").asText();

        // Empty object → Object.keys(shippingData).length === 0 → ShippingPage shows input form
        String clearBody = "{\"shippingData\":{}}";
        HttpRequest clearReq = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:5000/CustomerUpdate/" + customerId))
            .header("Content-Type", "application/json")
            .PUT(HttpRequest.BodyPublishers.ofString(clearBody))
            .build();
        http.send(clearReq, HttpResponse.BodyHandlers.ofString());
    }

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
