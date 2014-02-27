package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.mocks.*;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import com.codenjoy.dojo.transport.screen.ScreenSender;
import org.fest.reflect.core.Reflection;
import org.fest.reflect.field.Invoker;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static org.fest.reflect.core.Reflection.field;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {PlayerServiceImpl.class,
        MockScreenSenderConfiguration.class,
        MockChatService.class,
        MockPlayerControllerFactory.class,
        MockAutoSaver.class,
        MockSaveService.class,
        MockGameService.class,
        MockActionLogger.class,
        SpyPlayerGames.class,
        MockStatistics.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class PlayerServiceImplTest {

    public static final String VASYA_PASSWORD = "vasyapassword";
    public static final String PETYA_PASSWORD = "petyapassword";
    public static final String VASYA = "vasya";
    public static final String PETYA = "petya";
    public static final String KATYA = "katya";
    public static final String VASYA_URL = "http://vasya:1234";
    public static final String PETYA_URL = "http://petya:1234";
    public static final String VASYA_CODE = "1119795681432406325";
    private static final String PETYA_CDOE = "106558567952608766";

    private ArgumentCaptor<Map> screenSendCaptor;
    private ArgumentCaptor<Player> playerCaptor;
    private ArgumentCaptor<Integer> xCaptor;
    private ArgumentCaptor<Integer> yCaptor;
    private ArgumentCaptor<List> plotsCaptor;
    private ArgumentCaptor<String> boardCaptor;

    @Autowired private PlayerServiceImpl playerService;
    @Autowired private ScreenSender<ScreenRecipient, PlayerData> screenSender;
    @Autowired private PlayerControllerFactory playerControllerFactory;
    @Autowired private GameService gameService;
    @Autowired private ChatService chatService;
    @Autowired private AutoSaver autoSaver;
    @Autowired private ActionLogger actionLogger;
    @Autowired private PlayerGames playerGames;
    @Autowired private Statistics statistics;

    private GameType gameType;
    private PlayerScores playerScores1;
    private PlayerScores playerScores2;
    private PlayerScores playerScores3;
    private Game game;
    private Joystick joystick;
    private InformationCollector informationCollector;
    private PlayerController playerController;
    private PlayerSpy playerSpy;

    @Before
    @SuppressWarnings("all")
    public void setUp() throws IOException {
        Mockito.reset(actionLogger, autoSaver, chatService, gameService, playerControllerFactory, statistics);

        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        playerCaptor = ArgumentCaptor.forClass(Player.class);
        xCaptor = ArgumentCaptor.forClass(Integer.class);
        yCaptor = ArgumentCaptor.forClass(Integer.class);
        plotsCaptor = ArgumentCaptor.forClass(List.class);
        boardCaptor = ArgumentCaptor.forClass(String.class);

        playerScores1 = mock(PlayerScores.class);
        playerScores2 = mock(PlayerScores.class);
        playerScores3 = mock(PlayerScores.class);

        when(chatService.getChatLog()).thenReturn("chat");

        playerController = mock(PlayerController.class);
        when(playerControllerFactory.get(any(Protocol.class))).thenReturn(playerController);

        joystick = mock(Joystick.class);

        game = mock(Game.class);
        when(game.getJoystick()).thenReturn(joystick);
        when(game.getHero()).thenReturn(pt(1, 2), pt(3, 4), pt(5, 6), pt(7, 8));
        when(game.isGameOver()).thenReturn(false);

        gameType = mock(GameType.class);
        when(gameService.getGame(anyString())).thenReturn(gameType);

        when(gameType.getBoardSize()).thenReturn(v(15));
        when(gameType.getPlayerScores(anyInt())).thenReturn(playerScores1, playerScores2, playerScores3);
        when(gameType.newGame(any(InformationCollector.class))).thenReturn(game);
        when(gameType.gameName()).thenReturn("game");
        when(gameType.getPlots()).thenReturn(Elements.values());
        when(game.getBoardAsString()).thenReturn("1234");

        playerSpy = mock(PlayerSpy.class);
        when(statistics.newPlayer(any(Player.class))).thenReturn(playerSpy);

        playerGames.clear();
        Mockito.reset(playerController, screenSender, actionLogger);
        playerService.openRegistration();
    }

    enum Elements {
        A('1'), B('2'), C('3'), D('4');

        private final char ch;

        Elements(char c) {
            this.ch = c;
        }

        @Override
        public String toString() {
            return String.valueOf(ch);
        }
    }

    @Test
    public void shouldCreatePlayer() {
        createPlayer(VASYA);

        Player player = playerService.get(VASYA);

        assertEquals("game", player.getGameName());
        assertEquals(VASYA, player.getName());
        assertNull(player.getPassword());
        assertEquals(VASYA_CODE, player.getCode());
        assertEquals(VASYA_URL, player.getCallbackUrl());
        assertEquals(0, player.getCurrentLevel());
        assertSame(gameType, player.getGameType());
        assertEquals(Protocol.WS, player.getProtocol());
        assertNull(player.getMessage());
        assertEquals(0, player.getScore());
    }

    @Test
    public void shouldNotCreatePlayerWhenRegistrationWasClosed() {
        // given
        assertTrue(playerService.isRegistrationOpened());

        // when
        playerService.closeRegistration();

        // then
        Player player = createPlayer(VASYA);
        assertSame(Player.NULL, player);

        player = playerService.get(VASYA);
        assertSame(Player.NULL, player);

        assertFalse(playerService.isRegistrationOpened());

        // when
        playerService.openRegistration();

        // then
        assertTrue(playerService.isRegistrationOpened());

        player = createPlayer(VASYA);
        assertSame(VASYA, player.getName());

        player = playerService.get(VASYA);
        assertSame(VASYA, player.getName());
    }

    @Test
    public void shouldSendCoordinatesToPlayerBoard() throws IOException {
        Player vasia = createPlayer(VASYA);
        when(game.getBoardAsString()).thenReturn("1234");

        playerService.tick();

        assertSentToPlayers(vasia);
        assertEquals("ABCD", getBoardFor(vasia));
    }

    @Test
    public void shouldRequestControlFromAllPlayers() throws IOException {
        Player vasia = createPlayer(VASYA);
        Player petia = createPlayer(PETYA);

        playerService.tick();

        assertSentToPlayers(vasia, petia);
        verify(playerController, times(2)).requestControl(playerCaptor.capture(), anyString());

        assertHostsCaptured(VASYA_URL, PETYA_URL);
    }

    @Test
    public void shouldRequestControlFromAllPlayersWithGlassState() throws IOException {
        createPlayer(VASYA);
        when(game.getBoardAsString()).thenReturn("1234");

        playerService.tick();

        verify(playerController).requestControl(playerCaptor.capture(), boardCaptor.capture());
        assertEquals("1234", boardCaptor.getValue());
    }

    @Test
    public void shouldSendAdditionalInfoToAllPlayers() throws IOException {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        when(game.getBoardAsString())
                .thenReturn("1234")
                .thenReturn("4321");
        when(game.getCurrentScore()).thenReturn(8, 9);
        when(game.getMaxScore()).thenReturn(10, 11);
        when(playerScores1.getScore()).thenReturn(123);
        when(playerScores2.getScore()).thenReturn(234);

        // when
        playerService.tick();

        // then
        verify(screenSender).sendUpdates(screenSendCaptor.capture());
        Map<ScreenRecipient, PlayerData> data = screenSendCaptor.getValue();

        Map<String, String> expected = new HashMap<String, String>();
        expected.put(VASYA, "PlayerData[BoardSize:15, " +
                "Board:'ABCD', GameName:'game', Score:123, MaxLength:10, Length:8, CurrentLevel:1, Info:'', " +
                "ChatLog:'chat', Scores:'{\"vasya\":123,\"petya\":234}', Coordinates:'{\"vasya\":{\"y\":2,\"x\":1},\"petya\":{\"y\":4,\"x\":3}}']");

        expected.put(PETYA, "PlayerData[BoardSize:15, " +
                "Board:'DCBA', GameName:'game', Score:234, MaxLength:11, Length:9, CurrentLevel:1, Info:'', " +
                "ChatLog:'chat', Scores:'{\"vasya\":123,\"petya\":234}', Coordinates:'{\"vasya\":{\"y\":6,\"x\":5},\"petya\":{\"y\":8,\"x\":7}}']");

        assertEquals(2, data.size());

        for (Map.Entry<ScreenRecipient, PlayerData> entry : data.entrySet()) {
            assertEquals(expected.get(entry.getKey().toString()), entry.getValue().toString());
        }
    }

    @Test
    public void shouldNewUserHasZerroScoresWhenLastLoggedIfOtherPlayerHasPositiveScores() {
        // given
        Player vasya = createPlayer(VASYA);
        when(playerScores1.getScore()).thenReturn(10);

        // when
        Player petya = createPlayer(PETYA);

        // then
        assertEquals(0, petya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedIfSomePlayersHasNegativeScores() {
        // given
        Player vasya = createPlayer(VASYA);
        when(playerScores1.getScore()).thenReturn(10);

        Player petya = createPlayer(PETYA);
        assertEquals(10, vasya.getScore());
        assertEquals(0, petya.getScore());

        // when
        when(playerScores1.getScore()).thenReturn(5);
        when(playerScores2.getScore()).thenReturn(10);
        Player katya = createPlayer(KATYA);
        assertEquals(5, vasya.getScore());
        assertEquals(10, petya.getScore());

        assertEquals(0, katya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScoresWhenLastLoggedAfterNextStep() {
        // given
        Player vasya = createPlayer(VASYA);
        Player petya = createPlayer(PETYA);
        when(playerScores1.getScore()).thenReturn(5);
        when(playerScores2.getScore()).thenReturn(10);

        // when
        playerService.tick();

        Player katya = createPlayer(KATYA);

        // then
        assertEquals(5, vasya.getScore());
        assertEquals(10, petya.getScore());
        assertEquals(0, katya.getScore());
    }

    @Test
    public void shouldRemoveAllPlayerDataWhenRemovePlayer() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        playerService.remove(VASYA);

        //then
        assertEquals(Player.NULL, playerService.get(VASYA));
        assertNotSame(Player.NULL, playerService.get(PETYA));
        assertEquals(1, playerGames.size());
    }

    @Test
    public void shouldFindPlayerWhenGet() {
        // given
        Player newPlayer = createPlayer(VASYA);

        // when
        Player player = playerService.get(VASYA);

        //then
        assertSame(newPlayer, player);
    }

    @Test
    public void shouldGetNullPlayerWhenGetByNotExistsIp() {
        // given
        createPlayer(VASYA);

        // when
        Player player = playerService.get(KATYA);

        //then
        assertEquals(NullPlayer.class, player.getClass());
    }

    private Player createPlayer(String userName) {
        Player player = playerService.register(userName, userName + "password", getCallbackUrl(userName), userName + "game");

        if (player != Player.NULL) {
            ArgumentCaptor<InformationCollector> captor = ArgumentCaptor.forClass(InformationCollector.class);
            verify(gameType, atLeastOnce()).newGame(captor.capture());
            informationCollector = captor.getValue();
        }

        return player;
    }

    private String getCallbackUrl(String userName) {
        return "http://" + userName + ":1234";
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
    public void shouldCreatePlayerFromSavedPlayerGameWhenPlayerNotRegisterYet() {
        // given
        Player.PlayerBuilder playerBuilder =
                new Player.PlayerBuilder(VASYA, VASYA_PASSWORD,
                        getCallbackUrl(VASYA), "game", 100, "http");

        playerBuilder.setInformation("info");

        // when
        playerService.register(playerBuilder);

        // then
        verify(gameType).getPlayerScores(100);
        when(playerScores1.getScore()).thenReturn(100);

        Player player = playerService.get(VASYA);

        assertVasya(player);
        assertEquals(100, player.getScore());
        assertEquals("info", player.getMessage());
    }

    @Test
    public void shouldUpdatePlayerFromSavedPlayerGameWhenPlayerAlreadyRegistered() {
        // given
        Player registeredPlayer = createPlayer(VASYA);
        assertEquals(VASYA_URL, registeredPlayer.getCallbackUrl());

        Player.PlayerBuilder playerBuilder =
                new Player.PlayerBuilder(VASYA, VASYA_PASSWORD,
                        getCallbackUrl(VASYA), "game", 100, "http");
        playerBuilder.setInformation("info");

        // when
        playerService.register(playerBuilder);

        // then
        verify(gameType).getPlayerScores(100);
        when(playerScores2.getScore()).thenReturn(100);

        Player player = playerService.get(VASYA);

        assertVasya(player);
        assertEquals(100, player.getScore());
        assertEquals("info", player.getMessage());
    }

    private void assertVasya(Player player) {
        assertNotSame(NullPlayer.class, player.getClass());
        assertEquals(VASYA, player.getName());
        assertEquals(null, player.getPassword());
        assertEquals(VASYA_CODE, player.getCode());
        assertEquals(VASYA_URL, player.getCallbackUrl());
    }

    private void assertPetya(Player player) {
        assertNotSame(NullPlayer.class, player.getClass());
        assertEquals(PETYA, player.getName());
        assertEquals(null, player.getPassword());
        assertEquals(PETYA_CDOE, player.getCode());
        assertEquals(PETYA_URL, player.getCallbackUrl());
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifNoInfo() throws IOException {
        // given
        createPlayer(VASYA);

        // when, then
        checkInfo("");
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifPositiveValue() throws IOException {
        // given
        createPlayer(VASYA);

        // when, then
        when(playerScores1.getScore()).thenReturn(10, 13);
        informationCollector.levelChanged(1, null);
        informationCollector.event("event1");
        checkInfo("+3, Level 2");
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifNegativeValue() throws IOException {
        // given
        createPlayer(VASYA);

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
        createPlayer(VASYA);

        // when, then
        when(playerScores1.getScore()).thenReturn(10, 13);
        informationCollector.event("event1");
        checkInfo("+3");
    }

    private void checkInfo(String expected) {
        playerService.tick();

        verify(screenSender, atLeast(1)).sendUpdates(screenSendCaptor.capture());
        Map<ScreenRecipient, PlayerData> data = screenSendCaptor.getValue();
        Iterator<Map.Entry<ScreenRecipient, PlayerData>> iterator = data.entrySet().iterator();
        Map.Entry<ScreenRecipient, PlayerData> next = iterator.next();
        ScreenRecipient key = next.getKey();
        if (key.toString().equals("chatLog")) {   // потому что первый среди инфы о бзерах чат :)
            next = iterator.next();
        }
        assertEquals(expected, next.getValue().getInfo());
    }

    @Test
    public void shouldInformGameWhenUnregisterPlayer() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = mock(Game.class);
        Game game2 = mock(Game.class);
        setNewGames(game1, game2);

        playerService.removeAll();

        verify(game1).destroy();
        verify(game2).destroy();
    }

    @Test
    public void shouldTickForEachGamesWhenSeparateBordersGameType() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = mock(Game.class);
        Game game2 = mock(Game.class);

        setNewGames(game1, game2);

        setup(game1);
        setup(game2);

        when(gameType.isSingleBoardGame()).thenReturn(false);

        playerService.tick();

        verify(game1).tick();
        verify(game2).tick();
    }

    @Test
    public void shouldContinueTicksWhenException() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = mock(Game.class);
        Game game2 = mock(Game.class);

        setNewGames(game1, game2);

        setup(game1);
        setup(game2);
        doThrow(new RuntimeException()).when(game1).tick();

        when(gameType.isSingleBoardGame()).thenReturn(false);

        playerService.tick();

        verify(game1).tick();
        verify(game2).tick();
    }

    private void setNewGames(Game... games) {
        List<PlayerGame> list = field("playerGames").ofType(List.class).in(playerGames).get();
        for (int index = 0; index < list.size(); index++) {
            PlayerGame playerGame = list.get(index);

            field("game").ofType(Game.class).in(playerGame).set(games[index]);
        }
    }

    @Test
    public void shouldTickForOneGameWhenSingleBordersGameType() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = mock(Game.class);
        Game game2 = mock(Game.class);
        setNewGames(game1, game2);

        setup(game1);
        setup(game2);
        doThrow(new RuntimeException()).when(game1).tick();

        when(gameType.isSingleBoardGame()).thenReturn(true);   // тут отличия с прошлым тестом

        playerService.tick();

        verify(game1).tick();
        verify(game2, never()).tick();    // тут отличия с прошлым тестом
    }

    @Test
    public void shouldContinueTicksWhenExceptionInStatistics() {
        createPlayer(VASYA);

        Game game1 = mock(Game.class);

        setNewGames(game1);

        setup(game1);
        doThrow(new RuntimeException()).when(statistics).tick();

        playerService.tick();

        verify(game1).tick();
    }

    @Test
    public void shouldContinueTicksWhenExceptionInNewGame() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = mock(Game.class);
        Game game2 = mock(Game.class);
        setNewGames(game1, game2);

        setup(game1);
        setup(game2);

        when(game1.isGameOver()).thenReturn(true);
        doThrow(new RuntimeException()).when(game1).newGame();

        playerService.tick();

        verify(game1).tick();
        verify(game2).tick();
    }

    @Test
    public void shouldContinueTicksWhenExceptionInPlayerGameTick() {
        createPlayer(VASYA);

        Game game1 = mock(Game.class);
        setNewGames(game1);

        setup(game1);

        List list = Reflection.field("playerGames").ofType(List.class).in(playerGames).get();
        PlayerGame playerGame = (PlayerGame)list.remove(0);
        PlayerGame spy = spy(playerGame);
        list.add(spy);

        doThrow(new RuntimeException()).when(spy).tick();

        playerService.tick();

        verify(game1).tick();
    }

    @Test
    public void shouldContinueTicksWhenException_caseSingleBoardGame() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = mock(Game.class);
        Game game2 = mock(Game.class);
        setNewGames(game1, game2);

        setup(game1);
        setup(game2);

        when(gameType.isSingleBoardGame()).thenReturn(true);   // тут отличия с прошлым тестом

        playerService.tick();

        verify(game1).tick();
        verify(game2, never()).tick();    // тут отличия с прошлым тестом
    }

    @Test
    public void shouldJoystickWorkAfterFirstGameOver_lazyJoystick() throws IOException {
        // given
        createPlayer(VASYA);

        Joystick j = getJoystick(playerController);

        // when
        j.down();
        playerService.tick();

        // then
        verify(joystick).down();
        verifyNoMoreInteractions(joystick);

        Joystick joystick2 = mock(Joystick.class);
        when(game.isGameOver()).thenReturn(true);
        playerService.tick();
        verify(game).newGame();
        when(game.getJoystick()).thenReturn(joystick2);

        // when
        j.up();
        playerService.tick();

        // then
        verify(joystick2).up();
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldOnlyLastJoystickWorks_lazyJoystick() throws IOException {
        // given
        createPlayer(VASYA);

        Joystick j = getJoystick(playerController);

        // when
        j.down();
        j.up();
        j.left();
        j.right();
        verifyNoMoreInteractions(joystick);

        playerService.tick();

        // then
        verify(joystick).right();
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldFirstActWithDirection_lazyJoystick() throws IOException {
        // given
        createPlayer(VASYA);

        Joystick j = getJoystick(playerController);

        // when
        j.act(1, 2, 3);
        j.up();
        j.left();
        j.right();
        verifyNoMoreInteractions(joystick);

        playerService.tick();

        // then
        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).act(1, 2, 3);
        inOrder.verify(joystick).right();
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldLastActWithDirection_lazyJoystick() throws IOException {
        // given
        createPlayer(VASYA);

        Joystick j = getJoystick(playerController);

        // when
        j.right();
        j.left();
        j.up();
        j.act(5);
        j.act(5, 6);
        verifyNoMoreInteractions(joystick);

        playerService.tick();

        // then
        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).up();
        inOrder.verify(joystick).act(5, 6);
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldMixed_lazyJoystick() throws IOException {
        // given
        createPlayer(VASYA);

        Joystick j = getJoystick(playerController);

        // when
        j.right();
        j.left();
        j.up();
        j.act(5);
        j.act(5, 6);
        j.left();
        verifyNoMoreInteractions(joystick);

        playerService.tick();

        // then
        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).act(5, 6);
        inOrder.verify(joystick).left();
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldMixed2_lazyJoystick() throws IOException {
        // given
        createPlayer(VASYA);

        Joystick j = getJoystick(playerController);

        // when
        j.right();
        j.left();
        j.up();
        j.act(5);
        j.act(5, 6);
        j.left();
        j.act(7);
        verifyNoMoreInteractions(joystick);

        playerService.tick();

        // then
        InOrder inOrder = inOrder(joystick);
        inOrder.verify(joystick).left();
        inOrder.verify(joystick).act(7);
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldOnlyAct_lazyJoystick() throws IOException {
        // given
        createPlayer(VASYA);

        Joystick j = getJoystick(playerController);

        // when
        j.act();
        verifyNoMoreInteractions(joystick);

        playerService.tick();

        // then
        verify(joystick).act();
        verifyNoMoreInteractions(joystick);
    }

    private void setup(Game game) {
        when(game.getBoardAsString()).thenReturn("123");
        when(game.isGameOver()).thenReturn(false);
        when(game.getHero()).thenReturn(pt(0, 0));
    }

    @Test
    public void shouldGetAll() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        List<Player> all = playerService.getAll();

        assertEquals(2, all.size());
        Player player1 = all.get(0);
        Player player2 = all.get(1);

        assertVasya(player1);
        assertEquals(0, player1.getScore());
        assertNull(player1.getMessage());

        assertPetya(player2);
        assertEquals(0, player2.getScore());
        assertNull(player2.getMessage());
    }

    @Test
    public void shouldContains() {
        createPlayer(VASYA);

        assertTrue(playerService.contains(VASYA));
        assertFalse(playerService.contains(PETYA));
    }

    @Test
    public void shouldGetJoystick() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        assertSame(joystick, playerService.getJoystick(VASYA));
        assertSame(joystick, playerService.getJoystick(PETYA));
        assertSame(Joystick.NULL, playerService.getJoystick(KATYA));
    }

    @Test
    public void shouldCleanAllScores() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        playerService.cleanAllScores();

        verify(playerScores1).clear();
        verify(playerScores2).clear();
        verifyNoMoreInteractions(playerScores3);

        verify(game, times(2)).newGame();
        verify(game, times(2)).clearScore();
    }

    @Test
    public void shouldGetByCode() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        assertEquals(VASYA, playerService.getByCode(VASYA_CODE).getName());
        assertEquals(PETYA, playerService.getByCode(PETYA_CDOE).getName());
        assertEquals(Player.NULL, playerService.getByCode("qwe"));
    }

    @Test
    public void shouldGetRandom_other() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        assertEquals(VASYA, playerService.getRandom(gameType.gameName()).getName());
    }

    @Test
    public void shouldLogin() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        assertTrue(playerService.login(VASYA, VASYA_PASSWORD));
        assertTrue(playerService.login(PETYA, PETYA_PASSWORD));

        assertFalse(playerService.login(PETYA, "qwe"));
        assertFalse(playerService.login(VASYA, null));
    }

    @Test
    public void shouldUpdateAll_whenNullInfos() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        playerService.updateAll(null);

        List<Player> all = playerService.getAll();
        assertVasyaAndPetya(all);
    }

    @Test
    public void shouldUpdateAll_mainCase() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        List<PlayerInfo> infos = new LinkedList<PlayerInfo>();
        infos.add(new PlayerInfo("new-vasya", "new-pass1", "new-url1", "new-game"));
        infos.add(new PlayerInfo("new-petya", "new-pass2", "new-url2", "new-game"));
        playerService.updateAll(infos);

        List<Player> all = playerService.getAll();
        assertUpdatedVasyaAndPetya(all);
    }

    private void assertUpdatedVasyaAndPetya(List<Player> all) {
        assertEquals("[new-vasya, new-petya]", all.toString());

        Player player1 = all.get(0);
        assertEquals("new-url1", player1.getCallbackUrl());
        assertEquals(VASYA_CODE, player1.getCode());
        assertEquals("game", player1.getGameName());
        assertEquals(null, player1.getPassword());

        Player player2 = all.get(1);
        assertEquals("new-url2", player2.getCallbackUrl());
        assertEquals(PETYA_CDOE, player2.getCode());
        assertEquals("game", player1.getGameName());
        assertEquals(null, player2.getPassword());
    }

    @Test
    public void shouldUpdateAll_removeNullUsers() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        List<PlayerInfo> infos = new LinkedList<PlayerInfo>();
        infos.add(new PlayerInfo("new-vasya", "new-pass1", "new-url1", "new-game"));
        infos.add(new PlayerInfo("new-petya", "new-pass2", "new-url2", "new-game"));
        infos.add(new PlayerInfo(null, "new-pass2", "new-url2", "new-game"));
        playerService.updateAll(infos);

        List<Player> all = playerService.getAll();
        assertUpdatedVasyaAndPetya(all);
    }

    @Test
    public void shouldUpdateAll_exceptionIfCountUsersNotEqual() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        List<PlayerInfo> infos = new LinkedList<PlayerInfo>();
        infos.add(new PlayerInfo("new-vasya", "new-pass1", "new-url1", "new-game"));

        try {
            playerService.updateAll(infos);
            fail();
        } catch (Exception e) {
            assertEquals("java.lang.IllegalArgumentException: Diff players count", e.toString());
        }

        List<Player> all = playerService.getAll();
        assertVasyaAndPetya(all);
    }

    private void assertVasyaAndPetya(List<Player> all) {
        assertEquals("[vasya, petya]", all.toString());

        Player player1 = all.get(0);
        assertEquals(VASYA_URL, player1.getCallbackUrl());
        assertEquals(VASYA_CODE, player1.getCode());
        assertEquals(null, player1.getPassword());

        Player player2 = all.get(1);
        assertEquals(PETYA_URL, player2.getCallbackUrl());
        assertEquals(PETYA_CDOE, player2.getCode());
        assertEquals(null, player2.getPassword());
    }

    @Test
    public void shouldLogActionsOnTick() {
        createPlayer(VASYA);

        playerService.tick();

//        verify(actionLogger).log(playerGames);
        verifyNoMoreInteractions(actionLogger);
    }

    @Test
    public void shouldTickStatistics() {
        createPlayer(VASYA);

        playerService.tick();

        verify(statistics).tick();
    }

    @Test
    public void shouldActPlayerSpyWhenActAtJoystick() {
        // given
        createPlayer(VASYA);
        PlayerController controller = playerGames.get(VASYA).getController();
        Joystick j = getJoystick(controller);

        // when
        j.down();
        playerService.tick();

        // then
        verify(playerSpy).act();
        verifyNoMoreInteractions(playerSpy);
    }

    private Joystick getJoystick(PlayerController controller) {
        ArgumentCaptor<Joystick> joystickCaptor = ArgumentCaptor.forClass(Joystick.class);
        verify(controller).registerPlayerTransport(any(Player.class), joystickCaptor.capture());
        return joystickCaptor.getValue();
    }

}
