package net.tetris.services;


import com.codenjoy.dojo.tetris.model.*;
import net.tetris.dom.*;
import com.codenjoy.dojo.tetris.model.MockLevels;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static com.codenjoy.dojo.tetris.model.TestUtils.HEIGHT;
import static com.codenjoy.dojo.tetris.model.TestUtils.assertContainsPlot;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TetrisPlayerService.class,
        MockScreenSenderConfiguration.class, MockPlayerController.class,
        MockGameSettingsService.class, MockGameSaver.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {
    private ArgumentCaptor<Map> screenSendCaptor;
    private ArgumentCaptor<Player> playerCaptor;
    private ArgumentCaptor<Integer> xCaptor;
    private ArgumentCaptor<Integer> yCaptor;
    private ArgumentCaptor<Figure.Type> figureCaptor;
    private ArgumentCaptor<List> plotsCaptor;
    private ArgumentCaptor<List> futureCaptor;

    @Autowired
    private PlayerService<Object> playerService;

    @Autowired
    private GameSettings gameSettings;

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController playerController;

    @Autowired
    private GameSaver saver;


    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        playerCaptor = ArgumentCaptor.forClass(Player.class);
        xCaptor = ArgumentCaptor.forClass(Integer.class);
        yCaptor = ArgumentCaptor.forClass(Integer.class);
        figureCaptor = ArgumentCaptor.forClass(Figure.Type.class);
        plotsCaptor = ArgumentCaptor.forClass(List.class);
        futureCaptor = ArgumentCaptor.forClass(List.class);

        playerService.clear();
        Mockito.reset(playerController, screenSender, saver);
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
                xCaptor.capture(), yCaptor.capture(), Matchers.<TetrisJoystik>any(), plotsCaptor.capture(), futureCaptor.capture());

        assertHostsCaptured("http://vasya:1234", "http://petya:1234");
    }

    @Test
    public void shouldRequestControlFromAllPlayersWithGlassState() throws IOException {
        createPlayer("vasya");
        forceDropFigureInGlass(0, HEIGHT, new TetrisFigure());

        playerService.nextStepForAllGames();

        verify(playerController).requestControl(playerCaptor.capture(), figureCaptor.capture(),
                xCaptor.capture(), yCaptor.capture(), Matchers.<TetrisJoystik>any(), plotsCaptor.capture(), futureCaptor.capture());
        List<Plot> sentPlots = plotsCaptor.getValue();
        assertEquals(1, sentPlots.size());
        assertContainsPlot(0, 0, PlotColor.BLUE, sentPlots);
        List<Figure.Type> futureFigures = futureCaptor.getValue();
        assertEquals(PlayerFigures.DEFAULT_FUTURE_COUNT, futureFigures.size());
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
                "Score:0, LinesRemoved:0, NextLevelIngoingCriteria:'Remove 4 lines together', CurrentLevel:1, Info:'Level 1']");

        expected.put("petya", "PlayerData[Plots:[Plot{x=4, y=19, color=BLUE}, Plot{x=4, y=18, color=BLUE}, Plot{x=4, y=17, color=BLUE}, Plot{x=4, y=16, color=BLUE}], " +
                "Score:0, LinesRemoved:0, NextLevelIngoingCriteria:'Remove 4 lines together', CurrentLevel:1, Info:'Level 1']");

        assertEquals(2, data.size());

        for (Map.Entry<Player, PlayerData> entry : data.entrySet()) {
            assertEquals(expected.get(entry.getKey().getName()), entry.getValue().toString());
        }
    }

    @Test
    public void shouldNewUserHasZerroScoresWhenLastLoggedIfOtherPlayerHasPositiveScores() {
        // given
        Player vasya = createPlayer("vasya");
        forceDropFigureInAllPlayerGlasses(); // vasia +drop

        // when
        Player petya = createPlayer("petya");

        // then
        assertEquals(0, petya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedIfSomePlayersHasNegativeScores() {
        // given
        Player vasya = createPlayer("vasya");
        forceDropFigureInAllPlayerGlasses(); // vasia +drop
        Player petya = createPlayer("petya");

        // when
        forceEmptyAllPlayerGlasses(); // vasia & petia -overflown
        Player katya = createPlayer("katya");

        // then
        assertEquals(petya.getScore(), katya.getScore());
        assertEquals(0, petya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedAfterNextStep() {
        // given
        Player vasya = createPlayer("vasya");
        forceDropFigureInAllPlayerGlasses(); // vasia +drop
        Player petya = createPlayer("petya");
        forceEmptyAllPlayerGlasses(); // vasia & petia -overflown
        Player katya = createPlayer("katya");

        // when
        playerService.nextStepForAllGames();
        Player olia = createPlayer("olia");

        // then
        assertEquals(olia.getScore(), katya.getScore());
        assertEquals(olia.getScore(), petya.getScore());
    }

    @Test
    public void shouldRemoveAllPlayerDataWhenRemovePlayerByIp() {
        // given
        createPlayer("vasya");
        createPlayer("petya");

        // when
        playerService.removePlayerByIp("http://vasya:1234");

        //then
        assertNull(playerService.findPlayer("vasya"));
        assertNotNull(playerService.findPlayer("petya"));
        assertEquals(1, playerService.getGlasses().size());
    }

    @Test
    public void shouldRemoveAllPlayerDataWhenRemovePlayerByName() {
        // given
        createPlayer("vasya");
        createPlayer("petya");

        // when
        playerService.removePlayerByIp("vasya");

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

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer() throws IOException {
        createPlayer("vasya");

        List games = field("games").ofType(List.class).in(playerService).get();
        TetrisAdvancedGame game = (TetrisAdvancedGame) games.get(0);

        checkInfo("Level 1");

        game.left(4);
        game.down();
        checkInfo("+1");

        game.left(3);
        game.down();
        checkInfo("+1");

        game.left(2);
        game.down();
        checkInfo("+1");

        game.left(1);
        game.down();
        checkInfo("+1");

        game.down();
        checkInfo("+1");

        game.right(1);
        game.down();
        checkInfo("+1");

        game.right(2);
        game.down();
        checkInfo("+1");

        game.right(3);
        game.down();
        checkInfo("+1");

        game.right(4);
        game.down();
        checkInfo("+1");

        game.right(5);
        game.down();
        checkInfo("+1, +100, Level 2");
    }

    private void checkInfo(String expected) {
        playerService.nextStepForAllGames();

        verify(screenSender, atLeast(1)).sendUpdates(screenSendCaptor.capture());
        Map<Player, PlayerData> data = screenSendCaptor.getValue();
        assertEquals(expected, data.entrySet().iterator().next().getValue().getInfo());
    }

    @Test
    public void shouldSavePlayerWhenExists() {
        Player player = createPlayer("vasia");

        playerService.savePlayerGame("vasia");

        verify(saver).saveGame(player);
    }

    @Test
    public void shouldNotSavePlayerWhenNotExists() {
        playerService.savePlayerGame("vasia");

        verifyNoMoreInteractions(saver);
    }

    @Test
    public void shouldCreatePlayerFromSavedPlayerGameWhenPlayerNotRegisterYet() {
        Player.PlayerBuilder playerBuilder = new Player.PlayerBuilder();
        playerBuilder.setCallbackUrl("url");
        playerBuilder.setInformation("info");
        PlayerFigures queue = new PlayerFigures();
        playerBuilder.forLevels(queue, new MockLevels(queue));
        playerBuilder.setName("vasia");
        playerBuilder.setScores(100);
        when(saver.loadGame("vasia")).thenReturn(playerBuilder);

        playerService.loadPlayerGame("vasia");

        Player player = playerService.findPlayerByIp("url");
        forceDropFigureInAllPlayerGlasses(); // +drop

        assertNotSame(NullPlayer.class, player.getClass());
        assertEquals(100 + TetrisPlayerScores.FIGURE_DROPPED_SCORE, player.getScore());
    }

    @Test
    public void shouldUpdatePlayerFromSavedPlayerGameWhenPlayerAlreadyRegistered() {
        Player registeredPlayer = createPlayer("vasia");
        forceDropFigureInAllPlayerGlasses(); // +drop
        assertEquals(TetrisPlayerScores.FIGURE_DROPPED_SCORE, registeredPlayer.getScore());

        Player.PlayerBuilder playerBuilder = new Player.PlayerBuilder();
        playerBuilder.setCallbackUrl("url");
        playerBuilder.setInformation("info");
        PlayerFigures queue = new PlayerFigures();
        playerBuilder.forLevels(queue, new MockLevels(queue));
        playerBuilder.setName("vasia");
        playerBuilder.setScores(100);
        when(saver.loadGame("vasia")).thenReturn(playerBuilder);

        playerService.loadPlayerGame("vasia");

        Player player = playerService.findPlayerByIp("url");

        assertNotSame(NullPlayer.class, player.getClass());
        assertEquals(100, player.getScore());
    }

    @Test
    public void shouldGetAllActivePlayersWithSavedGamesDataSortedByName() {
        createPlayer("activeSaved"); // check sorting order (activeSaved > active)
        createPlayer("active");

        when(saver.getSavedList()).thenReturn(Arrays.asList("activeSaved", "saved"));

        List<PlayerInfo> games = playerService.getPlayersGames();
        assertEquals(3, games.size());

        PlayerInfo active = games.get(0);
        PlayerInfo activeSaved = games.get(1);
        PlayerInfo saved = games.get(2);

        assertEquals("active", active.getName());
        assertEquals("http://active:1234", active.getCallbackUrl());
        assertTrue(active.isActive());
        assertFalse(active.isSaved());

        assertEquals("activeSaved", activeSaved.getName());
        assertEquals("http://activeSaved:1234", activeSaved.getCallbackUrl());
        assertTrue(activeSaved.isActive());
        assertTrue(activeSaved.isSaved());

        assertEquals("saved", saved.getName());
        assertNull(saved.getCallbackUrl());
        assertFalse(saved.isActive());
        assertTrue(saved.isSaved());
    }

}
