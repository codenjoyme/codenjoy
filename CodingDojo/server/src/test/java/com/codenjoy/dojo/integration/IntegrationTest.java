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


import com.codenjoy.dojo.CodenjoyContestApplication;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.SaveService;
import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.services.dao.PlayerGameSaver;
import com.codenjoy.dojo.services.mail.MailService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CodenjoyContestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SQLiteProfile.NAME)
public class IntegrationTest {
    private static WebDriver driver;
    private String url;

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @SpyBean
    private PlayerService playerService;

    @SpyBean
    private MailService mailService;

    @Autowired
    private TimerService timerService;

    @Autowired
    private SaveService saveService;

    @Autowired
    private PlayerGameSaver playerGameSaver;

    @Before
    public void setupJetty() throws Exception {
        url = String.format("http://localhost:%s%s", port, contextPath);

        System.out.println(url);
        timerService.pause();

        driver = new HtmlUnitDriver(true);
        saveService.removeAllSaves();
        playerService.removeAll();

        timerService.resume();
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
        return name + new Random().nextInt(Integer.MAX_VALUE) + "@gmail.com";
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
        List<Player> players = playerService.getAll();
        List<String> namesList = new LinkedList<String>();
        for (Player player : players) {
            namesList.add(player.getId());
        }
        assertEquals(names, namesList.toString());
    }

    private void saveAll(String saves) {
        driver.get(url + "admin");
        saveService.removeAllSaves();
        assertSaves("[]");
        driver.findElement(By.linkText("SaveAll")).click();
        assertSaves(saves);
    }

    private void assertSaves(String saves) {
        assertEquals(saves, playerGameSaver.getSavedList().toString());
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
        verify(mailService).sendEmail(eq(name), eq("Codenjoy регистрация"), captor.capture());
        String message = captor.getValue();
        String str = "href=\"";
        int fromIndex = message.indexOf(str) + str.length();
        String activationUrl = message.substring(fromIndex, message.indexOf('"', fromIndex + 1));
        activationUrl = activationUrl.replace("codenjoy.com:80", "localhost:" + port);
        return activationUrl;
    }

}
