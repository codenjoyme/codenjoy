package com.codenjoy.dojo.cucumber.definitions;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.cucumber.page.WebDriverWrapper;
import com.codenjoy.dojo.cucumber.page.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class StepDefinitions {

    @Autowired
    private WebDriverWrapper web;

    @Autowired
    private LoginPage login;

    @Autowired
    private BoardPage board;

    @Autowired
    private RegistrationPage registration;

    @Autowired
    private ErrorPage error;

    @Autowired
    private Page page;

    @Autowired
    private AdminPage admin;

    @Autowired
    private WebsocketClients clients;

    @Before
    public void cleanUp() {
        registration.cleanUp();
        admin.cleanUp();
        clients.cleanUp();
    }

    @After
    public void tearDown() {
        cleanUp();
        web.closeBrowser();
    }

    @When("Open login page")
    public void loginPage() {
        login.open();
    }

    @When("Open admin login page")
    public void loginAdminPage() {
        login.adminOpen();
    }

    @When("Try to login as {string} with {string} password in game {string}")
    public void login(String email, String password, String game) {
        login.email(email);
        login.password(password);
        login.game(game);
        login.submit();
    }

    @When("Try to login as {string} with {string} password")
    public void login(String email, String password) {
        login.email(email);
        login.password(password);
        login.submit();
    }

    @Then("See {string} login error")
    public void assertLoginError(String error) {
        login.assertErrorMessage(error);
    }

    @Then("See {string} registration error")
    public void assertRegistrationError(String error) {
        registration.assertErrorMessage(error);
    }

    @When("Press register button")
    public void pressRegisterButton() {
        login.clickRegister();
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
        registration.name(name);
        registration.email(email);
        registration.password(password);
        registration.confirmPassword(password);
        registration.country(country);
        registration.tech(techSkills);
        registration.company(company);
        registration.experience(experience);
        registration.game(game);
        registration.submit();
    }

    @Then("User registered in database as {string}")
    public void assertUserRegistered(String user) {
        registration.assertUserInDatabase(user);
    }

    @When("Click logout")
    public void logout() {
        page.logout();
    }

    @When("Click login")
    public void login() {
        page.login();
    }

    @Then("Login link present")
    public void assertLoginLinkPresent() {
        page.assertLoginLink();
    }

    @Then("Logout link present")
    public void assertLogoutLinkPresent() {
        page.assertLogoutLink();
    }

    @Given("User registered with name {string}, email {string}, " +
            "password {string}, city {string}, " +
            "tech skills {string}, company {string}, " +
            "experience {string}")
    public void registerUser(String name, String email,
                              String password, String country,
                              String techSkills, String company,
                              String experience)
    {
        registration.registerInDatabase(name, email, password,
                country, techSkills, company, experience);
    }

    @When("Try open Admin page")
    public void tryOpenAdminPage() {
        admin.open();
    }

    @When("Login as {string} {string}")
    public void loginAs(String email, String password) {
        loginAs(email, password, "first");
    }

    @When("Login as {string} {string} in game {string}")
    public void loginAs(String email, String password, String game) {
        login.open();
        login(email, password, game);
        board.assertOnPage();
        page.assertUrl(BoardPage.URL);
    }

    @Then("Error page opened with message {string}")
    public void assertErrorPageWith(String message) {
        error.assertOnPage();
        error.assertTicketNumber();
        error.assertErrorMessage(message);
        error.clear();
    }

    @Then("We are on page with url {string}")
    public void assertUrl(String url) {
        page.assertUrl(url);
    }

    @Then("Admin page opened with url {string}")
    public void assertAdminPageOpened(String url) {
        admin.assertOnPage();
        page.assertUrl(url);
    }

    @Then("Board page opened with url {string}")
    public void assertBoardPageOpened(String url) {
        board.assertOnPage();
        page.assertUrl(url);
    }

    @Given("Login to Admin page")
    public void loginToAdminPage() {
        loginAs("admin@codenjoyme.com", "admin");
        admin.open();
        assertAdminPageOpened(AdminPage.URL + "first");
    }

    @Then("Registration is active")
    public void assertRegistrationIsActive() {
        admin.assertRegistrationActive(true);
    }

    @Then("Registration was closed")
    public void assertRegistrationWasClosed() {
        admin.assertRegistrationActive(false);
    }

    @When("Click Close registration")
    public void clickCloseRegistration() {
        admin.registrationChangeActiveLink().click();
    }

    @When("Click Open registration")
    public void clickOpenRegistration() {
        clickCloseRegistration();
    }

    @When("Open registration page")
    public void openRegistrationPage() {
        registration.open();
    }

    @And("There is no controls on registration form")
    public void assertNoControlsOnRegistrationForm() {
        registration.assertFormHidden();
    }

    @And("There is no controls on login form")
    public void assertNoControlsOnLoginForm() {
        login.assertFormHidden();
    }

    @Then("Game is resumed")
    public void assertGameIsResumed() {
        admin.assertGameIsActive(true);
    }

    @When("Click Pause game")
    public void clickPauseGame() {
        admin.pauseResumeGameLink().click();
    }

    @Then("Game is paused")
    public void assertGameIsPaused() {
        admin.assertGameIsActive(false);
    }

    @When("Click Resume game")
    public void clickResumeGame() {
        clickPauseGame();
    }

    @When("Select game room {string}")
    public void selectGameRoom(String room) {
        admin.selectRoom(room);
    }

    @Then("Check game room is {string}")
    public void assertGameIs(String room) {
        admin.assertRoom(room);
    }

    @Then("Websocket client {string} connected successfully to the {string}")
    public void assertClientConnected(String name, String url) {
        String game = page.pageSetting("game");
        url = page.injectSettings(url);
        clients.assertConnected(game, name, url);
    }

    @Then("Websocket {string} send {string} and got {string}")
    public void websocketClientSend(String name, String command, String board) {
        board = board.replaceAll("\\\\n", "\n");
        clients.assertRequestReceived(name, command, board);
    }

    @Then("Websocket {string} send {string} and got nothing")
    public void websocketClientGotNothing(String name, String command) {
        clients.assertRequestReceivedNothing(name, command);
    }
}
