package com.shopcart.tests;

import com.fasterxml.jackson.databind.JsonNode;
import com.shopcart.base.BaseTest;
import com.shopcart.pages.LoginPage;
import com.shopcart.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

/** SRS 3.2.2 / 3.1.1 — Sign In. REQ_F200, REQ_IO010. */
public class F2_SignInTest extends BaseTest {

    @Test(description = "REQ_F200_5: valid customer login redirects to home")
    public void customerLoginHappyPath() {
        startTest("REQ_F200_5 Customer Login",
                "Valid customer credentials redirect to home and clear /Customerlogin");
        JsonNode u = JsonDataReader.users().get("customer");
        LoginPage page = new LoginPage(driver).openCustomer()
                .typeEmail(u.get("email").asText())
                .typePassword(u.get("password").asText())
                .submit();
        Assert.assertTrue(page.loggedIn(),
                "Expected redirect away from /Customerlogin after valid login");
    }

    @Test(description = "REQ_F200_5: valid seller login redirects to dashboard")
    public void sellerLoginHappyPath() {
        startTest("REQ_F200_5 Seller Login",
                "Valid seller credentials redirect to seller dashboard");
        JsonNode u = JsonDataReader.users().get("seller");
        LoginPage page = new LoginPage(driver).openSeller()
                .typeEmail(u.get("email").asText())
                .typePassword(u.get("password").asText())
                .submit();
        Assert.assertTrue(page.loggedIn(),
                "Expected redirect away from /Sellerlogin after valid login");
    }

    @Test(description = "REQ_IO010_5: empty email shows 'Email is required'")
    public void emptyEmail() {
        startTest("REQ_IO010_5 Empty Email",
                "Submitting blank email shows required helper text");
        LoginPage page = new LoginPage(driver).openCustomer()
                .typePassword("anything")
                .submit();
        Assert.assertTrue(page.emailRequiredShown(), "Email required helper not shown");
    }

    @Test(description = "REQ_IO010_6: empty password shows 'Password is required'")
    public void emptyPassword() {
        startTest("REQ_IO010_6 Empty Password",
                "Submitting blank password shows required helper text");
        LoginPage page = new LoginPage(driver).openCustomer()
                .typeEmail("anything@x.com")
                .submit();
        Assert.assertTrue(page.passwordRequiredShown(), "Password required helper not shown");
    }

    @Test(description = "REQ_IO010_4: unknown user shows 'User not found' popup")
    public void unknownUser() {
        startTest("REQ_IO010_4 Unknown User",
                "Submitting unregistered email shows server error popup");
        JsonNode u = JsonDataReader.users().get("invalid");
        LoginPage page = new LoginPage(driver).openCustomer()
                .typeEmail(u.get("email").asText())
                .typePassword(u.get("password").asText())
                .submit();
        String txt = page.popupText().toLowerCase();
        Assert.assertTrue(
                txt.contains("not found") || txt.contains("invalid") || txt.contains("user"),
                "Expected error popup; got: " + txt);
    }

    @Test(description = "REQ_IO010_7: wrong password shows 'Invalid Password' popup")
    public void wrongPassword() {
        startTest("REQ_IO010_7 Wrong Password",
                "Existing email + wrong password shows invalid password popup");
        JsonNode customer = JsonDataReader.users().get("customer");
        LoginPage page = new LoginPage(driver).openCustomer()
                .typeEmail(customer.get("email").asText())
                .typePassword("DefinitelyWrong@99")
                .submit();
        String txt = page.popupText().toLowerCase();
        Assert.assertTrue(
                txt.contains("invalid") || txt.contains("password") || txt.contains("not"),
                "Expected error popup; got: " + txt);
    }
}
