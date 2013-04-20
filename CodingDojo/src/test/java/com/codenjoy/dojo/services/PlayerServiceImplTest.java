package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.playerdata.PlayerData;
import org.fest.reflect.field.Invoker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.*;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PlayerServiceImpl.class,
        MockScreenSenderConfiguration.class,
        MockPlayerController.class,
        MockGameSaver.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceImplTest {

    private ArgumentCaptor<Map> screenSendCaptor;
    private ArgumentCaptor<Player> playerCaptor;
    private ArgumentCaptor<Integer> xCaptor;
    private ArgumentCaptor<Integer> yCaptor;
    private ArgumentCaptor<List> plotsCaptor;
    private ArgumentCaptor<String> boardCaptor;

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private ScreenSender screenSender;

    @Autowired
    private PlayerController playerController;

    private GameType gameType;
    private PlayerScores playerScores1;
    private PlayerScores playerScores2;
    private PlayerScores playerScores3;
    private Game game;
    private Joystick joystick;
    private InformationCollector informationCollector;
    private GameSaver saver;

    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        playerCaptor = ArgumentCaptor.forClass(Player.class);
        xCaptor = ArgumentCaptor.forClass(Integer.class);
        yCaptor = ArgumentCaptor.forClass(Integer.class);
        plotsCaptor = ArgumentCaptor.forClass(List.class);
        boardCaptor = ArgumentCaptor.forClass(String.class);

        playerScores1 = mock(PlayerScores.class);
        playerScores2 = mock(PlayerScores.class);
        playerScores3 = mock(PlayerScores.class);

        joystick = mock(Joystick.class);

        saver = mock(GameSaver.class);

        game = mock(Game.class);
        when(game.getJoystick()).thenReturn(joystick);
        when(game.isGameOver()).thenReturn(false);

        gameType = mock(GameType.class);
        playerService.setGameType(gameType, saver); // TODO fixme
        when(gameType.getBoardSize()).thenReturn(15);
        when(gameType.getPlayerScores(anyInt())).thenReturn(playerScores1, playerScores2, playerScores3);
        when(gameType.newGame(any(InformationCollector.class))).thenReturn(game);
        when(gameType.getPlots()).thenReturn(Arrays.asList(1,2,3,4).toArray());
        when(game.getBoardAsString()).thenReturn("1234");
        playerService.setGameType(gameType, saver);

        playerService.clean();
        Mockito.reset(playerController, screenSender);
    }

    @Test
    public void shouldSendCoordinatesToPlayerBoard() throws IOException {
        Player vasya = createPlayer("vasya");
        when(game.getBoardAsString()).thenReturn("1234");

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya);
        assertEquals("ABCD", getBoardFor(vasya));
    }

    @Test
    public void shouldRequestControlFromAllPlayers() throws IOException {
        Player vasya = createPlayer("vasya");
        Player petya = createPlayer("petya");

        playerService.nextStepForAllGames();

        assertSentToPlayers(vasya, petya);
        verify(playerController, times(2)).requestControl(playerCaptor.capture(), Matchers.<Joystick>any(), anyString());

        assertHostsCaptured("http://vasya:1234", "http://petya:1234");
    }

    @Test
    public void shouldRequestControlFromAllPlayersWithGlassState() throws IOException {
        createPlayer("vasya");
        when(game.getBoardAsString()).thenReturn("1234");

        playerService.nextStepForAllGames();

        verify(playerController).requestControl(playerCaptor.capture(), Matchers.<Joystick>any(), boardCaptor.capture());
        assertEquals("1234", boardCaptor.getValue());
    }

    @Test
    public void shouldSendAdditionalInfoToAllPlayers() throws IOException {
        // given
        createPlayer("vasya");
        createPlayer("petya");

        when(game.getBoardAsString())
                .thenReturn("1234")
                .thenReturn("4321");
        when(game.getCurrentScore()).thenReturn(8, 9);
        when(game.getMaxScore()).thenReturn(10, 11);
        when(playerScores1.getScore()).thenReturn(123);
        when(playerScores2.getScore()).thenReturn(234);

        // when
        playerService.nextStepForAllGames();

        // then
        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        Map<Player, PlayerData> data = screenSendCaptor.getValue();

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("vasya", "PlayerData[BoardSize:15, " +
                "Board:'ABCD', Score:123, MaxLength:10, Length:8, CurrentLevel:1, Info:'']");

        expected.put("petya", "PlayerData[BoardSize:15, " +
                "Board:'DCBA', Score:234, MaxLength:11, Length:9, CurrentLevel:1, Info:'']");

        assertEquals(2, data.size());

        for (Map.Entry<Player, PlayerData> entry : data.entrySet()) {
            assertEquals(expected.get(entry.getKey().getName()), entry.getValue().toString());
        }
    }

    @Test
    public void shouldNewUserHasZerroScoresWhenLastLoggedIfOtherPlayerHasPositiveScores() {
        // given
        Player vasya = createPlayer("vasya");
        when(playerScores1.getScore()).thenReturn(10);

        // when
        Player petya = createPlayer("petya");

        // then
        assertEquals(0, petya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedIfSomePlayersHasNegativeScores() {
        // given
        Player vasya = createPlayer("vasya");
        when(playerScores1.getScore()).thenReturn(10);

        Player petya = createPlayer("petya");
        assertEquals(10, vasya.getScore());
        assertEquals(0, petya.getScore());

        // when
        when(playerScores1.getScore()).thenReturn(0);
        when(playerScores2.getScore()).thenReturn(-10);
        Player katya = createPlayer("katya");

        // then
        verify(gameType).getPlayerScores(-10);
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedAfterNextStep() {
        // given
        Player vasya = createPlayer("vasya");
        Player petya = createPlayer("petya");

        // when
        when(playerScores1.getScore()).thenReturn(0);
        when(playerScores2.getScore()).thenReturn(-10);

        playerService.nextStepForAllGames();

        Player katya = createPlayer("katya");

        // then
        verify(gameType).getPlayerScores(-10);
    }

    @Test
    public void shouldRemoveAllPlayerDataWhenRemovePlayer() {
        // given
        createPlayer("vasya");
        createPlayer("petya");

        // when
        playerService.removePlayerByIp("http://vasya:1234");

        //then
        assertNull(playerService.findPlayer("vasya"));
        assertNotNull(playerService.findPlayer("petya"));
        assertEquals(1, getGames().size());
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

    private Player createPlayer(String userName) {
        Player player = playerService.addNewPlayer(userName, "http://" + userName + ":1234");

        ArgumentCaptor<InformationCollector> captor = ArgumentCaptor.forClass(InformationCollector.class);
        verify(gameType, atLeastOnce()).newGame(captor.capture());
        informationCollector = captor.getValue();

        return player;
    }

    private String getBoardFor(Player vasya) {
        Map<Player, PlayerData> value = screenSendCaptor.getValue();
        return value.get(vasya).getBoard();
    }

    private void assertSentToPlayers(Player ... players) {
        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        Map sentScreens = screenSendCaptor.getValue();
        assertEquals(players.length, sentScreens.size());
        for (Player player : players) {
            assertTrue(sentScreens.containsKey(player));
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
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifNoInfo() throws IOException {
        // given
        createPlayer("vasya");

        // when, then
        checkInfo("");
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifPositiveValue() throws IOException {
        // given
        createPlayer("vasya");

        // when, then
        when(playerScores1.getScore()).thenReturn(10, 13);
        informationCollector.levelChanged(1, null);
        informationCollector.event("event1");
        checkInfo("+3, Level 2");
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifNegativeValue() throws IOException {
        // given
        createPlayer("vasya");

        // when, then
        when(playerScores1.getScore()).thenReturn(10, 9);
        informationCollector.event("event1");
        when(playerScores1.getScore()).thenReturn(10, 8);
        informationCollector.event("event2");
        checkInfo("-1, -2");
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifAdditionalInfo() throws IOException {
        // given
        createPlayer("vasya");

        // when, then
        when(playerScores1.getScore()).thenReturn(10, 13);
        informationCollector.event("event1");
        checkInfo("+3");
    }

    private List<Game> getGames() {
        return games().get();
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
        // given
        Player.PlayerBuilder playerBuilder = new Player.PlayerBuilder("vasia", "url", 100);
        playerBuilder.setInformation("info");
        when(saver.loadGame("vasia")).thenReturn(playerBuilder);

        // when
        playerService.loadPlayerGame("vasia");

        // then
        verify(gameType).getPlayerScores(100);
        when(playerScores1.getScore()).thenReturn(100);

        Player player = playerService.findPlayerByIp("url");

        assertNotSame(NullPlayer.class, player.getClass());
        assertEquals("vasia", player.getName());
        assertEquals("url", player.getCallbackUrl());
        assertEquals(100, player.getScore());
        assertEquals("info", player.getMessage());
    }

    @Test
    public void shouldUpdatePlayerFromSavedPlayerGameWhenPlayerAlreadyRegistered() {
        // given
        Player registeredPlayer = createPlayer("vasia");
        assertEquals("http://vasia:1234", registeredPlayer.getCallbackUrl());

        Player.PlayerBuilder playerBuilder = new Player.PlayerBuilder("vasia", "url", 100);
        playerBuilder.setInformation("info");
        when(saver.loadGame("vasia")).thenReturn(playerBuilder);

        // when
        playerService.loadPlayerGame("vasia");

        // then
        verify(gameType).getPlayerScores(100);
        when(playerScores2.getScore()).thenReturn(100);

        Player player = playerService.findPlayerByIp("url");

        assertNotSame(NullPlayer.class, player.getClass());
        assertEquals("vasia", player.getName());
        assertEquals("url", player.getCallbackUrl());
        assertEquals(100, player.getScore());
        assertEquals("info", player.getMessage());
    }

    @Test
    public void shouldGetAllActivePlayersWithSavedGamesDataSortedByName() {
        // given
        createPlayer("activeSaved"); // check sorting order (activeSaved > active)
        createPlayer("active");

        when(saver.getSavedList()).thenReturn(Arrays.asList("activeSaved", "saved"));

        // when
        List<PlayerInfo> games = playerService.getPlayersGames();

        // then
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

    @Test
    public void shouldInformGameWhenUnregisterPlayer() {
        createPlayer("vasia");
        createPlayer("petia");

        Game game1 = mock(Game.class);
        Game game2 = mock(Game.class);
        List<Game> games = new LinkedList<Game>();
        games.add(game1);
        games.add(game2);
        games().set(games);

        playerService.removeAll();

        verify(game1).destroy();
        verify(game2).destroy();
    }

    private Invoker<List> games() {
        return field("games").ofType(List.class).in(playerService);
    }

}
