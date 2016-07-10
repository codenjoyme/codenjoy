package net.tetris.online.service;


import net.tetris.services.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static net.tetris.dom.TestUtils.assertContainsPlot;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ReplayService.class,
        MockScreenSenderConfiguration.class, MockReplayPlayerController.class,
        MockGameSettingsService.class, MockServiceConfiguration.class,
        ReplayPoolConfiguration.class, MockGameSaver.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ReplayServiceTest {
    public static final int GLASS_FIGURE_TOP = 17;
    public static final int S_FIGURE_PLOTS_AMOUNT = 4;
    private ArgumentCaptor<Map> screenSendCaptor;

    @Autowired
    private ReplayService replayService;

    @Autowired
    private GameSettings gameSettings;

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController replayPlayerController;

    @Autowired
    private MockServiceConfiguration mockServiceConfiguration;

    @Autowired
    private ServiceConfiguration configuration;
    @Autowired
    private ScheduledThreadPoolExecutor replayExecutor;

    private MockAdvancedTetrisJoystik joystick;
    public static final int GLASS_CENTER = 4;

    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        mockServiceConfiguration.setUp();
        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        joystick = new MockAdvancedTetrisJoystik();
        Mockito.reset(replayPlayerController, screenSender);
    }

    @After
    public void tearDown() throws IOException {
        mockServiceConfiguration.tearDown();
    }

    @Test
    public void shouldReplayWhenOneStep() throws IOException, InterruptedException {
        create1LineLog("testUser", "123");

        replayAndWait("testUser", "123");

        //verify proper plots sent
        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        List<Plot> plots = getPlotsFor("testUser");
        assertEquals(S_FIGURE_PLOTS_AMOUNT, plots.size());
        //Figure S plots sent
        assertContainsSFigurePlots(plots, 0, 0);
    }

    @Test
    public void shouldReplayWhenSeveralSteps() throws InterruptedException {
        create2LineLog("testUser", "123", "left=" + 2);

        replayAndWait("testUser", "123");

        //verify proper plots sent
        verifyScreensSent2Times();
        List<Plot> plots = getPlotsFor("testUser");
        assertEquals(S_FIGURE_PLOTS_AMOUNT, plots.size());

        //Figure S plots sent
        assertContainsSFigurePlots(plots, -2, -1);
    }

    @Test
    public void shouldRemovePlayerWhenReplayEnds() throws InterruptedException {
        GameLogFile logFile = createLogFile("testUser", "321");
        logFile.close();

        replayAndWait("testUser", "321");

        assertTrue(replayService.getPlayers().isEmpty());
    }

    @Test
    public void shouldDoNothingWhenNoLogs() throws InterruptedException {
        GameLogFile logFile = createLogFile("testUser", "321");
        logFile.close();

        replayAndWait("fake", "111");

        verify(screenSender, never()).sendUpdates(screenSendCaptor.capture());
        assertTrue(replayService.getPlayers().isEmpty());
    }

    @Test
    public void shouldSendUpdatesOnceWhenSeveralGamesReplaying() throws InterruptedException {
        create1LineLog("user1", "123");
        create1LineLog("user2", "321");

        replayService.replay("user1", "123");
        replayService.replay("user2", "321");
        waitForAllReplaysDone();

        verifyScreensSent1Time();
        assertContainsSFigurePlots(getPlotsFor("user1"), 0, 0);
        assertContainsSFigurePlots(getPlotsFor("user2"), 0, 0);

    }

    private void verifyScreensSent1Time() {
        verify(screenSender, times(1)).sendUpdates(screenSendCaptor.capture());
    }

    @Test
    public void shouldCancelReplayWhenRequested() throws InterruptedException {
        create2LineLog("user", "123", "left=1");
        int replayId = replayService.replay("user", "123");

        replayService.cancelReplay(replayId);
        waitForAllReplaysDone();

        verifyScreensSent1Time();
    }

    @Test
    public void shouldCancelReplayForSpecificTimestampOnly() throws InterruptedException {
        create2LineLog("user1", "123", "left=1");
        create2LineLog("user2", "321", "right=" + 3);

        int replayToCancel = replayService.replay("user1", "123");
        replayService.replay("user2", "321");

        replayService.cancelReplay(replayToCancel);
        waitForAllReplaysDone();

        verifyScreensSent2Times();
        Map firstStep = screenSendCaptor.getAllValues().get(0);
        assertEquals("On 1st step updates are sent for 2 replays", 2, firstStep.size());
        assertEquals("On 2nd step updates are sent for 1 replay", 1, screenSendCaptor.getValue().size());
        assertContainsSFigurePlots(getPlotsFor("user2"), 3, -1);
    }

    private void verifyScreensSent2Times() {
        verify(screenSender, times(2)).sendUpdates(screenSendCaptor.capture());
    }

    private void create2LineLog(String playerName, String timeStamp, String firstResponse) {
        GameLogFile logFile = createLogFile(playerName, timeStamp);
        addPlayerResponse(logFile, firstResponse);
        addPlayerResponse(logFile, "bla-bla");
        logFile.close();
    }

    @Test
    @Ignore
    public void shouldSendAllUpdatesWhenPlayerLogsDifferentSize() {

    }

    private void create1LineLog(String playerName, String timeStamp) {
        GameLogFile logFile1 = createLogFile(playerName, timeStamp);
        addPlayerResponse(logFile1, "bla-bla");
        logFile1.close();
    }

    private void replayAndWait(String playerName, String timestamp) throws InterruptedException {
        replayService.replay(playerName, timestamp);
        waitForAllReplaysDone();
    }

    private void waitForAllReplaysDone() throws InterruptedException {
        while (replayService.hasScheduledReplays()) {
            Thread.sleep(50);
        }
        while (!replayService.getPlayers().isEmpty()) {
            Thread.sleep(100);
        }
    }

    @Test
    public void shouldCancelReplayWhenRequestedSamePlayer(){

    }

    private void assertContainsSFigurePlots(List<Plot> plots, int deltaX, int deltaY) {
        assertContainsPlot(GLASS_CENTER - 1 + deltaX, GLASS_FIGURE_TOP + 1 + deltaY, plots);
        assertContainsPlot(GLASS_CENTER + deltaX, GLASS_FIGURE_TOP + 1 + deltaY, plots);
        assertContainsPlot(GLASS_CENTER + deltaX, GLASS_FIGURE_TOP + 2 + deltaY, plots);
        assertContainsPlot(GLASS_CENTER + 1 + deltaX, GLASS_FIGURE_TOP + 2 + deltaY, plots);
    }

    private void addPlayerResponse(GameLogFile logFile, String response) {
        logFile.log("/tetrisServlet?figure=S&x=4&y=17&glass=++", response);
    }

    private GameLogFile createLogFile(String userName, String timeStamp) {
        return new GameLogFile(configuration, userName, timeStamp);
    }

    private List<Plot> getPlotsFor(String playerName) {
        PlayerData playerData = getDataFor(playerName);
        return playerData == null ? null : playerData.getPlots();
    }

    private PlayerData getDataFor(String playerName) {
        Map<Player, PlayerData> value = screenSendCaptor.getValue();
        for (Map.Entry<Player, PlayerData> entry : value.entrySet()) {
            if (entry.getKey().getName().equals(playerName)) {
                return entry.getValue();
            }
        }
        return null;
    }

}
