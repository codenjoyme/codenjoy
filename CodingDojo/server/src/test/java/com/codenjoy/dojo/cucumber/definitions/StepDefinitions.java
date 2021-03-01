package com.codenjoy.dojo.cucumber.definitions;

import com.codenjoy.dojo.cucumber.WebDriverWrapper;
import com.codenjoy.dojo.cucumber.page.Page;
import com.codenjoy.dojo.services.dao.Registration;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

import static com.codenjoy.dojo.cucumber.page.Page.CODE;
import static org.junit.Assert.assertEquals;

public class StepDefinitions {

    @Autowired
    private WebDriverWrapper web;

    @Autowired
    private Registration registration;

    @Autowired
    private Page page;

    @When("Login page opened in browser")
    public void loginPage() {
        web.open("/login");
    }

    @When("Try to login as {string} with {string} password in game {string}")
    public void login(String email, String password, String game) {
        web.text("#email input", email);
        web.text("#password input", password);
        web.select("#game select", game);
        web.click("#submit-button");
    }

    @Then("See {string} login error")
    public void login(String error) {
        assertEquals(error, web.element("#error-message").getText());
    }

    @When("Press register button")
    public void pressRegisterButton() {
        web.element("#register-button").click();
    }

    @When("Try to register with: name {string}, email {string}, " +
            "password {string}, city {string}, " +
            "tech skills {string}, company {string}, " +
            "experience {string}, game {string}")
    public void tryToRegister(String name, String email,
                              String password, String country,
                              String techSkills, String company,
                              String experience, String game)
    {
        web.text("#readableName input", name);
        web.text("#email input", email);
        web.text("#password input", password);
        web.text("#passwordConfirmation input", password);
        web.text("#data1 input", country);
        web.text("#data2 input", techSkills);
        web.text("#data3 input", company);
        web.text("#data4 input", experience);
        web.select("#game select", game);
        web.click("#submit-button");
    }

    @SneakyThrows
    @Then("On page with url {string}")
    public void onGameBoard(String url) {
        assertEquals(page.injectSettings(url), web.url());
    }

    @Given("Clean all registration data")
    public void cleanAllRegistrationData() {
        registration.removeAll();
    }

    @Then("User registered in database as {string}")
    public void userRegisteredInDatabaseAs(String user) {
        assertEquals(page.injectSettings(user),
                registration.getUserByCode(page.pageSetting(CODE)).toString());
    }

    @When("Logout")
    public void logout() {
        page.logLink().click();
    }

    @Then("Login link present")
    public void loginLinkPresent() {
        assertEquals("Login", page.logLink().getText());
    }

    @Then("Logout link present")
    public void logoutLinkPresent() {
        assertEquals("Logout", page.logLink().getText());
    }
}
