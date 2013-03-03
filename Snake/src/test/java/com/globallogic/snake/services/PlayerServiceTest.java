package com.globallogic.snake.services;
import com.globallogic.snake.model.Board;
import com.globallogic.snake.model.Joystick;
import com.globallogic.snake.model.Snake;
import com.globallogic.snake.model.Walls;
import com.globallogic.snake.model.artifacts.Apple;
import com.globallogic.snake.model.artifacts.ArtifactGenerator;
import com.globallogic.snake.model.artifacts.Stone;
import com.globallogic.snake.services.playerdata.PlayerData;
import com.globallogic.snake.services.playerdata.Plot;
import com.globallogic.snake.services.playerdata.PlotColor;
import com.globallogic.snake.services.playerdata.PlotsBuilder;
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

import static com.globallogic.snake.model.TestUtils.assertContainsPlot;
import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PlayerService.class,
        MockScreenSenderConfiguration.class,
        MockPlayerController.class,
        MockArtifactGenerator.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceTest {

    private ArgumentCaptor<Map> screenSendCaptor;
    private ArgumentCaptor<Player> playerCaptor;
    private ArgumentCaptor<Integer> xCaptor;
    private ArgumentCaptor<Integer> yCaptor;
    private ArgumentCaptor<List> plotsCaptor;
    private ArgumentCaptor<Board> boardCaptor;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController playerController;

    @Autowired
    private ArtifactGenerator artifactGenerator;

    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        playerCaptor = ArgumentCaptor.forClass(Player.class);
        xCaptor = ArgumentCaptor.forClass(Integer.class);
        yCaptor = ArgumentCaptor.forClass(Integer.class);
        plotsCaptor = ArgumentCaptor.forClass(List.class);
        boardCaptor = ArgumentCaptor.forClass(Board.class);

        playerService.clear();
        Mockito.reset(playerController, screenSender, artifactGenerator);
        setupArtifacts(1, 2, 2, 3);
    }

    @Test
    public void shouldSendCoordinatesToPlayerBoard() throws IOException {
        Player vasya = createPlayer("vasya");

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya);
        List<Plot> plots = getPlotsFor(vasya);
        assertContainsPlot(2, 3, PlotColor.STONE, plots);
        assertContainsPlot(1, 2, PlotColor.APPLE, plots);
        assertContainsPlot(8, 7, PlotColor.HEAD, plots);
        assertContainsPlot(7, 7, PlotColor.TAIL, plots);
    }

    @Test
    public void shouldRequestControlFromAllPlayers() throws IOException {
        Player vasya = createPlayer("vasya");
        Player petya = createPlayer("petya");

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya, petya);
        verify(playerController, times(2)).requestControl(playerCaptor.capture(), Matchers.<Joystick>any(), boardCaptor.capture());

        assertHostsCaptured("http://vasya:1234", "http://petya:1234");
    }

    @Test
    public void shouldRequestControlFromAllPlayersWithGlassState() throws IOException {
        createPlayer("vasya");

        playerService.nextStepForAllGames();

        verify(playerController).requestControl(playerCaptor.capture(), Matchers.<Joystick>any(), boardCaptor.capture());
        Board board = boardCaptor.getValue();
        List<Plot> sentPlots = new PlotsBuilder(board).get();
        // первая 4 - это змейка яблоко голова и хвост
        // за счет отсутствия дублирования в углах, периметр == (size - 1*4)
        assertEquals(4 + (board.getSize() - 1)*4, sentPlots.size());
        assertContainsPlot(2, 3, PlotColor.STONE, sentPlots);
        assertContainsPlot(1, 2, PlotColor.APPLE, sentPlots);
        assertContainsPlot(8, 7, PlotColor.HEAD, sentPlots);
        assertContainsPlot(7, 7, PlotColor.TAIL, sentPlots);
    }

    private void setupArtifacts(int appleX, int appleY, int stoneX, int stoneY) {
        when(artifactGenerator.generateApple(any(Snake.class), any(Apple.class), any(Stone.class), any(Walls.class), anyInt())).thenReturn(new Apple(appleX, appleY));
        when(artifactGenerator.generateStone(any(Snake.class), any(Apple.class), any(Walls.class), anyInt())).thenReturn(new Stone(stoneX, stoneY));
    }

    @Test
    public void shouldSendAdditionalInfoToAllPlayers() throws IOException {
        Player vasya = createPlayer("vasya");
        Player petya = createPlayer("petya");

        playerService.nextStepForAllGames();

        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        Map<Player, PlayerData> data = screenSendCaptor.getValue();

        String expectedString = "PlayerData[BoardSize:15, " +
                "Plots:[Plot{x=1, y=2, color=APPLE}, Plot{x=2, y=3, color=STONE}, " +
                "Plot{x=7, y=7, color=TAIL}, Plot{x=8, y=7, color=HEAD}, " +
                "Plot{x=0, y=0, color=WALL}, Plot{x=0, y=14, color=WALL}, " +
                "Plot{x=1, y=0, color=WALL}, Plot{x=1, y=14, color=WALL}, " +
                "Plot{x=2, y=0, color=WALL}, Plot{x=2, y=14, color=WALL}, " +
                "Plot{x=3, y=0, color=WALL}, Plot{x=3, y=14, color=WALL}, " +
                "Plot{x=4, y=0, color=WALL}, Plot{x=4, y=14, color=WALL}, " +
                "Plot{x=5, y=0, color=WALL}, Plot{x=5, y=14, color=WALL}, " +
                "Plot{x=6, y=0, color=WALL}, Plot{x=6, y=14, color=WALL}, " +
                "Plot{x=7, y=0, color=WALL}, Plot{x=7, y=14, color=WALL}, " +
                "Plot{x=8, y=0, color=WALL}, Plot{x=8, y=14, color=WALL}, " +
                "Plot{x=9, y=0, color=WALL}, Plot{x=9, y=14, color=WALL}, " +
                "Plot{x=10, y=0, color=WALL}, Plot{x=10, y=14, color=WALL}, " +
                "Plot{x=11, y=0, color=WALL}, Plot{x=11, y=14, color=WALL}, " +
                "Plot{x=12, y=0, color=WALL}, Plot{x=12, y=14, color=WALL}, " +
                "Plot{x=13, y=0, color=WALL}, Plot{x=13, y=14, color=WALL}, " +
                "Plot{x=14, y=0, color=WALL}, Plot{x=14, y=14, color=WALL}, " +
                "Plot{x=0, y=1, color=WALL}, Plot{x=14, y=1, color=WALL}, " +
                "Plot{x=0, y=2, color=WALL}, Plot{x=14, y=2, color=WALL}, " +
                "Plot{x=0, y=3, color=WALL}, Plot{x=14, y=3, color=WALL}, " +
                "Plot{x=0, y=4, color=WALL}, Plot{x=14, y=4, color=WALL}, " +
                "Plot{x=0, y=5, color=WALL}, Plot{x=14, y=5, color=WALL}, " +
                "Plot{x=0, y=6, color=WALL}, Plot{x=14, y=6, color=WALL}, " +
                "Plot{x=0, y=7, color=WALL}, Plot{x=14, y=7, color=WALL}, " +
                "Plot{x=0, y=8, color=WALL}, Plot{x=14, y=8, color=WALL}, " +
                "Plot{x=0, y=9, color=WALL}, Plot{x=14, y=9, color=WALL}, " +
                "Plot{x=0, y=10, color=WALL}, Plot{x=14, y=10, color=WALL}, " +
                "Plot{x=0, y=11, color=WALL}, Plot{x=14, y=11, color=WALL}, " +
                "Plot{x=0, y=12, color=WALL}, Plot{x=14, y=12, color=WALL}, " +
                "Plot{x=0, y=13, color=WALL}, Plot{x=14, y=13, color=WALL}], " +
                "Score:0, MaxLength:2, Length:2, CurrentLevel:1, Info:'']";

        Map<String, String> expected = new HashMap<>();
        expected.put("vasya", expectedString);
        expected.put("petya", expectedString);

        assertEquals(2, data.size());

        for (Map.Entry<Player, PlayerData> entry : data.entrySet()) {
            assertEquals(expected.get(entry.getKey().getName()), entry.getValue().toString());
        }
    }

    @Test
    public void shouldNewUserHasZerroScoresWhenLastLoggedIfOtherPlayerHasPositiveScores() {
        // given
        Player vasya = createPlayer("vasya");
        forceAllPlayerSnakesEatApple(); // vasia +10

        // when
        Player petya = createPlayer("petya");

        // then
        assertEquals(0, petya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedIfSomePlayersHasNegativeScores() {
        // given
        Player vasya = createPlayer("vasya");
        forceAllPlayerSnakesEatApple(); // vasia +3
        Player petya = createPlayer("petya");
        assertEquals(3, vasya.getScore());

        // when
        forceKillAllPlayerSnakes(); // vasia & petia -5
        Player katya = createPlayer("katya");

        // then
        assertEquals(petya.getScore(), katya.getScore());
        assertEquals(0, vasya.getScore());
        assertEquals(0, petya.getScore());
        assertEquals(0, katya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedAfterNextStep() {
        // given
        Player vasya = createPlayer("vasya");
        forceAllPlayerSnakesEatApple(); // vasia +10
        Player petya = createPlayer("petya");
        forceKillAllPlayerSnakes(); // vasia & petia -500
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
        assertEquals(1, playerService.getBoards().size());
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

    private void forceKillAllPlayerSnakes() {
        for (Board board : playerService.getBoards()) {
            board.getSnake().killMe();
        }
    }

    private Player createPlayer(String userName) {
        return playerService.addNewPlayer(userName, "http://" + userName + ":1234");
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

    private void forceAllPlayerSnakesEatApple() {
        for (Board board : playerService.getBoards()) {
            board.getSnake().grow();
        }
    }

    private void assertHostsCaptured(String ... hostUrls) {
        assertEquals(hostUrls.length, playerCaptor.getAllValues().size());
        for (int i = 0; i < hostUrls.length; i++) {
            String hostUrl = hostUrls[i];
            assertEquals(hostUrl, playerCaptor.getAllValues().get(i).getCallbackUrl());
        }
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_whenEatAppleAndStone() throws IOException {
        setupArtifacts(10, 7, 12, 7);  // snake head at 7,7
        createPlayer("vasya");

        List players = field("players").ofType(List.class).in(playerService).get();
        Player player = (Player) players.get(0);
        PlayerScores scores = field("scores").ofType(PlayerScores.class).in(player).get();
        field("score").ofType(int.class).in(scores).set(1000);

        checkInfo("");
        checkInfo("");
        checkInfo("+3");  // eat apple
        checkInfo("");
        checkInfo("-" +  + PlayerScores.EAT_STONE_PENALTY +
                ", -" +  + PlayerScores.GAME_OVER_PENALTY); // eat stone, gameover
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_whenEatAppleAndStone_manyScores() throws IOException {
        setupArtifacts(10, 7, 12, 7);  // snake head at 7,7
        createPlayer("vasya");

        checkInfo("");
        checkInfo("");
        checkInfo("+3");  // eat apple
        checkInfo("");
        checkInfo("-3"); // eat stone, gameover
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_whenEatWall() throws IOException {
        createPlayer("vasya");
        for (int count = 2; count <= 16; count ++) {
            forceAllPlayerSnakesEatApple(); // +2
        }

        List boards = field("boards").ofType(List.class).in(playerService).get();
        Board game = (Board) boards.get(0);

        game.getSnake().turnDown();
        checkInfo("+3, +4, +5, +6, +7, +8, +9, +10, +11, +12, +13, +14, +15, +16, +17");
        checkInfo("");
        checkInfo("");
        checkInfo("");
        checkInfo("");
        checkInfo("");
        checkInfo("-" + PlayerScores.GAME_OVER_PENALTY); // eatwall, gameover
    }

    private void checkInfo(String expected) {
        playerService.nextStepForAllGames();

        verify(screenSender, atLeast(1)).sendUpdates(screenSendCaptor.capture());
        Map<Player, PlayerData> data = screenSendCaptor.getValue();
        assertEquals(expected, data.entrySet().iterator().next().getValue().getInfo());
    }

}
