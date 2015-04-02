package com.codenjoy.dojo.integration;

import com.codenjoy.dojo.integration.mocker.SpringMockerJettyRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.chat.ChatServiceImpl;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.Nullable;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 22:17
 */
public class IntegrationTest {

    private static PlayerService players;
    private static TimerService timer;
    private static SaveServiceImpl save;
    private static ChatServiceImpl chat;
    private static GameServiceImpl game;
    private static WebDriver driver;
    private static SpringMockerJettyRunner runner;
    private static PlayerGameSaver saver;
    private static String url;
    private static int port;

    @BeforeClass
    public static void setupJetty() throws Exception {
        runner = new SpringMockerJettyRunner("src/main/webapp", "/codenjoy-contest");
        runner.spyBean("playerService");
        port = runner.start(new Random().nextInt(1000) + 10000);


        url = runner.getUrl();
        System.out.println(url);

        players = runner.getBean(PlayerService.class, "playerService");
        timer = runner.getBean(TimerService.class, "timerService");
        save = runner.getBean(SaveServiceImpl.class, "saveService");
        chat = runner.getBean(ChatServiceImpl.class, "chatService");
        game = runner.getBean(GameServiceImpl.class, "gameService");
        saver = runner.getBean(PlayerGameSaver.class, "gameSaver");
        timer.pause();

        driver = new HtmlUnitDriver(true);
        save.removeAllSaves();
        players.removeAll();

        new File("chat.log").delete();
        timer.resume();
    }

    @Test
    @Ignore
    public void test() throws InterruptedException {
        register("apofig", "pass");

        sendChat("hello world");

        save("[apofig]");

        register("zanefig", "pass2");

        saveAll("[apofig, zanefig]");

        removeSaveAll("[]");

        assertPlayers("[apofig, zanefig]");
        gameOverAll("[]");

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
        // cantSendChatWhenNotMyUser
        // cantJoystickWhenNotMyUser
        // unregisterAtMainPage
        // spyAnotherUser
        // loginToAnotherUser
    }

    private void removeSaveAll(String saves) {
        driver.get(url + "admin31415");
        driver.findElement(By.linkText("RemoveSaveAll")).click();
    }

    private void gameOverAll(String names) {
        driver.get(url + "admin31415");
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
        driver.get(url + "admin31415");
        save.removeAllSaves();
        assertSaves("[]");
        driver.findElement(By.linkText("SaveAll")).click();
        assertSaves(saves);
    }

    private void assertSaves(String saves) {
        assertEquals(saves, saver.getSavedList().toString());
    }

    private void save(String saves) {
        driver.get(url + "admin31415");
        driver.findElement(By.linkText("Save")).click();
        assertSaves(saves);
    }

    private void sendChat(final String message) {
        driver.findElement(By.id("chat-message")).sendKeys(message);
        driver.findElement(By.id("chat-send")).click();

        new WebDriverWait(driver, 500) {}.until(new ExpectedCondition<Object>() {
            @Override
            public Object apply(@Nullable WebDriver driverObject) {
                return chat.getChatLog().toString().contains(message);
            }
        });
    }

    private void register(String name, String password) {
        driver.get(url);
        driver.findElement(By.linkText("Register")).click();

        assertEquals("Registration", driver.findElement(By.id("title")).getText());
        assertEquals("Register", driver.findElement(By.id("submit")).getAttribute("value"));
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("submit")).click();

        assertEquals("http://localhost:" + port + "/codenjoy-contest/board/" + name + "?code=" + Registration.makeCode(name, password), driver.getCurrentUrl());
    }

}
