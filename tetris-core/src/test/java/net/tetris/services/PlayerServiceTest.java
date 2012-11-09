package net.tetris.services;


import net.tetris.dom.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import static net.tetris.dom.TestUtils.HEIGHT;
import static net.tetris.dom.TestUtils.assertContainsPlot;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PlayerService.class,
        MockScreenSenderConfiguration.class, MockPlayerController.class,
        MockGameSettingsService.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {
    private ArgumentCaptor<Map> screenSendCaptor;
    private ArgumentCaptor<Player> playerCaptor;
    private ArgumentCaptor<Integer> xCaptor;
    private ArgumentCaptor<Integer> yCaptor;
    private ArgumentCaptor<Figure.Type> figureCaptor;
    private ArgumentCaptor<List> plotsCaptor;

    @Spy
    private Levels levels;

    @Autowired
    private PlayerService<Object> playerService;

    @Autowired
    private GameSettings gameSettings;

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController playerController;

    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        playerCaptor = ArgumentCaptor.forClass(Player.class);
        xCaptor = ArgumentCaptor.forClass(Integer.class);
        yCaptor = ArgumentCaptor.forClass(Integer.class);
        figureCaptor = ArgumentCaptor.forClass(Figure.Type.class);
        plotsCaptor = ArgumentCaptor.forClass(List.class);

        playerService.clear();
        Mockito.reset(playerController, screenSender, gameSettings);
    }

    @Test
    public void shouldSendCoordinatesToPlayerBoard() throws IOException {
        Player vasya = createPlayer("vasya");

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya);
        List<Plot> plots = getPlotsFor(vasya);
        assertContainsPlot(4, 19, plots);
    }

    @Test
    public void shouldReturnPlotsOfCurrentAndDroppedFigures() {
        Player vasya = createPlayer("vasya");
        forceDropFigureInGlass(3, HEIGHT, new TetrisFigure());

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya);
        List<Plot> plots = getPlotsFor(vasya);
        assertContainsPlot(3, 0, PlotColor.BLUE, plots);
    }

    @Test
    public void shouldRequestControlFromAllPlayers() throws IOException {
        Player vasya = createPlayer("vasya");
        Player petya = createPlayer("petya");

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya, petya);
        verify(playerController, times(2)).requestControl(playerCaptor.capture(), figureCaptor.capture(),
                xCaptor.capture(), yCaptor.capture(), Matchers.<Joystick>any(), plotsCaptor.capture());

        assertHostsCaptured("http://vasya:1234", "http://petya:1234");
    }

    @Test
    public void shouldRequestControlFromAllPlayersWithGlassState() throws IOException {
        createPlayer("vasya");
        forceDropFigureInGlass(0, HEIGHT, new TetrisFigure());

        playerService.nextStepForAllGames();

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

        playerService.nextStepForAllGames();

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
        playerService.nextStepForAllGames();
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
        playerService.removePlayer("http://vasya:1234");

        //then
        assertNull(playerService.findPlayer("vasya"));
        assertNotNull(playerService.findPlayer("petya"));
        assertEquals(1, playerService.getGlasses().size());
    }

    @Test
    public void shouldFindPlayerWhenGetByIp() {
        // given
        Player newPlayer = createPlayer("vasya_ip");

        // when
        Player player = playerService.findPlayerByIp("vasya_ip");

        //then
        assertSame(newPlayer, player);
    }

    @Test
    public void shouldGetNullPlayerWhenGetByNotExistsIp() {
        // given
        createPlayer("vasya_ip");

        // when
        Player player = playerService.findPlayerByIp("kolia_ip");

        //then
        assertEquals(NullPlayer.class, player.getClass());
    }

    @Test
    public void shouldSkipSendScreensWhenNoPlayers() throws IOException {
        playerService.nextStepForAllGames();

        verify(screenSender, never()).sendUpdates(screenSendCaptor.capture());
    }

    private void forceEmptyAllPlayerGlasses() {
        for (Glass glass : playerService.getGlasses()) {
            glass.empty();
        }
    }

    private Player createPlayer(String userName) {
        return playerService.addNewPlayer(userName, "http://" + userName + ":1234", null);
    }


    private List<Plot> getPlotsFor(Player vasya) {
        Map<Player, PlayerData> value = screenSendCaptor.getValue();
        return value.get(vasya).getPlots();
    }

    private void assertSentToPlayers(Player ... players) {
        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        Map sentScreens = screenSendCaptor.getValue();
        assertEquals(players.length, sentScreens.size());
        for (Player player : players) {
            assertTrue(sentScreens.containsKey(player));
        }
    }

    private void forceDropFigureInAllPlayerGlasses() {
        for (Glass glass : playerService.getGlasses()) {
             glass.drop(new TetrisFigure(), 0, HEIGHT);
        }
    }

    private void forceDropFigureInGlass(int x, int y, TetrisFigure point) {
        Glass glass = playerService.getGlasses().get(0);
        glass.drop(point, x, y);
    }

    private void assertHostsCaptured(String ... hostUrls) {
        assertEquals(hostUrls.length, playerCaptor.getAllValues().size());
        for (int i = 0; i < hostUrls.length; i++) {
            String hostUrl = hostUrls[i];
            assertEquals(hostUrl, playerCaptor.getAllValues().get(i).getCallbackUrl());
        }
    }

}
