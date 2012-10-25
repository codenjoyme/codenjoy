package net.tetris.online.service;

import net.tetris.services.Player;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: serhiy.zelenin
 * Date: 10/24/12
 * Time: 5:29 PM
 */
@RunWith(MockitoJUnitRunner.class)
public class GameLoggerTest {

    private GameLogger gameLogger;

    @Mock
    private ServiceConfiguration configuration;
    private ServiceConfigFixture fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new ServiceConfigFixture();
        fixture.setupConfiguration(configuration);
        gameLogger = new GameLogger(configuration);
    }

    @After
    public void tearDown() throws IOException {
        fixture.tearDown();
    }

    @Test
    public void shouldStoreLogs() throws IOException {
        Player player = startLogForPlayer("vasya", "2012-10-24 173822");
        gameLogger.log(player, "/?figure=T&x=4&y=19&glass=*", "left=1,right=2,rotate=3,drop");
        gameLogger.stop();

        assertEquals("/?figure=T&x=4&y=19&glass=*" + "@" + "left=1,right=2,rotate=3,drop", readPlayerLogs(player, "2012-10-24 173822"));
    }

    @Test
    public void shouldFlushPreviousWhenNotFinished() throws IOException {
        Player vasya = startLogForPlayer("vasya", "2012-10-24 173822");
        gameLogger.log(vasya, "vasya request", "vasya response");

        Player petya = startLogForPlayer("petya", "2012-10-24 123456");
        gameLogger.log(petya, "petya request", "petya response");
        gameLogger.stop();

        assertEquals("vasya request" + "@" + "vasya response", readPlayerLogs(vasya, "2012-10-24 173822"));
        assertEquals("petya request" + "@" + "petya response", readPlayerLogs(petya, "2012-10-24 123456"));
    }

    @Test
    public void shouldAppend() throws IOException {
        Player player = startLogForPlayer("vasya", "123");

        gameLogger.log(player, "request1", "response1");
        gameLogger.log(player, "request2", "response2");
        gameLogger.stop();

        assertTrue(readPlayerLogs(player, "123").contains("request2"));
    }

    private File createPlayerLogDir(String playerName) {
        File playerLogsDir = new File(configuration.getLogsDir(), playerName);
        playerLogsDir.mkdirs();
        return playerLogsDir;
    }



    private String readPlayerLogs(Player player, String timeStamp) throws IOException {
        return readLogFile(getPlayerLogs(player.getName(), timeStamp));
    }

    private File getPlayerLogs(String playerName, String timeStamp) {
        File vasyaFolder = new File(fixture.getLogsFolder(), playerName);
        return new File(vasyaFolder, timeStamp);
    }

    private String readLogFile(File logFile) throws IOException {
        return StringUtils.trim(FileUtils.readFileToString(logFile));
    }

    private Player startLogForPlayer(String vasya, String timeStamp) {
        Player player = new Player(vasya, "<not important>", null, null);
        gameLogger.start(player, timeStamp);
        return player;
    }

}
