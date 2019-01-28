package com.codenjoy.dojo.integration;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.integration.mocker.SpringMockerJettyRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.PlayerGameSaver;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.mail.MailService;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import javax.mail.MessagingException;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class IntegrationTest {

    private static PlayerService players;
    private static TimerService timer;
    private static SaveServiceImpl save;
    private static GameServiceImpl game;
    private static MailService mail;
    private static WebDriver driver;
    private static SpringMockerJettyRunner runner;
    private static PlayerGameSaver saver;
    private static String url;
    private static int port;

    @BeforeClass
    public static void setupJetty() throws Exception {
        runner = new SpringMockerJettyRunner("src/main/webapp", "/appcontext");
        runner.spyBean("playerService");
        runner.spyBean("mailService");
        port = runner.start(new Random().nextInt(1000) + 10000);


        url = runner.getUrl();
        System.out.println(url);

        players = runner.getBean(PlayerService.class, "playerService");
        timer = runner.getBean(TimerService.class, "timerService");
        save = runner.getBean(SaveServiceImpl.class, "saveService");
        game = runner.getBean(GameServiceImpl.class, "gameService");
        saver = runner.getBean(PlayerGameSaver.class, "playerGameSaver");
        mail = runner.getBean(MailService.class, "mailService");
        timer.pause();

        driver = new HtmlUnitDriver(true);
        save.removeAllSaves();
        players.removeAll();

        timer.resume();
    }

    @Test
    @Ignore
    public void test() throws Exception {
        register(getEmail("apofig"), "pass", "snake");
        // TODO continue test
//        save("[apofig]");
//
//        register(getEmail("zanefig"), "pass2", "sample");
//
//        saveAll("[apofig, zanefig]");
//
//        removeSaveAll("[]");
//
//        assertPlayers("[apofig, zanefig]");
//        gameOverAll("[]");

        // admin-load
        // admin-loadAll
        // admin-removeSave
        // admin-gameOver
        // admin-viewGame
        // admin-cleanAllScores
        // admin-pauseGame
        // admin-switchToNewGame

        // connectWebsocketClientToServer
        // useJoystick
        // useJoystickOnMultiBoardGame
        // cantJoystickWhenNotMyUser
        // unregisterAtMainPage
        // spyAnotherUser
        // loginToAnotherUser
    }

    private String getEmail(String name) {
        return name + Math.abs(new Random().nextInt()) + "@gmail.com";
    }

    private void removeSaveAll(String saves) {
        driver.get(url + "admin");
        driver.findElement(By.linkText("RemoveSaveAll")).click();
    }

    private void gameOverAll(String names) {
        driver.get(url + "admin");
        driver.findElement(By.linkText("GameOverAll")).click();
        assertPlayers(names);
    }

    private void assertPlayers(String names) {
        List<Player> players = IntegrationTest.players.getAll();
        List<String> namesList = new LinkedList<String>();
        for (Player player : players) {
            namesList.add(player.getName());
        }
        assertEquals(names, namesList.toString());
    }

    private void saveAll(String saves) {
        driver.get(url + "admin");
        save.removeAllSaves();
        assertSaves("[]");
        driver.findElement(By.linkText("SaveAll")).click();
        assertSaves(saves);
    }

    private void assertSaves(String saves) {
        assertEquals(saves, saver.getSavedList().toString());
    }

    private void save(String saves) {
        driver.get(url + "admin");
        driver.findElement(By.linkText("Save")).click();
        assertSaves(saves);
    }

    private void register(String name, String password, String gameName) throws Exception {
        driver.get(url);

        driver.findElement(By.linkText("Register")).click();

        assertEquals("Registration", driver.findElement(By.id("title")).getText());
        assertEquals("Register", driver.findElement(By.id("submit")).getAttribute("value"));
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("gameName")).sendKeys(gameName);
        driver.findElement(By.id("submit")).click();

        String activationUrl = getActivationUrl(name);

        assertEquals("http://localhost:" + port + "/appcontext/register", driver.getCurrentUrl());
        assertTrue(activationUrl.startsWith("http://localhost:" + port + "/appcontext/register?approve="));

        driver.get(activationUrl);
    }

    private String getActivationUrl(String name) throws MessagingException {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mail).sendEmail(eq(name), eq("Codenjoy регистрация"), captor.capture());
        String message = captor.getValue();
        String str = "href=\"";
        int fromIndex = message.indexOf(str) + str.length();
        String activationUrl = message.substring(fromIndex, message.indexOf('"', fromIndex + 1));
        activationUrl = activationUrl.replace("tetrisj.jvmhost.net:12270", "localhost:" + port);
        return activationUrl;
    }

}
