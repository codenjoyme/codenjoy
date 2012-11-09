package net.tetris.online.service;


import net.tetris.dom.*;
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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static net.tetris.dom.TestUtils.assertContainsPlot;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ContextConfiguration(classes = {ReplayService.class,
        MockScreenSenderConfiguration.class, MockReplayPlayerController.class,
        MockGameSettingsService.class, MockServiceConfiguration.class})
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
    private MockJoystick joystick;
    public static final int GLASS_CENTER = 4;

    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        mockServiceConfiguration.setUp();
        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        joystick = new MockJoystick();
        Mockito.reset(replayPlayerController, screenSender, gameSettings);
    }

    @After
    public void tearDown() throws IOException {
        mockServiceConfiguration.tearDown();
    }

    @Test
    public void shouldReplayWhenOneStep() throws IOException {
        GameLogFile logFile = createLogFile("testUser", "123");
        addPlayerResponse(logFile, "bla-bla");
        logFile.close();

        replayService.replay("testUser", "123");

        //verify proper plots sent
        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        List<Plot> plots = getPlotsFor("testUser");
        assertEquals(S_FIGURE_PLOTS_AMOUNT, plots.size());
        //Figure S plots sent
        assertContainsSFigurePlots(plots, 0, 0);
    }

    @Test
    public void shouldReplayWhenSeveralSteps() {
        GameLogFile logFile = createLogFile("testUser", "123");
        addPlayerResponse(logFile, "left=" + 2);
        addPlayerResponse(logFile, "bla-bla");
        logFile.close();

        replayService.replay("testUser", "123");

        //verify proper plots sent
        verify(screenSender, times(2)).sendUpdates(screenSendCaptor.capture());
        List<Plot> plots = getPlotsFor("testUser");
        assertEquals(S_FIGURE_PLOTS_AMOUNT, plots.size());

        //Figure S plots sent
        assertContainsSFigurePlots(plots, -2, -1);
    }

    @Test
    public void shouldRemovePlayerWhenReplayEnds() {
        GameLogFile logFile = createLogFile("testUser", "321");
        logFile.close();

        replayService.replay("testUser", "321");

        assertTrue(replayService.getPlayers().isEmpty());
    }

    @Test
    public void shouldDoNothingWhenNoLogs() {
        GameLogFile logFile = createLogFile("testUser", "321");
        logFile.close();

        replayService.replay("fake", "111");

        verify(screenSender, never()).sendUpdates(screenSendCaptor.capture());
        assertTrue(replayService.getPlayers().isEmpty());
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
        Map<Player, PlayerData> value = screenSendCaptor.getValue();
        for (Map.Entry<Player, PlayerData> entry : value.entrySet()) {
            if (entry.getKey().getName().equals(playerName)) {
                return entry.getValue().getPlots();
            }
        }
        return null;
    }

/*
    @Test
    public void shouldReturnPlotsOfCurrentAndDroppedFigures() {
        Player vasya = createPlayer("vasya");
        forceDropFigureInGlass(3, HEIGHT, new TetrisFigure());

        replayService.nextStepForAllGames();

        assertSentToPlayers(vasya);
        List<Plot> plots = getPlotsFor(vasya);
        assertContainsPlot(3, 0, PlotColor.BLUE, plots);
    }

    @Test
    public void shouldRequestControlFromAllPlayers() throws IOException {
        Player vasya = createPlayer("vasya");
        Player petya = createPlayer("petya");

        replayService.nextStepForAllGames();

        assertSentToPlayers(vasya, petya);
        verify(playerController, times(2)).requestControl(playerCaptor.capture(), figureCaptor.capture(),
                xCaptor.capture(), yCaptor.capture(), Matchers.<Joystick>any(), plotsCaptor.capture());

        assertHostsCaptured("http://vasya:1234", "http://petya:1234");
    }

    @Test
    public void shouldRequestControlFromAllPlayersWithGlassState() throws IOException {
        createPlayer("vasya");
        forceDropFigureInGlass(0, HEIGHT, new TetrisFigure());

        replayService.nextStepForAllGames();

        verify(playerController).requestControl(playerCaptor.capture(), figureCaptor.capture(),
                xCaptor.capture(), yCaptor.capture(), Matchers.<Joystick>any(), plotsCaptor.capture());
        List<Plot> sentPlots = plotsCaptor.getValue();
        assertEquals(1, sentPlots.size());
        assertContainsPlot(0, 0, PlotColor.BLUE, sentPlots);
    }

    @Test
    public void shouldSendAdditionalInfoToAllPlayers() throws IOException {
        Player vasya = createPlayer("vasya");
        Player petya = createPlayer("petya");

        replayService.nextStepForAllGames();

        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        Map<Player, PlayerData> data = screenSendCaptor.getValue();

        Map<String, String> expected = new HashMap<>();
        expected.put("vasya", "PlayerData[Plots:[Plot{x=4, y=19, color=BLUE}, Plot{x=4, y=18, color=BLUE}, Plot{x=4, y=17, color=BLUE}, Plot{x=4, y=16, color=BLUE}], " +
                "Score:0, LinesRemoved:0, NextLevelIngoingCriteria:'This is last level', CurrentLevel:1]");

        expected.put("petya", "PlayerData[Plots:[Plot{x=4, y=19, color=BLUE}, Plot{x=4, y=18, color=BLUE}, Plot{x=4, y=17, color=BLUE}, Plot{x=4, y=16, color=BLUE}], " +
                "Score:0, LinesRemoved:0, NextLevelIngoingCriteria:'This is last level', CurrentLevel:1]");

        assertEquals(2, data.size());

        for (Map.Entry<Player, PlayerData> entry : data.entrySet()) {
            assertEquals(expected.get(entry.getKey().getName()), entry.getValue().toString());
        }
    }

    @Test
    public void shouldGetLevelsWhenRegistrateNewUser() throws IOException {
        createPlayer("vasya");

        verify(gameSettings).getGameLevels(any(PlayerFigures.class));
    }

    @Test
    public void shouldNewUserHasZerroScoresWhenLastLoggedIfOtherPlayerHasPositiveScores() {
        // given
        Player vasya = createPlayer("vasya");
        forceDropFigureInAllPlayerGlasses(); // vasia +10

        // when
        Player petya = createPlayer("petya");

        // then
        assertEquals(0, petya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedIfSomePlayersHasNegativeScores() {
        // given
        Player vasya = createPlayer("vasya");
        forceDropFigureInAllPlayerGlasses(); // vasia +10
        Player petya = createPlayer("petya");

        // when
        forceEmptyAllPlayerGlasses(); // vasia & petia -500
        Player katya = createPlayer("katya");

        // then
        assertEquals(petya.getScore(), katya.getScore());
        assertEquals(PlayerScores.GLASS_OVERFLOWN_PENALTY, petya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedAfterNextStep() {
        // given
        Player vasya = createPlayer("vasya");
        forceDropFigureInAllPlayerGlasses(); // vasia +10
        Player petya = createPlayer("petya");
        forceEmptyAllPlayerGlasses(); // vasia & petia -500
        Player katya = createPlayer("katya");

        // when
        replayService.nextStepForAllGames();
        Player olia = createPlayer("olia");

        // then
        assertEquals(olia.getScore(), katya.getScore());
        assertEquals(olia.getScore(), petya.getScore());
    }

    @Test
    public void shouldRemoveAllPlayerDataWhenRemovePlayer() {
        // given
        createPlayer("vasya");
        createPlayer("petya");

        // when
        replayService.removePlayer("http://vasya:1234");

        //then
        assertNull(replayService.findPlayer("vasya"));
        assertNotNull(replayService.findPlayer("petya"));
        assertEquals(1, replayService.getGlasses().size());
    }

    @Test
    public void shouldFindPlayerWhenGetByIp() {
        // given
        Player newPlayer = createPlayer("vasya_ip");

        // when
        Player player = replayService.findPlayerByIp("vasya_ip");

        //then
        assertSame(newPlayer, player);
    }

    @Test
    public void shouldGetNullPlayerWhenGetByNotExistsIp() {
        // given
        createPlayer("vasya_ip");

        // when
        Player player = replayService.findPlayerByIp("kolia_ip");

        //then
        assertEquals(NullPlayer.class, player.getClass());
    }

    private void forceEmptyAllPlayerGlasses() {
        for (Glass glass : replayService.getGlasses()) {
            glass.empty();
        }
    }

    private Player createPlayer(String userName) {
        return replayService.addNewPlayer(userName, "http://" + userName + ":1234");
    }


    private List<Plot> getPlotsFor(Player vasya) {
        Map<Player, PlayerData> value = screenSendCaptor.getValue();
        return value.get(vasya).getPlots();
    }


    private void forceDropFigureInAllPlayerGlasses() {
        for (Glass glass : replayService.getGlasses()) {
             glass.drop(new TetrisFigure(), 0, HEIGHT);
        }
    }

    private void forceDropFigureInGlass(int x, int y, TetrisFigure point) {
        Glass glass = replayService.getGlasses().get(0);
        glass.drop(point, x, y);
    }

    private void assertHostsCaptured(String ... hostUrls) {
        assertEquals(hostUrls.length, playerCaptor.getAllValues().size());
        for (int i = 0; i < hostUrls.length; i++) {
            String hostUrl = hostUrls[i];
            assertEquals(hostUrl, playerCaptor.getAllValues().get(i).getCallbackUrl());
        }
    }
*/

}
