package com.codenjoy.dojo.cucumber;

import com.codenjoy.dojo.services.room.RoomService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class StepDefinitions {

    @Autowired
    private Server server;

    @Given("Home page opened in browser")
    public void homePage() {

    }

    @When("Try to login as {string} with {string}")
    public void login(String email, String password) {

    }

    @Then("See {string} login error")
    public void login(String error) {
        assertEquals("", server.endpoint());
    }
}
