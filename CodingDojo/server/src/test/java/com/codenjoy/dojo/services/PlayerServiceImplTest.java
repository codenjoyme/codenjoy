package com.codenjoy.dojo.services;

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
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.controller.PlayerController;
import com.codenjoy.dojo.services.controller.ScreenController;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hero.HeroDataImpl;
import com.codenjoy.dojo.services.lock.LockedJoystick;
import com.codenjoy.dojo.services.mocks.AISolverStub;
import com.codenjoy.dojo.services.mocks.BoardStub;
import com.codenjoy.dojo.services.multiplayer.*;
import com.codenjoy.dojo.services.nullobj.NullJoystick;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.GraphicPrinter;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import com.codenjoy.dojo.transport.screen.ScreenSender;
import lombok.SneakyThrows;
import org.fest.reflect.core.Reflection;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static java.util.stream.Collectors.toList;
import static org.fest.reflect.core.Reflection.field;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CodenjoyContestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
public class PlayerServiceImplTest {

    public static final String VASYA = "vasya";
    public static final String VASYA_AI = "vasya-super-ai";
    public static final String PETYA = "petya";
    public static final String KATYA = "katya";
    public static final String OLIA = "olia";
    public static final String VASYA_URL = "http://vasya:1234";
    public static final String PETYA_URL = "http://petya:1234";
    public static final String KATYA_URL = "http://katya:1234";

    private ArgumentCaptor<Map> screenSendCaptor;
    private ArgumentCaptor<Player> playerCaptor;
    private ArgumentCaptor<Integer> xCaptor;
    private ArgumentCaptor<Integer> yCaptor;
    private ArgumentCaptor<List> plotsCaptor;
    private ArgumentCaptor<String> boardCaptor;

    @MockBean
    private ScreenSender screenSender;

    @MockBean
    private PlayerController playerController;

    @MockBean
    private ScreenController screenController;

    @MockBean
    private AutoSaver autoSaver;

    @MockBean
    private SaveService saveService;

    @MockBean
    private Registration registration;

    @MockBean
    private GameService gameService;

    @MockBean
    private Chat chat;

    @MockBean
    private SemifinalService semifinal;

    @MockBean
    private ActionLogger actionLogger;

    @SpyBean
    private PlayerGames playerGames;

    @MockBean
    protected RoomService roomService;

    @SpyBean
    private PlayerGamesView playerGamesView;

    @Autowired
    private PlayerServiceImpl playerService;

    @Mock
    private GameType gameType;
    
    private InformationCollector informationCollector;
    
    @Mock
    private GraphicPrinter printer;
    private List<Joystick> joysticks = new LinkedList<>();
    private List<GamePlayer> gamePlayers = new LinkedList<>();
    private List<GameField> gameFields = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private List<PlayerHero> heroesData = new LinkedList<>();
    private List<PlayerScores> playerScores = new LinkedList<>();
    private Map<String, Integer> chatIds = new HashMap<>();

    @Before
    public void setUp() {
        Mockito.reset(actionLogger, autoSaver, gameService, playerController, playerGames);
        playerGames.clean();

        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        playerCaptor = ArgumentCaptor.forClass(Player.class);
        xCaptor = ArgumentCaptor.forClass(Integer.class);
        yCaptor = ArgumentCaptor.forClass(Integer.class);
        plotsCaptor = ArgumentCaptor.forClass(List.class);
        boardCaptor = ArgumentCaptor.forClass(String.class);

        when(printer.print(any(), any())).thenReturn("1234");

        when(gameService.getGameType(anyString())).thenReturn(gameType);
        when(gameService.getGameType(anyString(), anyString())).thenReturn(gameType);
        when(gameService.exists(anyString())).thenReturn(true);

        when(chat.getLastMessageIds()).thenReturn(chatIds);

        when(gameType.getBoardSize(any())).thenReturn(v(15));
        when(gameType.getPlayerScores(anyInt(), any())).thenAnswer(inv -> {
            PlayerScores scores = mock(PlayerScores.class);
            when(scores.getScore()).thenReturn(0);
            playerScores.add(scores);
            return scores;
        });
        when(gameType.createGame(anyInt(), any())).thenAnswer(inv -> {
            GameField field = mock(GameField.class);
            gameFields.add(field);

            when(field.reader()).thenReturn(mock(BoardReader.class));
            return field;
        });
        heroesData.addAll(Arrays.asList(heroData(1, 2), heroData(3, 4), heroData(5, 6), heroData(7, 8)));
        when(gameType.createPlayer(any(EventListener.class), anyString(), any()))
                .thenAnswer(inv -> {
                    Joystick joystick = mock(Joystick.class);
                    joysticks.add(joystick);

                    GamePlayer gamePlayer = mock(GamePlayer.class);
                    gamePlayers.add(gamePlayer);

                    when(gamePlayer.getJoystick()).thenReturn(joystick);
                    when(gamePlayer.getHero()).thenReturn(heroesData.get(gamePlayers.size() - 1));
                    when(gamePlayer.isAlive()).thenReturn(true);
                    return gamePlayer;
                });
        when(gameType.name()).thenReturn("game");
        when(gameType.getPlots()).thenReturn(Elements.values());
        when(gameType.getPrinterFactory()).thenReturn(PrinterFactory.get(printer));
        when(gameType.getMultiplayerType(any())).thenReturn(MultiplayerType.SINGLE);

        // по умолчанию все команаты будут активными и открытыми для регистрации
        when(roomService.isActive(anyString())).thenReturn(true);
        when(roomService.isOpened(anyString())).thenReturn(true);

        doAnswer(inv -> {
            String id = inv.getArgument(0);
            return "readable_" + id;
        }).when(registration).getNameById(anyString());

        playerGames.clear();
        Mockito.reset(playerController, screenController, actionLogger);
        playerService.openRegistration();

        playerService.init();
    }

    private PlayerHero heroData(int x, int y) {
        return new PlayerHero(pt(x, y)) {
            @Override
            public void down() {

            }

            @Override
            public void up() {

            }

            @Override
            public void left() {

            }

            @Override
            public void right() {

            }

            @Override
            public void act(int... p) {

            }

            @Override
            public void tick() {

            }
        };
    }

    enum Elements implements CharElements {
        A('1'), B('2'), C('3'), D('4');

        private final char ch;

        Elements(char c) {
            this.ch = c;
        }

        @Override
        public String toString() {
            return String.valueOf(ch);
        }

        @Override
        public char ch() {
            return ch;
        }
    }

    @Test
    public void shouldCreatePlayer() {
        // given
        createPlayer(VASYA);

        // when
        Player player = playerService.get(VASYA);

        // then
        assertEquals("game", player.getGame());
        assertEquals(VASYA, player.getId());
        assertNull(player.getPassword());
        assertNull(player.getCode());
        assertEquals(VASYA_URL, player.getCallbackUrl());
        assertSame(gameType, player.getGameType());
        assertNull(player.getMessage());
        assertEquals(0, player.getScore());
    }

    @Test
    public void shouldNotCreatePlayer_whenRegistrationWasClosed() {
        // given
        assertTrue(playerService.isRegistrationOpened());

        // when
        playerService.closeRegistration();

        // then
        assertNotCreated(createPlayer(VASYA));
        assertNotCreated(playerService.get(VASYA));

        assertFalse(playerService.isRegistrationOpened());

        // when
        playerService.openRegistration();

        // then
        assertTrue(playerService.isRegistrationOpened());

        assertCreated(createPlayer(VASYA));
        assertSame(VASYA, playerService.get(VASYA).getId());
    }

    @Test
    public void shouldSendCoordinatesToPlayerBoard() {
        // given
        Player vasia = createPlayer(VASYA);
        when(printer.print(any(), any())).thenReturn("1234");

        // when
        playerService.tick();

        // then
        assertSentToPlayers(vasia);
        assertEquals("ABCD", getBoardFor(vasia));
    }

    @Test
    public void shouldSendPlayerBoardFromJsonBoard() {
        // givnm
        Player vasia = createPlayer(VASYA);
        when(printer.print(any(), any()))
                .thenReturn(new JSONObject("{'layers':['1234','4321']}"));

        // when
        playerService.tick();

        // then
        assertSentToPlayers(vasia);
        assertEquals("{\"layers\":[\"ABCD\",\"DCBA\"]}", getBoardFor(vasia));
    }

    @Test
    public void shouldRequestControl_fromAllPlayers() {
        // given
        Player vasia = createPlayer(VASYA);
        Player petia = createPlayer(PETYA);

        // when
        playerService.tick();

        // then
        assertSentToPlayers(vasia, petia);
        assertHostsCaptured(VASYA_URL, PETYA_URL);
    }

    protected void setActive(String room, boolean active) {
        when(roomService.isActive(room)).thenReturn(active);
    }

    protected void setRegistrationOpened(String room, boolean opened) {
        when(roomService.isOpened(room)).thenReturn(opened);
    }

    @Test
    public void shouldNotCreateUsers_forRoomWhereRegistrationIsClosed_case1() {
        // given
        setRegistrationOpened("room1", false);

        // when
        assertNotCreated(createPlayer(VASYA, "game1", "room1"));
        assertNotCreated(createPlayer(PETYA, "game1", "room1"));
        assertCreated(createPlayer(KATYA, "game1", "room2"));
        assertCreated(createPlayer(OLIA, "game3", "room3"));

        // then
        assertPlayers("[katya, olia]");

        // when
        setRegistrationOpened("room1", true);

        assertCreated(createPlayer(VASYA, "game1", "room1"));
        assertCreated(createPlayer(PETYA, "game1", "room1"));

        // then
        assertPlayers("[katya, olia, vasya, petya]");
    }

    @Test
    public void shouldNotCreateUsers_forRoomWhereRegistrationIsClosed_case2() {
        // given
        setRegistrationOpened("room2", false);

        // when
        assertCreated(createPlayer(VASYA, "game1", "room1"));
        assertCreated(createPlayer(PETYA, "game1", "room1"));
        assertNotCreated(createPlayer(KATYA, "game1", "room2"));
        assertCreated(createPlayer(OLIA, "game3", "room3"));

        // then
        assertPlayers("[vasya, petya, olia]");

        // when
        setRegistrationOpened("room2", true);

        assertCreated(createPlayer(KATYA, "game1", "room2"));

        // then
        assertPlayers("[vasya, petya, olia, katya]");
    }

    private void assertCreated(Player player) {
        assertNotSame(NullPlayer.INSTANCE, player);
    }

    private void assertNotCreated(Player player) {
        assertSame(NullPlayer.INSTANCE, player);
    }

    @Test
    public void shouldNotCreateUsers_forRoomWhereRegistrationIsClosed_case3() {
        // given
        setRegistrationOpened("room1", false);
        setRegistrationOpened("room3", false);

        // when
        assertNotCreated(createPlayer(VASYA, "game1", "room1"));
        assertNotCreated(createPlayer(PETYA, "game1", "room1"));
        assertCreated(createPlayer(KATYA, "game1", "room2"));
        assertNotCreated(createPlayer(OLIA, "game3", "room3"));

        // then
        assertPlayers("[katya]");

        // when
        setRegistrationOpened("room1", true);
        setRegistrationOpened("room3", true);

        assertCreated(createPlayer(VASYA, "game1", "room1"));
        assertCreated(createPlayer(PETYA, "game1", "room1"));
        assertCreated(createPlayer(OLIA, "game3", "room3"));

        // then
        assertPlayers("[katya, vasya, petya, olia]");
    }

    @Test
    public void shouldRequestControl_fromAllPlayers_skipNonActiveRooms() {
        // given
        Player vasia = createPlayer(VASYA, "game1", "room1");
        Player petia = createPlayer(PETYA, "game1", "room2");
        Player katya = createPlayer(KATYA, "game2", "room3");

        setActive("room1", false);

        // when
        playerService.tick();

        // then
        assertSentToPlayers(vasia, petia, katya);
        assertHostsCaptured(PETYA_URL, KATYA_URL);
    }

    @Test
    public void shouldRequestControl_fromAllPlayers_withGlassState() {
        // given
        createPlayer(VASYA);
        when(printer.print(any(), any())).thenReturn("1234");

        // when
        playerService.tick();

        // then
        verify(playerController).requestControl(playerCaptor.capture(), boardCaptor.capture());
        assertEquals("1234", boardCaptor.getValue());
    }

    @Test
    public void shouldSendAdditionalInfoToAllPlayers() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        when(printer.print(any(), any()))
                .thenReturn("1234")
                .thenReturn("4321");
        when(playerScores(0).getScore()).thenReturn(123);
        when(playerScores(1).getScore()).thenReturn(234);

        // when
        playerService.tick();

        // then
        verify(screenController).requestControlToAll(screenSendCaptor.capture());
        Map<ScreenRecipient, Object> data = screenSendCaptor.getValue();

        assertEquals(
                "{petya=PlayerData[" +
                    "BoardSize:15, Board:'DCBA', Game:'game', " +
                    "Score:234, Info:'', " +
                    "Scores:'{petya=234}', " +
                    "Coordinates:'{petya=HeroDataImpl(level=0, coordinate=[3,4], isMultiplayer=false, additionalData=null)}', " +
                    "ReadableNames:'{petya=readable_petya}', " +
                    "Group:[petya], " +
                    "LastChatMessage:106558567], " +
                "vasya=PlayerData[" +
                    "BoardSize:15, Board:'ABCD', Game:'game', " +
                    "Score:123, Info:'', " +
                    "Scores:'{vasya=123}', " +
                    "Coordinates:'{vasya=HeroDataImpl(level=0, coordinate=[1,2], isMultiplayer=false, additionalData=null)}', " +
                    "ReadableNames:'{vasya=readable_vasya}', " +
                    "Group:[vasya], " +
                    "LastChatMessage:111979568]}",
                data.toString().replaceAll("\"", "'"));
    }

    @Test
    public void shouldNewUserHasZeroScores_whenLastLogged_ifOtherPlayerHasPositiveScores() {
        // given
        Player vasya = createPlayer(VASYA);
        when(playerScores(0).getScore()).thenReturn(10);

        // when
        Player petya = createPlayer(PETYA);

        // then
        assertEquals(0, petya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScores_whenLastLogged_ifSomePlayersHasNegativeScores() {
        // given
        Player vasya = createPlayer(VASYA);
        when(playerScores(0).getScore()).thenReturn(10);

        Player petya = createPlayer(PETYA);
        assertEquals(10, vasya.getScore());
        assertEquals(0, petya.getScore());

        // when
        when(playerScores(0).getScore()).thenReturn(5);
        when(playerScores(1).getScore()).thenReturn(10);
        Player katya = createPlayer(KATYA);
        assertEquals(5, vasya.getScore());
        assertEquals(10, petya.getScore());

        assertEquals(0, katya.getScore());
    }

    @Test
    public void shouldNewUserHasMinimumPlayersScores_whenLastLogged_afterNextStep() {
        // given
        Player vasya = createPlayer(VASYA);
        Player petya = createPlayer(PETYA);
        when(playerScores(0).getScore()).thenReturn(5);
        when(playerScores(1).getScore()).thenReturn(10);

        // when
        playerService.tick();

        Player katya = createPlayer(KATYA);

        // then
        assertEquals(5, vasya.getScore());
        assertEquals(10, petya.getScore());
        assertEquals(0, katya.getScore());
    }

    @Test
    public void shouldRemoveAllPlayerData_whenRemovePlayer() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        playerService.remove(VASYA);

        //then
        assertEquals(NullPlayer.INSTANCE, playerService.get(VASYA));
        assertCreated(playerService.get(PETYA));
        assertEquals(1, playerGames.size());
    }

    @Test
    public void shouldFindPlayer_whenGet() {
        // given
        Player newPlayer = createPlayer(VASYA);

        // when
        Player player = playerService.get(VASYA);

        //then
        assertSame(newPlayer, player);
    }

    @Test
    public void shouldGetNullPlayer_whenGetByNotExistsIp() {
        // given
        createPlayer(VASYA);

        // when
        Player player = playerService.get(KATYA);

        //then
        assertEquals(NullPlayer.class, player.getClass());
    }

    private Player createPlayer(String id) {
        return createPlayer(id, id + " game", id + " room");
    }

    private Player createPlayer(String id, String game, String room) {
        Player player = playerService.register(id, game, room,
                getCallbackUrl(id));
        if (player == NullPlayer.INSTANCE) {
            return player;
        }

        players.add(player);
        chatIds.put(room, Math.abs(id.hashCode()));

        if (player != NullPlayer.INSTANCE) {
            verify(gameType, atLeastOnce()).createGame(anyInt(), any());
        }

        return player;
    }

    private String getCallbackUrl(String id) {
        return "http://" + id + ":1234";
    }

    private String getBoardFor(Player player) {
        Map sentScreens = screenSendCaptor.getValue();
        Map<Player, PlayerData> value = sentScreens;
        return value.get(player).getBoard().toString();
    }

    @SneakyThrows
    private void assertSentToPlayers(Player ... players) {
        verify(screenController).requestControlToAll(screenSendCaptor.capture());
        Map sentScreens1 = screenSendCaptor.getValue();
        Map sentScreens = sentScreens1;
        assertEquals(players.length, sentScreens.size());
        for (Player player : players) {
            assertTrue(sentScreens.containsKey(player));
        }
    }

    @SneakyThrows
    private void assertHostsCaptured(String ... hostUrls) {
        verify(playerController, times(hostUrls.length)).requestControl(playerCaptor.capture(), anyString());

        assertEquals(hostUrls.length, playerCaptor.getAllValues().size());
        for (int i = 0; i < hostUrls.length; i++) {
            String hostUrl = hostUrls[i];
            assertEquals(hostUrl, playerCaptor.getAllValues().get(i).getCallbackUrl());
        }
    }

    @Test
    public void shouldCreatePlayerFromSavedPlayerGame_whenPlayerNotRegisterYet() {
        // given
        PlayerSave save = new PlayerSave(VASYA, getCallbackUrl(VASYA), "game", "room", 100, null);

        // when
        playerService.register(save);

        // then
        verify(gameType).getPlayerScores(eq(100), any());
        when(playerScores(0).getScore()).thenReturn(100);

        Player player = playerService.get(VASYA);

        assertVasya(player);
        assertEquals(100, player.getScore());
        assertEquals(null, player.getMessage());
    }

    @Test
    public void shouldUpdatePlayerFromSavedPlayerGame_whenPlayerAlreadyRegistered_whenOtherGameType() {
        // given
        Player registeredPlayer = createPlayer(VASYA);
        assertEquals(VASYA_URL, registeredPlayer.getCallbackUrl());

        PlayerSave save = new PlayerSave(VASYA, getCallbackUrl(VASYA), "other_game", "other_room", 200, null);

        // when
        playerService.register(save);

        // then
        verify(gameType).getPlayerScores(eq(200), any());
        when(playerScores(1).getScore()).thenReturn(200);

        Player player = playerService.get(VASYA);

        assertVasya(player);
        assertEquals(200, player.getScore());
        assertEquals(null, player.getMessage());
    }

    @Test
    public void shouldNotUpdatePlayerFromSavedPlayerGame_whenPlayerAlreadyRegistered_whenSameGameType() {
        // given
        Player registeredPlayer = createPlayer(VASYA);
        assertEquals(VASYA_URL, registeredPlayer.getCallbackUrl());
        assertEquals(0, registeredPlayer.getScore());

        PlayerSave save = new PlayerSave(VASYA, getCallbackUrl(VASYA), "game", "room", 200, null);

        // when
        playerService.register(save);

        // then
        verify(gameType).getPlayerScores(eq(0), any());
        when(playerScores(1).getScore()).thenReturn(0);

        Player player = playerService.get(VASYA);

        assertVasya(player);
        assertEquals(0, player.getScore());
        assertEquals(null, player.getMessage());
    }

    private void assertVasya(Player player) {
        assertNotSame(NullPlayer.class, player.getClass());
        assertEquals(VASYA, player.getId());
        assertEquals(null, player.getPassword());
        assertEquals(null, player.getCode());
        assertEquals(VASYA_URL, player.getCallbackUrl());
    }

    private void assertPetya(Player player) {
        assertNotSame(NullPlayer.class, player.getClass());
        assertEquals(PETYA, player.getId());
        assertEquals(null, player.getPassword());
        assertNull(player.getCode());
        assertEquals(PETYA_URL, player.getCallbackUrl());
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifNoInfo() {
        // given
        createPlayer(VASYA);

        // when, then
        checkInfo("");
    }
    
    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifPositiveValue() {
        // given
        informationCollector = createPlayer(VASYA).getEventListener();

        // when, then
        when(playerScores(0).getScore()).thenReturn(10, 13);
        informationCollector.levelChanged(new LevelProgress(3, 2, 1));
        informationCollector.event("event1");
        checkInfo("+3, Level 2");
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifNegativeValue() {
        // given
        informationCollector = createPlayer(VASYA).getEventListener();

        // when, then
        when(playerScores(0).getScore()).thenReturn(10, 9);
        informationCollector.event("event1");
        when(playerScores(0).getScore()).thenReturn(10, 8);
        informationCollector.event("event2");
        checkInfo("-1, -2");
    }

    @Test
    public void shouldSendScoresAndLevelUpdateInfoInfoToPlayer_ifAdditionalInfo() {
        // given
        informationCollector = createPlayer(VASYA).getEventListener();

        // when, then
        when(playerScores(0).getScore()).thenReturn(10, 13);
        informationCollector.event("event1");
        checkInfo("+3");
    }

    @SneakyThrows
    private void checkInfo(String expected) {
        playerService.tick();

        verify(screenController, atLeast(1)).requestControlToAll(screenSendCaptor.capture());
        Map sentScreens = screenSendCaptor.getValue();
        Map<ScreenRecipient, PlayerData> data = sentScreens;
        Iterator<Map.Entry<ScreenRecipient, PlayerData>> iterator = data.entrySet().iterator();
        Map.Entry<ScreenRecipient, PlayerData> next = iterator.next();
        ScreenRecipient key = next.getKey();
        assertEquals(expected, next.getValue().getInfo());
    }

    @Test
    public void shouldInformGame_whenUnregisterPlayer() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));
        setNewGames(game1, game2);

        // when
        playerService.removeAll();

        // then
        verify(game1).close();
        verify(game2).close();
    }

    @Test
    public void shouldRemoveAll() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        playerService.removeAll();

        // then
        assertPlayers("[]");
    }

    @Test
    public void shouldRemoveAll_forRoom() {
        // given
        createPlayer(VASYA, "game1", "room1");
        createPlayer(PETYA, "game1", "room1");
        createPlayer(KATYA, "game1", "room2");
        createPlayer(OLIA, "game3", "room3");

        // when
        playerService.removeAll("room1");

        // then
        assertPlayers("[katya, olia]");
    }

    private void assertPlayers(String expected) {
        assertEquals(expected,
                playerService.getAll().stream()
                        .map(Player::getId)
                        .collect(toList())
                        .toString());
    }

    @Test
    public void shouldTickForEachGames_whenSeparateBordersGameType() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));

        setNewGames(game1, game2);

        setup(game1);
        setup(game2);

        when(gameType.getMultiplayerType(any())).thenReturn(MultiplayerType.SINGLE);

        // when
        playerService.tick();

        // then
        verify(game1.getField()).quietTick();
        verify(game2.getField()).quietTick();
    }

    private Game createGame(GameField gameField) {
        Game game = mock(Game.class);
        when(game.getField()).thenReturn(gameField);
        return game;
    }

    @Test
    public void shouldContinueTicks_whenException() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));

        setNewGames(game1, game2);

        setup(game1);
        setup(game2);
        GameField field1 = game1.getField();
        GameField field2 = game2.getField();
        doThrow(new RuntimeException()).when(field1).tick();

        when(gameType.getMultiplayerType(any())).thenReturn(MultiplayerType.SINGLE);

        // when
        playerService.tick();

        // then
        verify(field1).quietTick();
        verify(field2).quietTick();
    }

    private void setNewGames(Game... games) {
        List<PlayerGame> list = getPlayerGames();
        for (int index = 0; index < list.size(); index++) {
            PlayerGame playerGame = list.get(index);

            field("game").ofType(Game.class).in(playerGame).set(games[index]);
        }
    }

    private List<PlayerGame> getPlayerGames() {
        return field(PlayerGames.Fields.all).ofType(List.class).in(playerGames).get();
    }

    @Test
    public void shouldTickForOneGame_whenSingleBordersGameType() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));
        setNewGames(game1, game2);

        setup(game1);
        setup(game2);
        GameField field1 = game1.getField();
        when(game2.getField()).thenReturn(field1);
        doThrow(new RuntimeException()).when(field1).tick();

        when(gameType.getMultiplayerType(any())).thenReturn(MultiplayerType.MULTIPLE);   // тут отличия с прошлым тестом

        // when
        playerService.tick();

        // then
        verify(field1, times(1)).quietTick();
    }

    @Test
    public void shouldContinueTicks_whenExceptionInNewGame() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));
        setNewGames(game1, game2);

        setup(game1);
        setup(game2);

        when(game1.isGameOver()).thenReturn(true);
        doThrow(new RuntimeException()).when(game1).newGame();

        // when
        playerService.tick();

        // then
        verify(game1.getField()).quietTick();
        verify(game2.getField()).quietTick();
    }

    @Test
    public void shouldContinueTicks_whenExceptionInPlayerGameTick() {
        // given
        createPlayer(VASYA);

        Game game1 = createGame(gameField(VASYA));
        setNewGames(game1);

        setup(game1);

        List list = Reflection.field(PlayerGames.Fields.all).ofType(List.class).in(playerGames).get();
        PlayerGame playerGame = (PlayerGame)list.remove(0);
        PlayerGame spy = spy(playerGame);
        list.add(spy);

        doThrow(new RuntimeException()).when(spy).tick();

        // when
        playerService.tick();

        // then
        verify(game1.getField()).quietTick();
    }

    @Test
    public void shouldContinueTicks_whenException_caseMultiplayer() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));
        setNewGames(game1, game2);

        setup(game1);
        setup(game2);

        when(gameType.getMultiplayerType(any())).thenReturn(MultiplayerType.MULTIPLE); // тут отличия с прошлым тестом
        GameField field1 = game1.getField();
        when(game2.getField()).thenReturn(field1);

        // when
        playerService.tick();

        // then
        verify(field1, times(1)).quietTick();
    }

    @Test
    public void shouldJoystickWork_afterFirstGameOver_lazyJoystick() {
        // given
        createPlayer(VASYA);

        verify(gameField(VASYA)).newGame(gamePlayer(VASYA));
        reset(gameField(VASYA));

        Joystick j = getJoystick(playerController);

        // when
        j.down();
        playerService.tick();

        // then
        verify(joystick(VASYA)).down();
        verifyNoMoreInteractions(joystick(VASYA));

        // when
        when(gamePlayer(VASYA).isAlive()).thenReturn(false);
        playerService.tick();
        verify(gameField(VASYA)).newGame(gamePlayer(VASYA));

        Joystick joystick2 = mock(Joystick.class);
        when(gamePlayer(VASYA).getJoystick()).thenReturn(joystick2);

        // when
        j.up();
        playerService.tick();

        // then
        verify(joystick2).up();
        verifyNoMoreInteractions(joystick(VASYA));
    }

    private Joystick joystick(String player) {
        return joysticks.get(getIndexOf(player));
    }

    private GamePlayer gamePlayer(String player) {
        return gamePlayers.get(getIndexOf(player));
    }

    private GameField gameField(String player) {
        return gameFields.get(getIndexOf(player));
    }

    private int getIndexOf(String player) {
        Player found = players.stream()
                .filter(p -> p.getId().equals(player))
                .findFirst()
                .orElse(null);
        int indexOf = players.indexOf(found);
        if (indexOf == -1) {
            throw new IllegalArgumentException("Player не найден");
        }
        return indexOf;
    }


    @Test
    public void shouldAllJoystickCommandsWorks_lazyJoystick() {
        // given
        createPlayer(VASYA);

        Joystick j = getJoystick(playerController);

        // when
        j.down();
        j.up();
        j.left();
        j.right();
        verifyNoMoreInteractions(joystick(VASYA));

        playerService.tick();

        // then
        Joystick joystick = joystick(VASYA);
        InOrder inOrder = inOrder(joystick);

        inOrder.verify(joystick).down();
        inOrder.verify(joystick(VASYA)).up();
        inOrder.verify(joystick(VASYA)).left();
        inOrder.verify(joystick(VASYA)).right();
        verifyNoMoreInteractions(joystick(VASYA));
    }

    @Test
    public void shouldFirstActWithDirection_lazyJoystick() {
        // given
        createPlayer(VASYA);
        Joystick joystick = joystick(VASYA);

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
        inOrder.verify(joystick).up();
        inOrder.verify(joystick).left();
        inOrder.verify(joystick).right();
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldLastActWithDirection_lazyJoystick() {
        // given
        createPlayer(VASYA);
        Joystick joystick = joystick(VASYA);

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
        inOrder.verify(joystick).right();
        inOrder.verify(joystick).left();
        inOrder.verify(joystick).up();
        inOrder.verify(joystick).act(5);
        inOrder.verify(joystick).act(5, 6);
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldMixed_lazyJoystick() {
        // given
        createPlayer(VASYA);
        Joystick joystick = joystick(VASYA);

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
        inOrder.verify(joystick).right();
        inOrder.verify(joystick).left();
        inOrder.verify(joystick).up();
        inOrder.verify(joystick).act(5);
        inOrder.verify(joystick).act(5, 6);
        inOrder.verify(joystick).left();
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldMixed2_lazyJoystick() {
        // given
        createPlayer(VASYA);
        Joystick joystick = joystick(VASYA);

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
        inOrder.verify(joystick).right();
        inOrder.verify(joystick).left();
        inOrder.verify(joystick).up();
        inOrder.verify(joystick).act(5);
        inOrder.verify(joystick).act(5, 6);
        inOrder.verify(joystick).left();
        inOrder.verify(joystick).act(7);
        verifyNoMoreInteractions(joystick);
    }

    @Test
    public void shouldOnlyAct_lazyJoystick() {
        // given
        createPlayer(VASYA);
        Joystick joystick = joystick(VASYA);

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
        when(game.getHero()).thenReturn(new HeroDataImpl(pt(0, 0),
                MultiplayerType.SINGLE.isSingleplayer()));
    }

    @Test
    public void shouldGetAll() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        List<Player> all = playerService.getAll();

        // then
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
        // given
        createPlayer(VASYA);

        // when then
        assertTrue(playerService.contains(VASYA));
        assertFalse(playerService.contains(PETYA));
    }

    @Test
    public void shouldGetJoystick() {
        // given
        createPlayer(VASYA);
        Joystick joystick1 = joystick(VASYA);
        createPlayer(PETYA);
        Joystick joystick2 = joystick(PETYA);

        // when then
        assertSame(joystick1, ((LockedJoystick)playerService.getJoystick(VASYA)).getWrapped());
        assertSame(joystick2, ((LockedJoystick)playerService.getJoystick(PETYA)).getWrapped());
        assertSame(NullJoystick.INSTANCE, playerService.getJoystick(KATYA));
    }

    @Test
    public void shouldNewGame_whenCreatePlayer() {
        // given when
        createPlayer(VASYA);
        createPlayer(PETYA);

        // then
        verify(gameField(VASYA)).newGame(any());
        verify(gameField(PETYA)).newGame(any());

        assertEquals(2, gameFields.size());
        assertEquals(2, playerScores.size());
    }

    @Test
    public void shouldCleanAllScores() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);
        
        // when
        playerService.cleanAllScores();

        // then
        verify(playerScores(0)).clear();
        verify(playerScores(1)).clear();

        verify(gameField(VASYA)).clearScore();
        verify(gameField(PETYA)).clearScore();

        verify(semifinal).clean();
    }

    @Test
    public void shouldCleanAllScores_forRoom() {
        // given
        createPlayer(VASYA, "game1", "room1");
        createPlayer(PETYA, "game1", "room1");
        createPlayer(KATYA, "game1", "room2");
        createPlayer(OLIA, "game3", "room3");

        // when
        playerService.cleanAllScores("room1");

        // then
        verify(playerScores(0)).clear();
        verify(playerScores(1)).clear();
        verifyNoMoreInteractions(playerScores(2));
        verifyNoMoreInteractions(playerScores(3));

        verify(gameField(VASYA)).clearScore();
        verify(gameField(PETYA)).clearScore();
        verify(gameField(KATYA), never()).clearScore();
        verify(gameField(OLIA), never()).clearScore();

        verify(semifinal).clean("room1");
    }

    private PlayerScores playerScores(int index) {
        return playerScores.get(index);
    }

    @Test
    public void shouldGetRandom_other() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        assertEquals(VASYA, playerService.getRandom(gameType.name()).getId());
    }

    @Test
    public void shouldUpdateAll_whenNullInfos() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        playerService.updateAll(null);

        // then
        List<Player> all = playerService.getAll();
        assertVasyaAndPetya(all);
    }

    @Test
    public void shouldUpdateAll_mainCase() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        List<PlayerInfo> infos = new LinkedList<>();
        infos.add(new PlayerInfo("new-vasya", "new-pass1", "new-url1", "new-game"));
        infos.add(new PlayerInfo("new-petya", "new-pass2", "new-url2", "new-game"));
        playerService.updateAll(infos);

        // then
        List<Player> all = playerService.getAll();
        assertUpdatedVasyaAndPetya(all);
    }

    @Test
    public void shouldSendPlayerNameToGame() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when then
        // TODO implement
    }

    private void assertUpdatedVasyaAndPetya(List<Player> all) {
        assertEquals("[new-vasya, new-petya]", all.toString());

        Player player1 = all.get(0);
        assertEquals("new-url1", player1.getCallbackUrl());
        assertNull(player1.getCode());
        assertEquals("game", player1.getGame());
        assertEquals(null, player1.getPassword());

        Player player2 = all.get(1);
        assertEquals("new-url2", player2.getCallbackUrl());
        assertNull(player2.getCode());
        assertEquals("game", player1.getGame());
        assertEquals(null, player2.getPassword());
    }

    @Test
    public void shouldUpdateAll_removeNullUsers() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        List<PlayerInfo> infos = new LinkedList<>();
        infos.add(new PlayerInfo("new-vasya", "new-pass1", "new-url1", "new-game"));
        infos.add(new PlayerInfo("new-petya", "new-pass2", "new-url2", "new-game"));
        infos.add(new PlayerInfo(null, "new-pass2", "new-url2", "new-game"));
        playerService.updateAll(infos);

        // then
        List<Player> all = playerService.getAll();
        assertUpdatedVasyaAndPetya(all);
    }

    @Test
    public void shouldUpdateAll_exceptionIfCountUsersNotEqual() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        List<PlayerInfo> infos = new LinkedList<>();
        infos.add(new PlayerInfo("new-vasya", "new-pass1", "new-url1", "new-game"));

        try {
            // when
            playerService.updateAll(infos);
            fail();
        } catch (Exception e) {
            // then
            assertEquals("java.lang.IllegalArgumentException: Diff players count", e.toString());
        }

        List<Player> all = playerService.getAll();
        assertVasyaAndPetya(all);
    }

    private void assertVasyaAndPetya(List<Player> all) {
        assertEquals("[vasya, petya]", all.toString());

        Player player1 = all.get(0);
        assertEquals(VASYA_URL, player1.getCallbackUrl());
        assertNull(player1.getCode());
        assertEquals(null, player1.getPassword());

        Player player2 = all.get(1);
        assertEquals(PETYA_URL, player2.getCallbackUrl());
        assertNull(player2.getCode());
        assertEquals(null, player2.getPassword());
    }

    @Test
    public void shouldUpdateAll_loadFromSave() {
        // given
        Player player1 = createPlayer(VASYA);
        Player player2 = createPlayer(PETYA);

        // when
        List<PlayerInfo> infos = new LinkedList<PlayerInfo>(){{
            add(new PlayerInfo(player1){{
                setData("{\"some\":\"data1\"}");
            }});
            add(new PlayerInfo(player2){{
                setData("{\"some\":\"data2\"}");
            }});
        }};
        playerService.updateAll(infos);

        // then
        assertSaveLoaded(player1, "[{\"some\":\"data1\"}]");
        assertSaveLoaded(player2, "[{\"some\":\"data2\"}]");
    }

    private void assertSaveLoaded(Player player, String save) {
        ArgumentCaptor<JSONObject> captor = ArgumentCaptor.forClass(JSONObject.class);
        verify(playerGames).setLevel(eq(player.getId()), captor.capture());
        assertEquals(save, captor.getAllValues().toString());
    }

    @Test
    public void shouldUpdateAll_loadFromSave_onlyIfSaveIsNotSame() {
        // given
        Player player1 = createPlayer(VASYA);
        Player player2 = createPlayer(PETYA);

        // when
        List<PlayerInfo> infos = new LinkedList<PlayerInfo>(){{
            add(new PlayerInfo(player1){{
                setData("{\"some\":\"data1\"}");
            }});
            add(new PlayerInfo(player2){{
                setData("{}"); // same
            }});
        }};
        playerService.updateAll(infos);

        // then
        assertSaveLoaded(player1, "[{\"some\":\"data1\"}]");
        assertSaveNotLoaded(player2);
    }

    @Test
    public void shouldUpdateAll_loadFromSave_onlyIfSaveIsNotEmptyOrNull() {
        // given
        Player player1 = createPlayer(VASYA);
        Player player2 = createPlayer(PETYA);
        Player player3 = createPlayer(KATYA);
        Player player4 = createPlayer(OLIA);

        // when
        List<PlayerInfo> infos = new LinkedList<PlayerInfo>(){{
            add(new PlayerInfo(player1){{
                setData("{\"some\":\"data1\"}");
            }});
            add(new PlayerInfo(player2){{
                setData(""); // empty
            }});
            add(new PlayerInfo(player3){{
                setData(null); // null
            }});
            add(new PlayerInfo(player4){{
                setData("null"); // "null"
            }});
        }};
        playerService.updateAll(infos);

        // then
        assertSaveLoaded(player1, "[{\"some\":\"data1\"}]");
        assertSaveNotLoaded(player2);
        assertSaveNotLoaded(player3);
        assertSaveNotLoaded(player4);
    }

    private void assertSaveNotLoaded(Player player) {
        verify(playerGames, never()).setLevel(eq(player.getId()), any(JSONObject.class));
    }

    @Test
    public void shouldLogActionsOnTick() {
        // given
        createPlayer(VASYA);

        // when
        playerService.tick();

        // then
        verify(actionLogger).log(playerGames);
//        verifyNoMoreInteractions(actionLogger);
    }

    @Test
    public void shouldTickSemifinal_whenTick() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        playerService.tick();

        // then
        verify(semifinal, only()).tick();
    }

    private Joystick getJoystick(Controller controller) {
        ArgumentCaptor<Joystick> joystickCaptor = ArgumentCaptor.forClass(Joystick.class);
        verify(controller).registerPlayerTransport(any(Player.class), joystickCaptor.capture());
        return joystickCaptor.getValue();
    }

    @Test
    public void testReloadAI() {
        // given
        WebSocketRunner.ATTEMPTS = 0;
        WebSocketRunner.TIMEOUT = 100;

        when(gameType.getAI()).thenReturn((Class)AISolverStub.class);
        when(gameType.getBoard()).thenReturn((Class)BoardStub.class);

        String game = createPlayer(VASYA).getGame();

        verify(gameType, times(1)).getAI();
        verify(gameType, times(1)).getBoard();

        // when
        playerService.reloadAI(VASYA);

        // then
        verify(gameType, times(2)).getAI();
        verify(gameType, times(2)).getBoard();

        PlayerGame playerGame = playerGames.get(VASYA);
        assertEquals(game, playerGame.getPlayer().getGame());
        Player player = playerGame.getPlayer();
        assertEquals(VASYA, player.getId());
        assertNotNull(VASYA, player.getAi());
    }

    @Test
    public void testLoadPlayersFromSaveAndLoadAI() {
        // given
        when(gameType.getAI()).thenReturn((Class)AISolverStub.class);
        when(gameType.getBoard()).thenReturn((Class)BoardStub.class);
        PlayerSave save = new PlayerSave(VASYA_AI, getCallbackUrl(VASYA_AI), "game", "room", 100, null);

        // when
        playerService.register(save);

        // then
        verify(gameType).getAI();
        verify(gameType).getBoard();

        PlayerGame playerGame = playerGames.get(VASYA_AI);
        assertEquals("game", playerGame.getPlayer().getGame());
        Player player = playerGame.getPlayer();
        assertEquals(VASYA_AI, player.getId());
        assertNotNull(VASYA, player.getAi());
    }
}