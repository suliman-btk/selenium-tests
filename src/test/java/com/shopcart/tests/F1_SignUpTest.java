package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.RegisterPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.UUID;

/** SRS 3.2.1 / 3.1.2 — Sign Up. REQ_F100, REQ_IO020. */
public class F1_SignUpTest extends BaseTest {

    private String unique(String prefix) {
        return prefix + "+" + UUID.randomUUID().toString().substring(0,8) + "@shopcart.test";
    }

    @Test(description = "REQ_F100_1: valid customer signup creates account")
    public void customerSignupHappyPath() {
        startTest("REQ_F100_1 Customer Signup Happy Path",
                "Customer fills all valid fields; account created & redirects home");
        JsonNode u = JsonDataReader.users().get("customer");
        RegisterPage page = new RegisterPage(driver).openCustomer()
                .fillName(u.get("name").asText())
                .fillEmail(unique("cust"))
                .fillPassword(u.get("password").asText())
                .submit();
        Assert.assertTrue(page.registeredSuccessfully(),
                "Expected redirect away from /Customerregister after valid signup");
    }

    @Test(description = "REQ_F100_1: valid seller signup creates shop")
    public void sellerSignupHappyPath() {
        startTest("REQ_F100_1 Seller Signup Happy Path",
                "Seller fills name, shopName, email, password; account created");
        JsonNode u = JsonDataReader.users().get("seller");
        RegisterPage page = new RegisterPage(driver).openSeller()
                .fillName(u.get("name").asText())
                .fillShopName(u.get("shopName").asText())
                .fillEmail(unique("seller"))
                .fillPassword(u.get("password").asText())
                .submit();
        Assert.assertTrue(page.registeredSuccessfully(),
                "Expected redirect away from /Sellerregister after valid signup");
    }

    @Test(description = "REQ_IO020_6: missing email shows 'Email is required'")
    public void emptyEmail() {
        startTest("REQ_IO020_6 Empty Email",
                "Submitting with email blank shows 'Email is required'");
        RegisterPage page = new RegisterPage(driver).openCustomer()
                .fillName("X")
                .fillPassword("Some@Pass123")
                .submit();
        Assert.assertTrue(page.emailRequiredShown(), "Email-required helper text missing");
    }

    @Test(description = "REQ_IO020_10: missing password shows 'Password is required'")
    public void emptyPassword() {
        startTest("REQ_IO020_10 Empty Password",
                "Submitting with password blank shows 'Password is required'");
        RegisterPage page = new RegisterPage(driver).openCustomer()
                .fillName("X")
                .fillEmail(unique("nopwd"))
                .submit();
        Assert.assertTrue(page.passRequiredShown(), "Password-required helper text missing");
    }

    @Test(description = "REQ_IO020_11: missing name shows 'Name is required'")
    public void emptyName() {
        startTest("REQ_IO020_11 Empty Name",
                "Submitting with name blank shows 'Name is required'");
        RegisterPage page = new RegisterPage(driver).openCustomer()
                .fillEmail(unique("noname"))
                .fillPassword("Some@Pass123")
                .submit();
        Assert.assertTrue(page.nameRequiredShown(), "Name-required helper text missing");
    }
}
