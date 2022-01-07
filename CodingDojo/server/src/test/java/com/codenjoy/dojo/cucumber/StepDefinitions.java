package com.codenjoy.dojo.cucumber;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.cucumber.page.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;

import static org.junit.Assert.assertEquals;

@RequiredArgsConstructor
public class StepDefinitions {

    private final WebDriverWrapper web;
    private final LoginPage login;
    private final BoardPage board;
    private final RegistrationPage registration;
    private final ErrorPage error;
    private final Page page;
    private final AdminPage admin;
    private final WebsocketClients clients;
    private final Storage storage;

    @Before
    public void cleanUp() {
        admin.close();
        registration.close();
        clients.close();
        storage.close();
        error.close();
    }

    @After
    public void tearDown() {
        cleanUp();
        web.close();
    }

    @ParameterType(value = "true|True|TRUE|false|False|FALSE")
    public Boolean bool(String value) {
        return Boolean.valueOf(value);
    }

    @When("Open login page")
    public void loginPage() {
        login.open();
    }

    @When("Open admin login page")
    public void loginAdminPage() {
        login.adminOpen();
    }

    @When("Try to login as {string} with {string} password in room {string}")
    public void login(String email, String password, String room) {
        login.email(email);
        login.password(password);
        login.game(room);
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
            "experience {string}, game {string}, room {string}")
    public void tryToRegister(String name, String email,
                              String password, String country,
                              String techSkills, String company,
                              String experience, String game,
                              String room)
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
        registration.room(room);
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

    @Given("Open Admin page")
    public void openAdminPage() {
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
        error.close();
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

    @Then("Board page opened with url {string} in room {string}")
    public void assertBoardPageOpened(String url, String room) {
        board.assertOnPage();
        page.assertUrl(url);
        page.assertRoom(room);
    }

    @Given("Login to Admin page")
    public void loginToAdminPage() {
        login.adminOpen();
        login.email("admin@codenjoyme.com");
        login.password("admin");
        login.submit();
        admin.assertOnPage();
        assertAdminPageOpened(AdminPage.URL + "first");
    }

    // ------------

    @Then("Registration is active")
    public void assertRegistrationIsActive() {
        admin.serverRegistration().assertOpened();
    }

    @Then("Registration was closed")
    public void assertRegistrationWasClosed() {
        admin.serverRegistration().assertClosed();
    }

    @When("Click Close registration")
    public void clickCloseRegistration() {
        admin.serverRegistration().close();
    }

    @When("Click Open registration")
    public void clickOpenRegistration() {
        admin.serverRegistration().open();
    }

    // ------------

    @Then("Room registration is active")
    public void assertRoomRegistrationIsActive() {
        admin.roomRegistration().assertOpened();
    }

    @Then("Room registration was closed")
    public void assertRoomRegistrationWasClosed() {
        admin.roomRegistration().assertClosed();
    }

    @When("Click Close room registration")
    public void clickCloseRoomRegistration() {
        admin.roomRegistration().close();
    }

    @When("Click Open room registration")
    public void clickOpenRoomRegistration() {
        admin.roomRegistration().open();
    }

    // ------------

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

    // ------------

    @Then("Game is resumed")
    public void assertGameIsResumed() {
        admin.pauseResume().assertActive();
    }

    @When("Click Pause game")
    public void clickPauseGame() {
        admin.pauseResume().pauseGame();
    }

    @Then("Game is paused")
    public void assertGameIsPaused() {
        admin.pauseResume().assertPaused();
    }

    @When("Click Resume game")
    public void clickResumeGame() {
        admin.pauseResume().resumeGame();
    }

    // ------------

    @When("Click Start game recording")
    public void clickStartGameRecording() {
        admin.gameRecording().start();
    }

    @Then("Game recording is started")
    public void assertGameRecordingIsStarted() {
        admin.gameRecording().assertStarted();
    }

    @When("Click Stop game recording")
    public void clickStopGameRecording() {
        admin.gameRecording().stop();
    }

    @Then("Game recording is suspended")
    public void assertGameRecordingIsSuspended() {
        admin.gameRecording().assertSuspended();
    }

    // ------------

    @When("Click Start debug")
    public void clickStartDebug() {
        admin.debug().start();
    }

    @Then("Debug is started")
    public void assertDebugIsStarted() {
        admin.debug().assertStarted();
    }

    @When("Click Stop debug")
    public void clickStopDebug() {
        admin.debug().stop();
    }

    @Then("Debug is suspended")
    public void assertDebugIsSuspended() {
        admin.debug().assertSuspended();
    }

    // ------------

    @When("Click Start auto save")
    public void clickStartAutoSsave() {
        admin.autoSave().start();
    }

    @Then("Auto save is started")
    public void assertAutoSaveIsStarted() {
        admin.autoSave().assertStarted();
    }

    @When("Click Stop auto save")
    public void clickStopAutoSave() {
        admin.autoSave().stop();
    }

    @Then("Auto save is suspended")
    public void assertAutoSaveIsSuspended() {
        admin.autoSave().assertSuspended();
    }

    // ------------

    @When("Select game room {string}")
    public void selectGameRoom(String room) {
        admin.selectRoom(room);
    }

    @Then("Check game is {string} and room is {string}")
    public void assertGame(String game, String room) {
        admin.assertGameAndRoom(game, room);
    }

    @Then("Check game is {string} and room is {string} on the information panel")
    public void assertGameAndRoom(String game, String room) {
        admin.gameRoomStatus().assertRoom(room);
        admin.gameRoomStatus().assertGame(game);
    }

    @Then("Game version is {string}")
    public void assertGameVersion(String version) {
        admin.gameRoomStatus().assertGameVersion(version);
    }

    @Then("Websocket client {string} connected successfully to the {string}")
    public void assertClientConnected(String name, String url) {
        String game = page.pageSetting("game");
        url = page.injectSettings(url);
        clients.registerWebSocketClient(game, name, url);
    }

    @Then("Websocket {string} send {string}")
    public void websocketClientSend(String name, String command) {
        clients.sendRequest(name, command);
    }

    // TODO [RK#4]: Emphasise that client override running solver to return different command.
    //              messages are being sent on the background when a session is opened.
    @Then("Websocket {string} send {string} and got {string}")
    public void websocketClientSendAndGot(String name, String command, String board) {
        board = board.replaceAll("\\\\n", "\n");
        clients.assertRequestReceived(name, command, board);
        page.refresh();
    }

    @Then("Websocket {string} send {string} and got nothing")
    public void websocketClientGotNothing(String name, String command) {
        clients.assertRequestReceivedNothing(name, command);
    }

    @When("Open page with url {string}")
    public void openPageWithUrl(String url) {
        page.open(url);
    }

    @Then("There is list of rooms {string} on the login and register form")
    public void thereIsListOfRoomsOnLoginAndRegisterForm(String rooms) {
        thereIsListOfRoomsOnLoginForm(rooms);
        login.clickRegister();
        thereIsListOfRoomsOnRegisterForm(rooms);
    }

    @Then("There is list of rooms {string} on the login form")
    public void thereIsListOfRoomsOnLoginForm(String rooms) {
        login.assertRoomsAvailable(rooms);
    }

    @Then("There is list of rooms {string} on the register form")
    public void thereIsListOfRoomsOnRegisterForm(String rooms) {
        registration.assertRoomsAvailable(rooms);
    }

    @Then("There is list of rooms {string} on the admin page")
    public void thereIsListOfRoomsOnAdminPage(String rooms) {
        admin.assertRoomsAvailable(rooms);
    }

    @Then("There are players {string} on the leaderboard")
    public void thereArePlayersOnLeaderboard(String players) {
        board.leaderboard().waitUntilNotEmpty();
        board.leaderboard().assertPlayers(players);
    }

    @Then("There are players in rooms {string} on the admin page")
    public void thereArePlayersOnTheRoomsOnAdminPage(String expected) {
        admin.assertPlayersInRooms(expected);
    }

    @When("Click LoadAll players")
    public void clickLoadAllPlayers() {
        admin.players().loadAllPlayers();
        clients.refreshAllRunnersSessions();
    }

    @When("Set inactivity kick enabled checkbox to {bool}")
    public void playerUserMailComIsKickedTrue(boolean enabled) {
        admin.inactivity().kickEnabled(enabled);
    }

    @And("Set inactivity timeout parameter to {int}")
    public void setInactivityTimeoutParameterTo(int ticks) {
        admin.inactivity().timeout(ticks);
    }

    @And("Press inactivity settings save button")
    public void pressInactivitySettingsSaveButton() {
        admin.inactivity().submit();
    }

    @Then("Inactivity parameters {string}")
    public void assertInactivitySettings(String expected) {
        assertEquals(expected, admin.inactivity().toString());
    }

    @And("All players inactivity ticks are reset")
    public void assertAllPlayersInactivityTicksReset() {
        admin.inactivity().playersInactiveTicks()
                .forEach(ticks -> assertEquals("0", ticks.getText()));
    }

    @Then("Wait for {int} seconds when refresh is {bool}")
    public void waitForSeconds(int secondsToWait, boolean refresh) throws InterruptedException {
        for (int second = 0; second < secondsToWait; second++) {
            Thread.sleep(1000);
            if (refresh) {
                page.refresh();
            }
        }
    }

    @And("Shutdown {string} websocket runner")
    public void shutdownClientWebsocketRunner(String player) {
        clients.shutDownRunnerSession(player);
    }

    @Then("Player {string} is kicked {bool}")
    public void playerIsKicked(String email, boolean isKicked) {
        admin.inactivity().assertPlayerKicked(email, isKicked);
    }

    @When("Enter value {string} = {string} for the {string} is {string} and click Save")
    public void updatePlayerAttribute(String key, String value, String selectorKey, String selectorValue) {
        admin.players().updatePlayer(selectorKey, selectorValue, key, value);
    }

    @When("Click ViewGame for the {string} is {string}")
    public void clickViewGameOfPlayer(String selectorKey, String selectorValue) {
        String url = admin.players().playerBoardLink(selectorKey, selectorValue);
        page.open(url);
    }

    @When("Save page url as {string}")
    public void savePageUrlAs(String key) {
        storage.save(key, page.url(false));
    }

    @When("Click AllBoards on Leaderboard")
    public void clickAllBoardsOnLeaderboard() {
        String url = board.leaderboard().allBoardsLink();
        page.open(url);
    }

    @Then("All levels are {string}")
    public void allLevelsAre(String map) {
        admin.levels().assertMaps(map);
    }

    @When("Change map value at {int} to {string}")
    public void changeMapValue(int index, String map) {
        admin.levels().mapValue(index, map);
    }

    @When("Change map key at {int} to {string}")
    public void changeMapKey(int index, String key) {
        admin.levels().mapKey(index, key);
    }

    @When("Save all level maps")
    public void saveAllLevelMaps() {
        admin.levels().save();
    }

    @When("Add new map")
    public void addNewMap() {
        admin.levels().add();
    }

    @Then("Timer period is {int}")
    public void timerPeriodIs(int mills) {
        admin.timerPeriod().checkIs(mills);
    }

    @When("Update timer period to {int}")
    public void updateTimerPeriodTo(int mills) {
        admin.timerPeriod().update(mills);
    }

    @When("Click Set timer period button")
    public void clickSetTimerPeriodButton() {
        admin.timerPeriod().save();
    }

    @When("Create new room {string}")
    public void createNewRoom(String room) {
        admin.gameRoomStatus().createNew(room);
    }

    @When("Remove room")
    public void removeRoom() {
        admin.gameRoomStatus().removeRoom();
    }
}