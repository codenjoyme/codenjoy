package com.codenjoy.dojo.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class StepDefinitions {

    @Autowired
    private WebDriverWrapper web;

    @Given("Login page opened in browser")
    public void loginPage() {
        web.open("/login");
    }

    @When("Try to login as {string} with {string} password in game {string}")
    public void login(String email, String password, String game) {
        web.element("#email input").sendKeys(email);
        web.element("#password input").sendKeys(password);
        web.select("#game select").selectByVisibleText(game);
        web.click("#submit-button");
    }

    @Then("See {string} login error")
    public void login(String error) {
        assertEquals(error, web.element("#error-message").getText());
    }
}
