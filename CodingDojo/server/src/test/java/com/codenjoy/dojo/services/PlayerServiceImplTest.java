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
import com.codenjoy.dojo.services.dao.DealSaver;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hero.HeroDataImpl;
import com.codenjoy.dojo.services.joystick.NoActJoystick;
import com.codenjoy.dojo.services.joystick.NoDirectionJoystick;
import com.codenjoy.dojo.services.lock.LockedJoystick;
import com.codenjoy.dojo.services.mocks.AISolverStub;
import com.codenjoy.dojo.services.mocks.BoardStub;
import com.codenjoy.dojo.services.multiplayer.*;
import com.codenjoy.dojo.services.nullobj.NullJoystick;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.services.printer.GraphicPrinter;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import com.codenjoy.dojo.services.settings.SettingsReader;
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
import org.mockito.verification.VerificationMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static com.codenjoy.dojo.services.AdminServiceTest.assertPlayersLastResponse;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static com.codenjoy.dojo.utils.JsonUtils.clean;
import static com.codenjoy.dojo.utils.TestUtils.split;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.fest.reflect.core.Reflection.field;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CodenjoyContestApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles(SQLiteProfile.NAME)
@TestPropertySource(properties = {"game.ai=true"})
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
    private GameSaver saver;

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

    @MockBean
    private TimeService timeService;

    @SpyBean
    private Deals deals;

    @MockBean
    protected RoomService roomService;

    @SpyBean
    private DealsView dealsView;

    @Autowired
    private PlayerServiceImpl playerService;

    @Autowired
    private ScoresCleaner scoresCleaner;

    private InformationCollector informationCollector;

    @Mock
    private GraphicPrinter printer;
    private List<Joystick> joysticks = new LinkedList<>();
    private List<GamePlayer> gamePlayers = new LinkedList<>();
    private LinkedList<GameField> gameFields = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private List<String> ids = new LinkedList<>();
    private List<PlayerHero> heroesData = new LinkedList<>();
    private List<PlayerScores> playerScores = new LinkedList<>();
    private Map<String, Integer> chatIds = new HashMap<>();
    private Map<String, GameType> gameTypes = new HashMap<>();
    private Consumer<GameType> gameTypePostSetup;

    @Before
    public void setUp() {
        Mockito.reset(actionLogger, autoSaver, gameService, playerController, deals);
        deals.clear();

        screenSendCaptor = ArgumentCaptor.forClass(Map.class);
        playerCaptor = ArgumentCaptor.forClass(Player.class);
        xCaptor = ArgumentCaptor.forClass(Integer.class);
        yCaptor = ArgumentCaptor.forClass(Integer.class);
        plotsCaptor = ArgumentCaptor.forClass(List.class);
        boardCaptor = ArgumentCaptor.forClass(String.class);

        when(printer.print(any(), any())).thenReturn("1234");

        when(saver.loadGame(any())).thenReturn(PlayerSave.NULL);

        heroesData.addAll(Arrays.asList(heroData(1, 2), heroData(3, 4), heroData(5, 6), heroData(7, 8)));
        when(gameService.getGameType(anyString())).thenAnswer(inv -> {
            String game = inv.getArgument(0);
            return getGameType(game, game);
        });
        when(gameService.getGameType(anyString(), anyString())).thenAnswer(inv -> {
            String game = inv.getArgument(0);
            String room = inv.getArgument(1);
            when(roomService.game(room)).thenReturn(game);
            GameType gameType = getGameType(game, room);
            when(roomService.gameType(room)).thenReturn(gameType);
            return gameType;
        });
        when(gameService.exists(anyString())).thenReturn(true);

        when(chat.getLastMessageIds()).thenReturn(chatIds);

        // по умолчанию все команаты будут активными и открытыми для регистрации
        when(roomService.isActive(anyString())).thenReturn(true);
        when(roomService.isOpened(anyString())).thenReturn(true);

        doAnswer(inv -> {
            String id = inv.getArgument(0);
            return "readable_" + id;
        }).when(registration).getNameById(anyString());

        deals.clear();
        Mockito.reset(playerController, screenController, actionLogger);
        playerService.openRegistration();

        playerService.init();
    }

    public GameType getGameType(String game, String room) {
        if (!gameTypes.containsKey(room)) {
            GameType gameType = mock(GameType.class);
            setupGameType(gameType, game);
            gameTypes.put(room, gameType);
        }
        return gameTypes.get(room);
    }

    public void setupGameType(GameType gameType, String game) {
        when(gameType.name()).thenReturn(game);

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
            when(field.getSave()).thenReturn(new JSONObject(
                    "{'save':'field" + gameFields.size() + "'}"));
            return field;
        });

        when(gameType.createPlayer(any(EventListener.class), anyInt(), anyString(), any()))
                .thenAnswer(inv -> {
                    int teamId = inv.getArgument(1);

                    String id = inv.getArgument(2);
                    ids.add(id);

                    Joystick joystick = mock(Joystick.class);
                    joysticks.add(joystick);

                    GamePlayer gamePlayer = mock(GamePlayer.class);
                    gamePlayers.add(gamePlayer);

                    when(gamePlayer.getJoystick()).thenReturn(joystick);
                    when(gamePlayer.getHero()).thenReturn(heroesData.get(gamePlayers.size() - 1));
                    when(gamePlayer.isAlive()).thenReturn(true);
                    return gamePlayer;
                });

        when(gameType.getPlots()).thenReturn(Element.values());

        when(gameType.getPrinterFactory()).thenReturn(PrinterFactory.get(printer));

        spyMultiplayerType(gameType, MultiplayerType.SINGLE);

        if (gameTypePostSetup != null) {
            gameTypePostSetup.accept(gameType);
        }
    }

    // оборачиваем progress в spy - мы будем потом верифаить на нем что вызывалось
    public static void spyMultiplayerType(GameType gameType, MultiplayerType real) {
        MultiplayerType type = spy(real);
        when(type.progress()).thenAnswer(inv -> spy(inv.callRealMethod()));
        when(gameType.getMultiplayerType(any())).thenReturn(type);
    }

    static class APlayerHero extends PlayerHero implements NoActJoystick, NoDirectionJoystick {
        public APlayerHero(int x, int y) {
            super(x, y);
        }

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public void tick() {
            // do nothing
        }
    }

    private PlayerHero heroData(int x, int y) {
        return new APlayerHero(x, y);
    }

    enum Element implements CharElement {
        A('1'), B('2'), C('3'), D('4');

        private final char ch;

        Element(char c) {
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
        createPlayer(VASYA, "game", "room");

        // when
        Player player = playerService.get(VASYA);

        // then
        assertEquals("game", player.getGame());
        assertEquals(VASYA, player.getId());
        assertEquals(null, player.getPassword());
        assertEquals(null, player.getCode());
        assertEquals(VASYA_URL, player.getCallbackUrl());
        assertSame(gameType("room"), player.getGameType());
        assertNull(player.getMessage());
        assertEquals(0, player.getScore());
    }

    private GameType gameType(String room) {
        return gameTypes.get(room);
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
    public void shouldSetLastResponse_whenCreatePlayer() {
        // given
        setupTimeService(this.timeService);

        // when
        createPlayer(VASYA, "game1", "room1");
        createPlayer(PETYA, "game1", "room1");
        createPlayer(KATYA, "game1", "room2");
        createPlayer(OLIA, "game2", "room3");

        // then
        assertPlayersLastResponse(players,
                "[vasya: 1000], [petya: 2000], [katya: 3000], [olia: 4000]");
    }

    public static void setupTimeService(TimeService timeService) {
        AtomicLong time = new AtomicLong(1);
        when(timeService.now()).thenAnswer(inv -> time.getAndIncrement() * 1000L);
    }

    @Test
    public void shouldSendAdditionalInfoToAllPlayers() {
        // given
        createPlayer(VASYA, "game", "room1");
        createPlayer(PETYA, "game", "room2");

        when(printer.print(any(), any()))
                .thenReturn("1234")
                .thenReturn("4321");

        when(playerScores(0).getScore()).thenReturn(123);
        when(playerScores(1).getScore()).thenReturn(234);

        players.get(0).setTeamId(1);
        players.get(1).setTeamId(2);

        // when
        playerService.tick();

        // then
        verify(screenController).requestControlToAll(screenSendCaptor.capture());
        Map<ScreenRecipient, Object> data = screenSendCaptor.getValue();

        assertEquals(
                "{petya=PlayerData[BoardSize:15, \n" +
                    "Board:'DCBA', \n" +
                    "Game:'game', \n" +
                    "Score:234, \n" +
                    "Teams:{petya=2}, \n" +
                    "Info:'', \n" +
                    "Scores:'{petya=234}', \n" +
                    "Coordinates:'{petya=HeroDataImpl(level=0, \n" +
                        "coordinate=[3,4], \n" +
                        "isMultiplayer=false, \n" +
                        "additionalData=null)}', \n" +
                    "ReadableNames:'{petya=readable_petya}', \n" +
                    "Group:[petya], \n" +
                    "LastChatMessage:106558567], \n" +
                "vasya=PlayerData[BoardSize:15, \n" +
                    "Board:'ABCD', \n" +
                    "Game:'game', \n" +
                    "Score:123, \n" +
                    "Teams:{vasya=1}, \n" +
                    "Info:'', \n" +
                    "Scores:'{vasya=123}', \n" +
                    "Coordinates:'{vasya=HeroDataImpl(level=0, \n" +
                        "coordinate=[1,2], \n" +
                        "isMultiplayer=false, \n" +
                        "additionalData=null)}', \n" +
                    "ReadableNames:'{vasya=readable_vasya}', \n" +
                    "Group:[vasya], \n" +
                    "LastChatMessage:111979568]}",
                clean(split(data, ", \n")));
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
        assertEquals(1, deals.size());
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
        return createPlayer(id, "game", "room");
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
            verify(gameType(room), atLeastOnce()).createGame(anyInt(), any());
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
    public void shouldCreatePlayerFromSavedDeal_whenPlayerNotRegisterYet() {
        // given
        PlayerSave save = new PlayerSave(VASYA, getCallbackUrl(VASYA), "game", "room", 100, null);

        // when
        playerService.register(save);

        // then
        verify(gameType("room")).getPlayerScores(eq(100), any());
        when(playerScores(0).getScore()).thenReturn(100);

        Player player = playerService.get(VASYA);

        assertVasya(player);
        assertEquals(100, player.getScore());
        assertEquals(null, player.getMessage());
    }

    @Test
    public void shouldUpdatePlayerFromSavedDeal_whenPlayerAlreadyRegistered_whenOtherGameType() {
        // given
        Player registered = createPlayer(VASYA, "game", "room");
        assertEquals(VASYA_URL, registered.getCallbackUrl());

        PlayerSave save = new PlayerSave(VASYA, getCallbackUrl(VASYA), "other_game", "other_room", 200, null);

        // when
        playerService.register(save);

        // then
        verify(gameType("other_room")).getPlayerScores(eq(200), any());
        when(playerScores(1).getScore()).thenReturn(200);

        Player player = playerService.get(VASYA);

        assertVasya(player);
        assertEquals(200, player.getScore());
        assertEquals(null, player.getMessage());
    }

    @Test
    public void shouldNotUpdatePlayerFromSavedDeal_whenPlayerAlreadyRegistered_whenSameGameType() {
        // given
        Player registeredPlayer = createPlayer(VASYA, "game", "room");
        assertEquals(VASYA_URL, registeredPlayer.getCallbackUrl());
        assertEquals(0, registeredPlayer.getScore());

        PlayerSave save = new PlayerSave(VASYA, getCallbackUrl(VASYA), "game", "room", 200, null);

        // when
        playerService.register(save);

        // then
        verify(gameType("room")).getPlayerScores(eq(0), any());
        when(playerScores(0).getScore()).thenReturn(0);

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
        assertEquals(null, player.getCode());
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
    public void shouldAlsoCleanedSpreader() {
        // given
        shouldInformGame_whenUnregisterPlayer();

        // then
        assertEquals(0, deals.rooms().size());
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

    @Test
    public void shouldGetAllInRoom() {
        // given
        createPlayer(VASYA, "game1", "room1");
        createPlayer(PETYA, "game1", "room1");
        createPlayer(KATYA, "game1", "room2");
        createPlayer(OLIA, "game3", "room3");

        // when then
        assertEquals("[vasya, petya]",
                playerService.getAllInRoom("room1").toString());

        assertEquals("[katya]",
                playerService.getAllInRoom("room2").toString());

        assertEquals("[olia]",
                playerService.getAllInRoom("room3").toString());

        assertEquals("[]",
                playerService.getAllInRoom("room4").toString());
    }

    @Test
    public void shouldGetAll_forGame() {
        // given
        createPlayer(VASYA, "game1", "room1");
        createPlayer(PETYA, "game1", "room1");
        createPlayer(KATYA, "game1", "room2");
        createPlayer(OLIA, "game3", "room3");

        // when then
        assertEquals("[vasya, petya, katya]",
                playerService.getAll("game1").toString());

        assertEquals("[olia]",
                playerService.getAll("game3").toString());
    }

    @Test
    public void shouldGetRoomCounts() {
        // given
        createPlayer(VASYA, "game1", "room1");
        createPlayer(PETYA, "game1", "room1");
        createPlayer(KATYA, "game1", "room2");
        createPlayer(OLIA, "game3", "room3");

        when(roomService.rooms()).thenReturn(Arrays.asList(
                "room1", "room2", "room3", "room4"));

        // when
        Map<String, Integer> roomCounts = playerService.getRoomCounts();

        // then
        assertEquals("{room1=2, room2=1, room3=1, room4=0}",
                roomCounts.toString());
    }

    @Test
    public void shouldGetAnyGameWithPlayers() {
        // given
        createPlayer(VASYA, "game1", "room1");
        createPlayer(PETYA, "game1", "room1");
        createPlayer(KATYA, "game1", "room2");
        createPlayer(OLIA, "game3", "room3");
        gameService.getGameType("game4", "room4");

        when(roomService.rooms()).thenReturn(Arrays.asList(
                "room1", "room2", "room3", "room4"));

        // when
        String room = playerService.getAnyRoomWithPlayers();

        // then
        assertEquals("room1", room);
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
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));

        setNewGames(game1, game2);

        setup(game1);
        setup(game2);

        when(gameType("room").getMultiplayerType(any())).thenReturn(MultiplayerType.SINGLE);

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
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));

        setNewGames(game1, game2);

        setup(game1);
        setup(game2);
        GameField field1 = game1.getField();
        GameField field2 = game2.getField();
        doThrow(new RuntimeException()).when(field1).tick();

        when(gameType("room").getMultiplayerType(any())).thenReturn(MultiplayerType.SINGLE);

        // when
        playerService.tick();

        // then
        verify(field1).quietTick();
        verify(field2).quietTick();
    }

    private void setNewGames(Game... games) {
        List<Deal> list = getDeals();
        for (int index = 0; index < list.size(); index++) {
            Deal deal = list.get(index);

            field("game").ofType(Game.class).in(deal).set(games[index]);
        }
    }

    private List<Deal> getDeals() {
        return field(Deals.Fields.all).ofType(List.class).in(deals).get();
    }

    @Test
    public void shouldTickForOneGame_whenSingleBordersGameType() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));
        setNewGames(game1, game2);

        setup(game1);
        setup(game2);
        GameField field1 = game1.getField();
        when(game2.getField()).thenReturn(field1);
        doThrow(new RuntimeException()).when(field1).tick();

        when(gameType("room").getMultiplayerType(any())).thenReturn(MultiplayerType.MULTIPLE); // тут отличия с прошлым тестом

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
    public void shouldContinueTicks_whenExceptionInDealTick() {
        // given
        createPlayer(VASYA);

        Game game1 = createGame(gameField(VASYA));
        setNewGames(game1);

        setup(game1);

        List list = Reflection.field(Deals.Fields.all).ofType(List.class).in(deals).get();
        Deal deal = (Deal)list.remove(0);
        Deal spy = spy(deal);
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
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        Game game1 = createGame(gameField(VASYA));
        Game game2 = createGame(gameField(PETYA));
        setNewGames(game1, game2);

        setup(game1);
        setup(game2);

        when(gameType("room").getMultiplayerType(any())).thenReturn(MultiplayerType.MULTIPLE); // тут отличия с прошлым тестом
        GameField field1 = game1.getField();
        when(game2.getField()).thenReturn(field1);

        // when
        playerService.tick();

        // then
        verify(field1, once()).quietTick();
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
        verify(playerScores(0), once()).clear();
        verify(playerScores(1), once()).clear();

        verify(gameField(VASYA), once()).clearScore();
        verify(gameField(PETYA), once()).clearScore();

        verify(deals.get(VASYA).getGame().getProgress(), once()).reset();
        verify(deals.get(PETYA).getGame().getProgress(), once()).reset();

        verify(semifinal, once()).clean();
    }

    @Test
    public void shouldCleanAllScores_alsoCleanSaved() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");
        createPlayer(OLIA, "game", "room");

        long time = 100L;

        when(timeService.now()).thenReturn(time);
        when(saver.getSavedList()).thenReturn(Arrays.asList(PETYA, OLIA, KATYA));
        when(saver.loadGame(PETYA)).thenReturn(new PlayerSave(PETYA, "saved-url1", "game", "room", 123, "{}"));
        when(saver.loadGame(OLIA)).thenReturn(new PlayerSave(OLIA, "saved-url2", "game", "room", 234, "{}"));
        when(saver.loadGame(KATYA)).thenReturn(new PlayerSave(KATYA, "saved-url3", "game2", "room2", 345, "{}"));
        when(gameService.getDefaultProgress(any())).thenReturn(
                "{'data':'value1'}",
                "{'data':'value2'}",
                "{'data':'value3'}",
                "{'data':'value4'}");

        // when
        playerService.cleanAllScores();

        // then
        verify(playerScores(0), once()).clear();
        verify(playerScores(1), once()).clear();
        verify(playerScores(2), once()).clear();

        verify(gameField(VASYA), once()).clearScore();
        verify(gameField(PETYA), once()).clearScore();
        verify(gameField(OLIA), once()).clearScore();

        verify(deals.get(VASYA).getGame().getProgress(), once()).reset();
        verify(deals.get(PETYA).getGame().getProgress(), once()).reset();
        verify(deals.get(OLIA).getGame().getProgress(), once()).reset();

        verify(semifinal, once()).clean();

        // clear saved scores
        ArgumentCaptor<Player> player = ArgumentCaptor.forClass(Player.class);
        ArgumentCaptor<String> save = ArgumentCaptor.forClass(String.class);
        verify(saver, times(3)).saveGame(player.capture(), eq(0), save.capture(), eq(time));
        List<Player> players = player.getAllValues();
        List<String> saves = save.getAllValues();

        assertEquals("[petya, olia, katya]", players.toString());

        Player player1 = players.get(0);
        assertEquals("petya", player1.getId());
        assertEquals("room", player1.getRoom());
        assertEquals("game", player1.getGame());
        assertEquals("saved-url1", player1.getCallbackUrl());
        assertEquals(0, player1.getScore());

        Player player2 = players.get(1);
        assertEquals("olia", player2.getId());
        assertEquals("room", player2.getRoom());
        assertEquals("game", player2.getGame());
        assertEquals("saved-url2", player2.getCallbackUrl());
        assertEquals(0, player2.getScore());

        Player player3 = players.get(2);
        assertEquals("katya", player3.getId());
        assertEquals("room2", player3.getRoom());
        assertEquals("game2", player3.getGame());
        assertEquals("saved-url3", player3.getCallbackUrl());
        assertEquals(0, player3.getScore());

        assertEquals("[{'data':'value1'}, {'data':'value2'}, {'data':'value3'}]", saves.toString());

        // another way to clear saved scores for rest active players without save
        ArgumentCaptor<List<Deal>> deals = ArgumentCaptor.forClass(List.class);
        verify(saver, times(1)).saveGames(deals.capture(), eq(time));
        if (!deals.getAllValues().isEmpty()) {
            assertEquals("[Save[time:100, id:vasya, teamId:0, url:http://vasya:1234, " +
                            "game:game, room:room, score:0, save:{\"save\":\"field1\"}]]",
                    deals.getValue().stream()
                            .map(deal -> new DealSaver.Save(deal, String.valueOf(time)))
                            .collect(toList())
                            .toString());

        }
    }

    @Test
    public void shouldCleanScores() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        // when
        playerService.cleanScores(VASYA);

        // then
        verify(playerScores(0), once()).clear();
        verify(playerScores(1), never()).clear();

        verify(gameField(VASYA), once()).clearScore();
        verify(gameField(PETYA), never()).clearScore();

        verify(deals.get(VASYA).getGame().getProgress(), once()).reset();
        verify(deals.get(PETYA).getGame().getProgress(), never()).reset();

        verify(semifinal, never()).clean();
    }

    @Test
    public void shouldCleanScores_alsoCleanSaved() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        long time = 100L;
        when(timeService.now()).thenReturn(time);
        when(saver.getSavedList()).thenReturn(Arrays.asList(VASYA, OLIA));
        when(saver.loadGame(VASYA)).thenReturn(new PlayerSave(VASYA, "saved-url1", "game", "room", 123, "{}"));
        when(saver.loadGame(OLIA)).thenReturn(new PlayerSave(OLIA, "saved-url2", "game2", "room2", 234, "{}"));
        when(gameService.getDefaultProgress(any())).thenReturn(
                "{'data':'value1'}",
                "{'data':'value2'}",
                "{'data':'value3'}",
                "{'data':'value4'}");

        // when
        playerService.cleanScores(VASYA);

        // then
        verify(playerScores(0), once()).clear();
        verify(playerScores(1), never()).clear();

        verify(gameField(VASYA), once()).clearScore();
        verify(gameField(PETYA), never()).clearScore();

        verify(deals.get(VASYA).getGame().getProgress(), once()).reset();
        verify(deals.get(PETYA).getGame().getProgress(), never()).reset();

        verify(semifinal, never()).clean();

        // clear saved scores
        ArgumentCaptor<Player> player = ArgumentCaptor.forClass(Player.class);
        ArgumentCaptor<String> save = ArgumentCaptor.forClass(String.class);
        verify(saver, times(1)).saveGame(player.capture(), eq(0), save.capture(), eq(time));
        List<Player> players = player.getAllValues();
        List<String> saves = save.getAllValues();

        assertEquals("[vasya]", players.toString());

        Player player1 = players.get(0);
        assertEquals("vasya", player1.getId());
        assertEquals("room", player1.getRoom());
        assertEquals("game", player1.getGame());
        assertEquals("saved-url1", player1.getCallbackUrl());
        assertEquals(0, player1.getScore());

        assertEquals("[{'data':'value1'}]", saves.toString());

        // another way to clear saved scores for rest active players without save
        ArgumentCaptor<List<Deal>> deals = ArgumentCaptor.forClass(List.class);
        verify(saver, times(0)).saveGames(deals.capture(), eq(time));
        if (!deals.getAllValues().isEmpty()) {
            assertEquals("[]",
                    deals.getValue().stream()
                            .map(deal -> new DealSaver.Save(deal, String.valueOf(time)))
                            .collect(toList())
                            .toString());
        }
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
        verify(playerScores(0), once()).clear();
        verify(playerScores(1), once()).clear();
        verify(playerScores(2), never()).clear();
        verify(playerScores(3), never()).clear();

        verify(gameField(VASYA), once()).clearScore();
        verify(gameField(PETYA), once()).clearScore();
        verify(gameField(KATYA), never()).clearScore();
        verify(gameField(OLIA), never()).clearScore();

        verify(deals.get(VASYA).getGame().getProgress(), once()).reset();
        verify(deals.get(PETYA).getGame().getProgress(), once()).reset();
        verify(deals.get(KATYA).getGame().getProgress(), never()).reset();
        verify(deals.get(OLIA).getGame().getProgress(), never()).reset();

        verify(semifinal, once()).clean("room1");
    }

    @Test
    public void shouldCleanAllScores_forRoom_alsoCleanSaved() {
        // given
        createPlayer(VASYA, "game1", "room1");
        createPlayer(PETYA, "game1", "room1");
        createPlayer(KATYA, "game1", "room2");
        createPlayer(OLIA, "game3", "room3");

        long time = 100L;
        when(timeService.now()).thenReturn(time);
        when(saver.getSavedList("room1")).thenReturn(Arrays.asList(PETYA));
        when(saver.getSavedList("room2")).thenReturn(Arrays.asList(KATYA));
        when(saver.getSavedList("room3")).thenReturn(Arrays.asList(OLIA));
        when(saver.loadGame(PETYA)).thenReturn(new PlayerSave(PETYA, "saved-url1", "game1", "room1", 123, "{}"));
        when(saver.loadGame(KATYA)).thenReturn(new PlayerSave(KATYA, "saved-url2", "game1", "room2", 234, "{}"));
        when(saver.loadGame(OLIA)).thenReturn(new PlayerSave(OLIA, "saved-url3", "game3", "room3", 345, "{}"));
        when(gameService.getDefaultProgress(any())).thenReturn(
                "{'data':'value1'}",
                "{'data':'value2'}",
                "{'data':'value3'}",
                "{'data':'value4'}");

        // when
        playerService.cleanAllScores("room1");

        // then
        verify(playerScores(0), once()).clear();
        verify(playerScores(1), once()).clear();
        verify(playerScores(2), never()).clear();
        verify(playerScores(3), never()).clear();

        verify(gameField(VASYA), once()).clearScore();
        verify(gameField(PETYA), once()).clearScore();
        verify(gameField(KATYA), never()).clearScore();
        verify(gameField(OLIA), never()).clearScore();

        verify(deals.get(VASYA).getGame().getProgress(), once()).reset();
        verify(deals.get(PETYA).getGame().getProgress(), once()).reset();
        verify(deals.get(KATYA).getGame().getProgress(), never()).reset();
        verify(deals.get(OLIA).getGame().getProgress(), never()).reset();

        verify(semifinal, once()).clean("room1");

        // clear saved scores
        ArgumentCaptor<Player> player = ArgumentCaptor.forClass(Player.class);
        ArgumentCaptor<String> save = ArgumentCaptor.forClass(String.class);
        verify(saver, times(1)).saveGame(player.capture(), eq(0), save.capture(), eq(time));
        List<Player> players = player.getAllValues();
        List<String> saves = save.getAllValues();

        assertEquals("[petya]", players.toString());

        Player player1 = players.get(0);
        assertEquals("petya", player1.getId());
        assertEquals("room1", player1.getRoom());
        assertEquals("game1", player1.getGame());
        assertEquals("saved-url1", player1.getCallbackUrl());
        assertEquals(0, player1.getScore());

        assertEquals("[{'data':'value1'}]", saves.toString());

        // another way to clear saved scores for rest active players without save
        ArgumentCaptor<List<Deal>> deals = ArgumentCaptor.forClass(List.class);
        verify(saver, times(1)).saveGames(deals.capture(), eq(time));
        if (!deals.getAllValues().isEmpty()) {
            assertEquals("[Save[time:100, id:vasya, teamId:0, url:http://vasya:1234, " +
                            "game:game1, room:room1, score:0, save:{\"save\":\"field1\"}]]",
                    deals.getValue().stream()
                            .map(deal -> new DealSaver.Save(deal, String.valueOf(time)))
                            .collect(toList())
                            .toString());
        }
    }

    private VerificationMode once() {
        return times(1);
    }

    private PlayerScores playerScores(int index) {
        return playerScores.get(index);
    }

    @Test
    public void shouldGetRandom_other() {
        createPlayer(VASYA);
        createPlayer(PETYA);

        assertEquals(VASYA, playerService.getRandomInRoom("room").getId());
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
        infos.add(new PlayerInfo(VASYA, "new-code1", "new-url1", "game") {{
            setEmail("new-email1");
            setReadableName("new-readableName1");
        }});
        infos.add(new PlayerInfo(PETYA, "new-code2", "new-url2", "game") {{
            setEmail("new-email2");
            setReadableName("new-readableName2");
        }});
        playerService.updateAll(infos);

        // then
        assertUpdated("[vasya, petya]", playerService.getAll());
    }

    @Test
    public void shouldUpdateAll_changeTeamId() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        assertPlayersTeams("{vasya=0, petya=0}");

        // when
        List<PlayerInfo> infos = new LinkedList<>();
        infos.add(new PlayerInfo(VASYA, null, null, null,
                null, 2, "game", null, false));
        infos.add(new PlayerInfo(PETYA, null, null, null,
                null, 1, "game", null, false));
        playerService.updateAll(infos);

        // then
        assertPlayersTeams("{vasya=2, petya=1}");

        // when
        infos = new LinkedList<>();
        infos.add(new PlayerInfo(VASYA, null, null, null,
                null, 3, "game", null, false));
        infos.add(new PlayerInfo(PETYA, null, null, null,
                null, 3, "game", null, false));
        playerService.updateAll(infos);

        // then
        assertPlayersTeams("{vasya=3, petya=3}");
    }

    @Test
    public void shouldUpdate_mainCase() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        playerService.update(new PlayerInfo(VASYA, "new-code1", "new-url1", "game") {{
            setEmail("new-email1");
            setReadableName("new-readableName1");
        }});
        playerService.update(new PlayerInfo(PETYA, "new-code2", "new-url2", "game") {{
            setEmail("new-email2");
            setReadableName("new-readableName2");
        }});

        // then
        assertUpdated("[vasya, petya]", playerService.getAll());
    }

    @Test
    public void shouldUpdate_changeTeamId() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        assertPlayersTeams("{vasya=0, petya=0}");

        // when
        playerService.update(new PlayerInfo(VASYA, null, null, null,
                null, 1, "game", null, false));

        // then
        assertPlayersTeams("{vasya=1, petya=0}");

        // when
        playerService.update(new PlayerInfo(PETYA, null, null, null,
                null, 2, "game", null, false));

        // then
        assertPlayersTeams("{vasya=1, petya=2}");

        // when
        playerService.update(new PlayerInfo(VASYA, null, null, null,
                null, 2, "game", null, false));

        // then
        assertPlayersTeams("{vasya=2, petya=2}");
    }

    private void assertPlayersTeams(String expected) {
        assertEquals(expected,
                playerService.getAll().stream()
                        .collect(toMap(Player::getId, Player::getTeamId,
                                (value1, value2) -> value2,
                                LinkedHashMap::new))
                        .toString());
    }

    @Test
    public void shouldUpdate_changeRoom_caseNewRoom_sameGame_chooseGame() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        assertEquals("[vasya, petya]",
                playerService.getAll("game").toString());

        assertEquals("[vasya, petya]",
                playerService.getAllInRoom("room").toString());

        // when
        playerService.update(new PlayerInfo(VASYA, null, null, null,
                "otherRoom", DEFAULT_TEAM_ID, "game", null, true));

        // then
        assertEquals("[vasya, petya]",
                playerService.getAll("game").toString());

        assertEquals("[petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[vasya]",
                playerService.getAllInRoom("otherRoom").toString());
    }

    @Test
    public void shouldUpdate_changeRoom_caseNewRoom_sameGame_notSetGame() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        assertEquals("[vasya, petya]",
                playerService.getAll("game").toString());

        assertEquals("[vasya, petya]",
                playerService.getAllInRoom("room").toString());

        // when
        String game = null; // мы не установили игру
        playerService.update(new PlayerInfo(VASYA, null, null, null,
                "otherRoom", DEFAULT_TEAM_ID, game, null, true));

        // then
        assertEquals("[vasya, petya]",
                playerService.getAll("game").toString());

        assertEquals("[petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[vasya]",
                playerService.getAllInRoom("otherRoom").toString());
    }

    @Test
    public void shouldUpdate_changeRoom_caseExistingRoom_sameGame_chooseGame() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");
        createPlayer(OLIA, "game", "otherRoom");

        assertEquals("[vasya, petya, olia]",
                playerService.getAll("game").toString());

        assertEquals("[vasya, petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[olia]",
                playerService.getAllInRoom("otherRoom").toString());

        // when
        playerService.update(new PlayerInfo(VASYA, null, null, null,
                "otherRoom", DEFAULT_TEAM_ID, "game", null, true));

        // then
        assertEquals("[vasya, petya, olia]",
                playerService.getAll("game").toString());

        assertEquals("[petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[vasya, olia]",
                playerService.getAllInRoom("otherRoom").toString());
    }

    @Test
    public void shouldUpdate_changeRoom_caseExistingRoom_sameGame_notSetGame() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");
        createPlayer(OLIA, "game", "otherRoom");

        assertEquals("[vasya, petya, olia]",
                playerService.getAll("game").toString());

        assertEquals("[vasya, petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[olia]",
                playerService.getAllInRoom("otherRoom").toString());

        // when
        String game = null; // мы не установили игру
        playerService.update(new PlayerInfo(VASYA, null, null, null,
                "otherRoom", DEFAULT_TEAM_ID, game, null, true));

        // then
        assertEquals("[vasya, petya, olia]",
                playerService.getAll("game").toString());

        assertEquals("[petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[vasya, olia]",
                playerService.getAllInRoom("otherRoom").toString());
    }

    @Test
    public void shouldUpdate_changeRoom_caseNewRoom_otherGame() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");

        assertEquals("[vasya, petya]",
                playerService.getAll("game").toString());

        assertEquals("[vasya, petya]",
                playerService.getAllInRoom("room").toString());

        // when
        playerService.update(new PlayerInfo(VASYA, null, null, null,
                "otherRoom", DEFAULT_TEAM_ID, "otherGame", null, true));

        // then
        assertEquals("[petya]",
                playerService.getAll("game").toString());

        assertEquals("[vasya]",
                playerService.getAll("otherGame").toString());

        assertEquals("[petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[vasya]",
                playerService.getAllInRoom("otherRoom").toString());
    }

    @Test
    public void shouldUpdate_changeRoom_caseExistingRoom_otherGame() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");
        createPlayer(OLIA, "otherGame", "otherRoom");

        assertEquals("[vasya, petya]",
                playerService.getAll("game").toString());

        assertEquals("[olia]",
                playerService.getAll("otherGame").toString());

        assertEquals("[vasya, petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[olia]",
                playerService.getAllInRoom("otherRoom").toString());

        // when
        playerService.update(new PlayerInfo(VASYA, null, null, null,
                "otherRoom", DEFAULT_TEAM_ID, "otherGame", null, true));

        // then
        assertEquals("[petya]",
                playerService.getAll("game").toString());

        assertEquals("[olia, vasya]",
                playerService.getAll("otherGame").toString());

        assertEquals("[petya]",
                playerService.getAllInRoom("room").toString());

        assertEquals("[olia, vasya]",
                playerService.getAllInRoom("otherRoom").toString());
    }

    @Test
    public void shouldSendPlayerNameToGame() {
        // given
        createPlayer(VASYA, "game", "room");
        createPlayer(PETYA, "game", "room");
        createPlayer(OLIA, "game", "otherRoom");
        createPlayer(KATYA, "otherGame", "otherRoom2");

        // when then
        assertEquals("[vasya, petya, olia, katya]", players.toString());
        assertEquals("[vasya, petya, olia, katya]", ids.toString());
    }

    private void assertUpdated(String expected, List<Player> all) {
        assertEquals(expected, all.toString());

        Player player1 = all.get(0);
        assertEquals("new-url1", player1.getCallbackUrl());
        assertEquals(null, player1.getCode());
        assertEquals("game", player1.getGame());
        assertEquals("new-email1", player1.getEmail());
        verify(registration).updateEmail(VASYA, "new-email1");
        assertEquals("new-readableName1", player1.getReadableName());
        verify(registration).updateReadableName(VASYA, "new-readableName1");
        assertEquals(null, player1.getPassword());

        Player player2 = all.get(1);
        assertEquals("new-url2", player2.getCallbackUrl());
        assertEquals(null, player2.getCode());
        assertEquals("game", player1.getGame());
        assertEquals("new-email2", player2.getEmail());
        verify(registration).updateEmail(PETYA, "new-email2");
        assertEquals("new-readableName2", player2.getReadableName());
        verify(registration).updateReadableName(PETYA, "new-readableName2");
        assertEquals(null, player2.getPassword());
    }

    @Test
    public void shouldUpdateAll_removeNullUsers() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        // when
        List<PlayerInfo> infos = new LinkedList<>();
        infos.add(new PlayerInfo(VASYA, "new-pass1", "new-url1", "game") {{
            setEmail("new-email1");
            setReadableName("new-readableName1");
        }});
        infos.add(new PlayerInfo(PETYA, "new-pass2", "new-url2", "game") {{
            setEmail("new-email2");
            setReadableName("new-readableName2");
        }});
        infos.add(new PlayerInfo(null, "new-pass3", "new-url3", "game") {{
            setEmail("new-email3");
            setReadableName("new-readableName3");
        }});
        playerService.updateAll(infos);

        // then
        assertUpdated("[vasya, petya]", playerService.getAll());
    }

    @Test
    public void shouldUpdateAll_exceptionIfCountUsersNotEqual() {
        // given
        createPlayer(VASYA);
        createPlayer(PETYA);

        List<PlayerInfo> infos = new LinkedList<>();
        infos.add(new PlayerInfo("new-vasya", "new-pass1", "new-url1", "game"));

        try {
            // when
            playerService.updateAll(infos);
            fail();
        } catch (Exception e) {
            // then
            assertEquals("java.lang.IllegalArgumentException: Player not found by id: new-vasya", e.toString());
        }

        List<Player> all = playerService.getAll();
        assertVasyaAndPetya(all);
    }

    private void assertVasyaAndPetya(List<Player> all) {
        assertEquals("[vasya, petya]", all.toString());

        Player player1 = all.get(0);
        assertEquals(VASYA_URL, player1.getCallbackUrl());
        assertEquals(null, player1.getCode());
        assertEquals(null, player1.getPassword());

        Player player2 = all.get(1);
        assertEquals(PETYA_URL, player2.getCallbackUrl());
        assertEquals(null, player2.getCode());
        assertEquals(null, player2.getPassword());
    }

    @Test
    public void shouldUpdateAll_loadFromSave() {
        // given
        Player player1 = createPlayer(VASYA);
        Player player2 = createPlayer(PETYA);
        long now = Calendar.getInstance().getTimeInMillis();

        // when
        List<PlayerInfo> infos = new LinkedList<>(){{
            add(new PlayerInfo(player1, now){{
                setData("{\"some\":\"data1\"}");
            }});
            add(new PlayerInfo(player2, now){{
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
        verify(deals).setLevel(eq(player.getId()), captor.capture());
        assertEquals(save, captor.getAllValues().toString());
    }

    @Test
    public void shouldUpdateAll_loadFromSave_onlyIfSaveIsNotSame() {
        // given
        Player player1 = createPlayer(VASYA);
        Player player2 = createPlayer(PETYA);
        long now = Calendar.getInstance().getTimeInMillis();

        // when
        List<PlayerInfo> infos = new LinkedList<>(){{
            add(new PlayerInfo(player1, now){{
                setData("{\"some\":\"data1\"}");
            }});
            add(new PlayerInfo(player2, now){{
                setData(gameFields.getLast().getSave().toString()); // same
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
        long now = Calendar.getInstance().getTimeInMillis();

        // when
        List<PlayerInfo> infos = new LinkedList<>(){{
            add(new PlayerInfo(player1, now){{
                setData("{\"some\":\"data1\"}");
            }});
            add(new PlayerInfo(player2, now){{
                setData(""); // empty
            }});
            add(new PlayerInfo(player3, now){{
                setData(null); // null
            }});
            add(new PlayerInfo(player4, now){{
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
        verify(deals, never()).setLevel(eq(player.getId()), any(JSONObject.class));
    }

    @Test
    public void shouldLogActionsOnTick() {
        // given
        createPlayer(VASYA);

        // when
        playerService.tick();

        // then
        verify(actionLogger).log(deals);
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

        gameTypePostSetup = gameType -> {
            when(gameType.getAI()).thenReturn(AISolverStub.class);
            when(gameType.getBoard()).thenReturn(BoardStub.class);
        };

        String game = createPlayer(VASYA, "game", "room").getGame();

        verify(gameType("room"), times(1)).getAI();
        verify(gameType("room"), times(1)).getBoard();

        // when
        playerService.reloadAI(VASYA);

        // then
        verify(gameType("room"), times(2)).getAI();
        verify(gameType("room"), times(2)).getBoard();

        Deal deal = deals.get(VASYA);
        assertEquals(game, deal.getPlayer().getGame());
        Player player = deal.getPlayer();
        assertEquals(VASYA, player.getId());
        assertNotNull(VASYA, player.getAi());
    }

    @Test
    public void testLoadPlayersFromSaveAndLoadAI() {
        // given
        gameTypePostSetup = gameType -> {
            when(gameType.getAI()).thenReturn(AISolverStub.class);
            when(gameType.getBoard()).thenReturn(BoardStub.class);
        };

        PlayerSave save = new PlayerSave(VASYA_AI, getCallbackUrl(VASYA_AI), "game", "room", 100, null);

        // when
        playerService.register(save);

        // then
        verify(gameType("room")).getAI();
        verify(gameType("room")).getBoard();

        Deal deal = deals.get(VASYA_AI);
        assertEquals("game", deal.getPlayer().getGame());
        Player player = deal.getPlayer();
        assertEquals(VASYA_AI, player.getId());
        assertNotNull(VASYA, player.getAi());
    }

    @Test
    public void testCleanSavedScore_passTeamIdFromSave() {
        // given
        int teamId = 3;
        PlayerSave playerSave = new PlayerSave("player", teamId, "url", "game", "room", 0, "{}");
        when(saver.loadGame("player")).thenReturn(playerSave);

        // when
        scoresCleaner.cleanScores("player");

        // then
        verify(saver).saveGame(new Player(playerSave), teamId, null, 0L);
    }

    @Test
    public void testRegister_passTeamIdFromSave() {
        // given
        playerService = spy(playerService);
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);

        int teamId = 3;
        PlayerSave playerSave = new PlayerSave("player", teamId, "url", "game", "room", 0, "{}");
        when(saver.loadGame("player")).thenReturn(playerSave);

        // when
        playerService.register("player", "game", "room", "ip");
        verify(playerService).register(captor.capture());

        // then
        assertEquals(1, deals.all().size());
        assertEquals(teamId, captor.getValue().getTeamId());
    }

    @Test
    public void testRegister_passDefaultTeamIdFromSave() {
        // given
        playerService = spy(playerService);
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);

        PlayerSave playerSave = new PlayerSave("player", "url", "game", "room", 0, "{}");
        when(saver.loadGame("player")).thenReturn(playerSave);

        // when
        playerService.register("player", "game", "room", "ip");
        verify(playerService).register(captor.capture());

        // then
        assertEquals(1, deals.all().size());
        assertEquals(DEFAULT_TEAM_ID, captor.getValue().getTeamId());
    }

    @Test
    public void testRegister_passDefaultTeamIdFromNullSave() {
        // given
        playerService = spy(playerService);
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);

        when(saver.loadGame("player")).thenReturn(PlayerSave.NULL);

        // when
        playerService.register("player", "game", "room", "ip");
        verify(playerService).register(captor.capture());

        // then
        assertEquals(1, deals.all().size());
        assertEquals(DEFAULT_TEAM_ID, captor.getValue().getTeamId());
    }
}