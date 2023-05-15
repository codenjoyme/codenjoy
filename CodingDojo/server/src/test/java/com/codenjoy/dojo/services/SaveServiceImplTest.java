package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.info.Information;
import com.codenjoy.dojo.services.multiplayer.FieldService;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.Spreader;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import java.util.*;

import static com.codenjoy.dojo.services.PlayerSave.NULL;
import static com.codenjoy.dojo.services.PlayerServiceImplTest.setupTimeService;
import static com.codenjoy.dojo.utils.TestUtils.setupChat;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// TODO try @SpringBootTest
public class SaveServiceImplTest {

    private Registration registration;
    private SaveServiceImpl saveService;
    private PlayerService playerService;
    private Deals deals;
    private GameSaver saver;
    private TimeService timeService;

    private List<Registration.User> users;
    private List<Player> players;
    private List<GameField> fields;
    public static final boolean NOT_REGISTERED = false;

    @Before
    public void setup() {
        saveService = new SaveServiceImpl(){{
            RoomService roomService = mock(RoomService.class);
            FieldService fieldService = mock(FieldService.class);
            Spreader spreader = new Spreader(fieldService);
            this.deals = SaveServiceImplTest.this.deals = new Deals(spreader, roomService);
            setupChat(deals, null);
            this.players = SaveServiceImplTest.this.playerService = mock(PlayerService.class);
            this.saver = SaveServiceImplTest.this.saver = mock(GameSaver.class);
            this.registration = SaveServiceImplTest.this.registration = mock(Registration.class);
            this.time = SaveServiceImplTest.this.timeService = spy(TimeService.class);
        }};

        users = new LinkedList<>();
        players = new LinkedList<>();
        fields = new LinkedList<>();

        when(playerService.getAll()).thenReturn(players);
        when(playerService.get(anyString())).thenReturn(NullPlayer.INSTANCE);
    }

    @Test
    public void shouldSavePlayerWhenExists() {
        // given
        Player player = createPlayer("vasia");
        fieldSave(0, "{'key':'value'}");

        // when
        long time = saveService.save("vasia");

        // then
        verify(saver).saveGame(player, 0, "{\"key\":\"value\"}", time);
    }

    private Player createPlayer(String id) {
        return createPlayer(id, "room");
    }

    private Player createPlayer(String id, String room) {
        Player player = mock(Player.class);
        when(player.getId()).thenReturn(id);
        when(player.getCode()).thenReturn("code_" + id);
        when(player.getData()).thenReturn("data for " + id);
        when(player.getGame()).thenReturn("game " + room);
        when(player.getRoom()).thenReturn(room);
        when(player.hasAi()).thenReturn(true);
        when(player.getCallbackUrl()).thenReturn("http://" + id + ":1234");
        when(player.getEmail()).thenReturn(null);        // берется из registration
        when(player.getReadableName()).thenReturn(null); // берется из registration
        when(player.getInfo()).thenReturn(mock(Information.class));
        long now = timeService.now();
        when(player.getLastResponse()).thenReturn(now);
        when(playerService.get(id)).thenReturn(player);
        players.add(player);

        Answer<Object> answerCreateGame = inv1 -> {
            GameField field = mock(GameField.class);
            fields.add(field);
            return field;
        };

        TestUtils.Env env = TestUtils.getDeal(
                deals,
                player,
                room,
                answerCreateGame,
                MultiplayerType.SINGLE,
                null,
                parameters -> "board"
        );
        Deal deal = env.deal;

        return deal.getPlayer();
    }

    @Test
    public void shouldNotSavePlayer_whenNotExists() {
        // when
        saveService.save("cocacola");

        // then
        verify(saver, never()).saveGame(any(Player.class), eq(0), any(String.class), anyLong());
    }

    @Test
    public void shouldLoadPlayer_whenSaveNotFound() {
        // given
        when(saver.loadGame("vasia")).thenReturn(NULL);

        // when
        boolean load = saveService.load("vasia");

        // then
        assertEquals(false, load);
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldLoadPlayer_whenNotRegistered() {
        // given
        PlayerSave save = new PlayerSave("vasia", "url", "game", "room", 100, null);
        when(saver.loadGame("vasia")).thenReturn(save);
        allPlayersNotRegistered();

        // when
        saveService.load("vasia");

        // then
        verify(playerService).contains("vasia");
        verify(playerService).register(save);
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldLoadPlayer_whenRegistered() {
        // given
        PlayerSave save = new PlayerSave("vasia", "127.0.0.2", "game", "room", 100, null);
        when(saver.loadGame("vasia")).thenReturn(save);
        allPlayersRegistered();

        // when
        saveService.load("vasia");

        // then
        verify(playerService).contains("vasia");
        verify(playerService).remove("vasia");
        verify(playerService).register(save);
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldLoadPlayerWithExternalSave_whenNotRegistered_caseSaveExists() {
        // given
        PlayerSave save = new PlayerSave("vasia", "127.0.0.2", "game", "room", 0, "{'save':'data'}");
        when(saver.loadGame("vasia")).thenReturn(save);
        allPlayersNotRegistered();

        // when
        saveService.load("vasia", "game", "room", "{'save':'data'}");

        // then
        verify(saver).loadGame("vasia");
        verifyNoMoreInteractions(saver);

        verify(playerService).contains("vasia");
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);
        verify(playerService).register(captor.capture());
        PlayerSave actual = captor.getValue();
        assertEquals("{'callbackUrl':'127.0.0.2'," +
                "'game':'game'," +
                "'id':'vasia'," +
                "'room':'room'," +
                "'save':'{'save':'data'}'," +
                "'score':0," +
                "'teamId':0}", JsonUtils.cleanSorted(actual));
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldLoadPlayerWithExternalSave_whenNotRegistered_caseSaveNotExists() {
        // given
        when(saver.loadGame("vasia")).thenReturn(NULL);
        allPlayersNotRegistered();

        // when
        saveService.load("vasia", "game", "room", "{'save':'data'}");

        // then
        verify(saver).loadGame("vasia");
        verifyNoMoreInteractions(saver);

        verify(playerService).contains("vasia");
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);
        verify(playerService).register(captor.capture());
        PlayerSave actual = captor.getValue();
        assertEquals("{'callbackUrl':'" + SaveServiceImpl.DEFAULT_CALLBACK_URL + "'," +
                "'game':'game'," +
                "'id':'vasia'," +
                "'room':'room'," +
                "'save':'{'save':'data'}'," +
                "'score':0," +
                "'teamId':0}", JsonUtils.cleanSorted(actual));
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldLoadPlayerWithExternalSave_whenRegistered() {
        // given
        PlayerSave save = new PlayerSave("vasia", "127.0.0.2", "game", "room", 0, "{'save':'data'}");
        when(saver.loadGame("vasia")).thenReturn(save);
        allPlayersRegistered();

        // when
        saveService.load("vasia", "game", "room", "{'save':'data'}");

        // then
        verify(saver).loadGame("vasia");
        verifyNoMoreInteractions(saver);

        verify(playerService).contains("vasia");
        verify(playerService).remove("vasia");  // << difference
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);
        verify(playerService).register(captor.capture());
        PlayerSave actual = captor.getValue();
        assertEquals("{'callbackUrl':'127.0.0.2'," +
                "'game':'game'," +
                "'id':'vasia'," +
                "'room':'room'," +
                "'save':'{'save':'data'}'," +
                "'score':0," +
                "'teamId':0}", JsonUtils.cleanSorted(actual));
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldGetAllActivePlayersWithSavedGamesDataSortedByName() {
        // given
        setupTimeService(timeService);

        Player activeSavedPlayer = createPlayer("activeSaved"); // check sorting order (activeSaved > active)
        Player activePlayer = createPlayer("active");

        teamId(activeSavedPlayer, 1);
        teamId(activePlayer, 2);

        scores(activeSavedPlayer, 10);
        scores(activePlayer, 11);

        PlayerSave save1 = new PlayerSave(activeSavedPlayer);
        PlayerSave save2 = new PlayerSave(activePlayer);
        PlayerSave save3 = new PlayerSave("saved", "http://saved:1234", "saved game room", "room", 15, "data for saved");

        when(saver.getSavedList()).thenReturn(Arrays.asList("activeSaved", "saved"));
        when(saver.loadGame("activeSaved")).thenReturn(save1);
        when(saver.loadGame("active")).thenReturn(save2);
        when(saver.loadGame("saved")).thenReturn(save3);

        fieldSave(0, "{'data':1}");
        fieldSave(1, "{'data':2}");

        createUser("activeSaved");
        createUser("active");
        createUser("saved");

        // when
        List<PlayerInfo> games = saveService.getSaves();

        // then
        assertPlayerInfo(games,
                "[{\n" +
                "  'active':true,\n" +
                "  'aiPlayer':true,\n" +
                "  'callbackUrl':'http://active:1234',\n" +
                "  'code':'code_active',\n" +
                "  'data':'{\\'data\\':2}',\n" +
                "  'email':'active@email.com',\n" +
                "  'game':'game room',\n" +
                "  'gameOnly':'game room',\n" +
                "  'hidden':false,\n" +
                "  'id':'active',\n" +
                "  'lastResponse':2000,\n" +
                "  'notNullReadableName':'readable_active',\n" +
                "  'readableName':'readable_active',\n" +
                "  'room':'room',\n" +
                "  'saved':false,\n" +
                "  'score':11,\n" +
                "  'teamId':2,\n" +
                "  'ticksInactive':1\n" +
                "}, {\n" +
                "  'active':true,\n" +
                "  'aiPlayer':true,\n" +
                "  'callbackUrl':'http://activeSaved:1234',\n" +
                "  'code':'code_activeSaved',\n" +
                "  'data':'{\\'data\\':1}',\n" +
                "  'email':'activeSaved@email.com',\n" +
                "  'game':'game room',\n" +
                "  'gameOnly':'game room',\n" +
                "  'hidden':false,\n" +
                "  'id':'activeSaved',\n" +
                "  'lastResponse':1000,\n" +
                "  'notNullReadableName':'readable_activeSaved',\n" +
                "  'readableName':'readable_activeSaved',\n" +
                "  'room':'room',\n" +
                "  'saved':true,\n" +
                "  'score':10,\n" +
                "  'teamId':1,\n" +
                "  'ticksInactive':2\n" +
                "}, {\n" +
                "  'active':false,\n" +
                "  'aiPlayer':false,\n" +
                "  'callbackUrl':'http://saved:1234',\n" +
                "  'code':'code_saved',\n" +
                "  'email':'saved@email.com',\n" +
                "  'game':'saved game room',\n" +
                "  'gameOnly':'saved game room',\n" +
                "  'hidden':false,\n" +
                "  'id':'saved',\n" +
                "  'lastResponse':3000,\n" +
                "  'notNullReadableName':'readable_saved',\n" +
                "  'readableName':'readable_saved',\n" +
                "  'room':'room',\n" +
                "  'saved':true,\n" +
                "  'score':15,\n" +
                "  'teamId':0,\n" +
                "  'ticksInactive':0\n" +
                "}]");
    }

    private void assertPlayerInfo(List<PlayerInfo> games, String expected) {
        assertEquals(expected,
                games.stream()
                        .map(info -> JsonUtils.prettyPrint(info))
                        .collect(toList()).toString());
    }

    @Test
    public void shouldGetAllActivePlayersWithSavedGamesDataSortedByName_byRoomName() {
        // given
        setupTimeService(timeService);

        Player activeSavedPlayer = createPlayer("activeSaved", "room"); // check sorting order (activeSaved > active)
        Player activePlayer = createPlayer("active", "room");
        Player activeSavedPlayerInOtherRoom = createPlayer("activeSavedInOtherRoom", "otherRoom");
        Player activePlayerInOtherRoom = createPlayer("activeInOtherRoom", "otherRoom");


        teamId(activeSavedPlayer, 1);
        teamId(activePlayer, 2);
        teamId(activeSavedPlayerInOtherRoom, 3);
        teamId(activePlayerInOtherRoom, 4);

        scores(activeSavedPlayer, 10);
        scores(activePlayer, 11);
        scores(activeSavedPlayerInOtherRoom, 12);
        scores(activePlayerInOtherRoom, 13);

        when(playerService.getAllInRoom("room")).thenReturn(Arrays.asList(activeSavedPlayer, activePlayer));
        when(playerService.getAllInRoom("otherRoom")).thenReturn(Arrays.asList(activePlayerInOtherRoom, activeSavedPlayerInOtherRoom));

        PlayerSave save1 = new PlayerSave(activeSavedPlayer);
        PlayerSave save2 = new PlayerSave(activePlayer);
        PlayerSave save3 = new PlayerSave("saved", "http://saved:1234", "saved game room", "room", 15, "data for saved");
        PlayerSave save4 = new PlayerSave(activeSavedPlayerInOtherRoom);
        PlayerSave save5 = new PlayerSave(activePlayerInOtherRoom);
        PlayerSave save6 = new PlayerSave("savedInOtherRoom", "http://savedInOtherRoom:2345", "saved game otherRoom", "otherRoom", 26, "data for savedInOtherRoom");

        when(saver.getSavedList("room")).thenReturn(Arrays.asList("activeSaved", "saved"));
        when(saver.getSavedList("otherRoom")).thenReturn(Arrays.asList("activeSavedInOtherRoom", "savedInOtherRoom"));
        when(saver.loadGame("activeSaved")).thenReturn(save1);
        when(saver.loadGame("active")).thenReturn(save2);
        when(saver.loadGame("saved")).thenReturn(save3);
        when(saver.loadGame("activeSavedInOtherRoom")).thenReturn(save4);
        when(saver.loadGame("activeInOtherRoom")).thenReturn(save5);
        when(saver.loadGame("savedInOtherRoom")).thenReturn(save6);

        fieldSave(0, "{'data':1}");
        fieldSave(1, "{'data':2}");
        fieldSave(2, "{'data':3}");
        fieldSave(3, "{'data':4}");

        createUser("activeSaved");
        createUser("active");
        createUser("saved");
        createUser("activeSavedInOtherRoom");
        createUser("activeInOtherRoom");
        createUser("savedInOtherRoom");

        // when
        List<PlayerInfo> games = saveService.getSaves("room");

        // then
        // then
        assertPlayerInfo(games,
                "[{\n" +
                "  'active':true,\n" +
                "  'aiPlayer':true,\n" +
                "  'callbackUrl':'http://active:1234',\n" +
                "  'code':'code_active',\n" +
                "  'data':'{\\'data\\':2}',\n" +
                "  'email':'active@email.com',\n" +
                "  'game':'game room',\n" +
                "  'gameOnly':'game room',\n" +
                "  'hidden':false,\n" +
                "  'id':'active',\n" +
                "  'lastResponse':2000,\n" +
                "  'notNullReadableName':'readable_active',\n" +
                "  'readableName':'readable_active',\n" +
                "  'room':'room',\n" +
                "  'saved':false,\n" +
                "  'score':11,\n" +
                "  'teamId':2,\n" +
                "  'ticksInactive':3\n" +
                "}, {\n" +
                "  'active':true,\n" +
                "  'aiPlayer':true,\n" +
                "  'callbackUrl':'http://activeSaved:1234',\n" +
                "  'code':'code_activeSaved',\n" +
                "  'data':'{\\'data\\':1}',\n" +
                "  'email':'activeSaved@email.com',\n" +
                "  'game':'game room',\n" +
                "  'gameOnly':'game room',\n" +
                "  'hidden':false,\n" +
                "  'id':'activeSaved',\n" +
                "  'lastResponse':1000,\n" +
                "  'notNullReadableName':'readable_activeSaved',\n" +
                "  'readableName':'readable_activeSaved',\n" +
                "  'room':'room',\n" +
                "  'saved':true,\n" +
                "  'score':10,\n" +
                "  'teamId':1,\n" +
                "  'ticksInactive':4\n" +
                "}, {\n" +
                "  'active':false,\n" +
                "  'aiPlayer':false,\n" +
                "  'callbackUrl':'http://saved:1234',\n" +
                "  'code':'code_saved',\n" +
                "  'email':'saved@email.com',\n" +
                "  'game':'saved game room',\n" +
                "  'gameOnly':'saved game room',\n" +
                "  'hidden':false,\n" +
                "  'id':'saved',\n" +
                "  'lastResponse':5000,\n" +
                "  'notNullReadableName':'readable_saved',\n" +
                "  'readableName':'readable_saved',\n" +
                "  'room':'room',\n" +
                "  'saved':true,\n" +
                "  'score':15,\n" +
                "  'teamId':0,\n" +
                "  'ticksInactive':0\n" +
                "}]");

        // when
        games = saveService.getSaves("otherRoom");

        // then
        // then
        assertPlayerInfo(games,
                "[{\n" +
                "  'active':true,\n" +
                "  'aiPlayer':true,\n" +
                "  'callbackUrl':'http://activeInOtherRoom:1234',\n" +
                "  'code':'code_activeInOtherRoom',\n" +
                "  'data':'{\\'data\\':4}',\n" +
                "  'email':'activeInOtherRoom@email.com',\n" +
                "  'game':'game otherRoom',\n" +
                "  'gameOnly':'game otherRoom',\n" +
                "  'hidden':false,\n" +
                "  'id':'activeInOtherRoom',\n" +
                "  'lastResponse':4000,\n" +
                "  'notNullReadableName':'readable_activeInOtherRoom',\n" +
                "  'readableName':'readable_activeInOtherRoom',\n" +
                "  'room':'otherRoom',\n" +
                "  'saved':false,\n" +
                "  'score':13,\n" +
                "  'teamId':4,\n" +
                "  'ticksInactive':2\n" +
                "}, {\n" +
                "  'active':true,\n" +
                "  'aiPlayer':true,\n" +
                "  'callbackUrl':'http://activeSavedInOtherRoom:1234',\n" +
                "  'code':'code_activeSavedInOtherRoom',\n" +
                "  'data':'{\\'data\\':3}',\n" +
                "  'email':'activeSavedInOtherRoom@email.com',\n" +
                "  'game':'game otherRoom',\n" +
                "  'gameOnly':'game otherRoom',\n" +
                "  'hidden':false,\n" +
                "  'id':'activeSavedInOtherRoom',\n" +
                "  'lastResponse':3000,\n" +
                "  'notNullReadableName':'readable_activeSavedInOtherRoom',\n" +
                "  'readableName':'readable_activeSavedInOtherRoom',\n" +
                "  'room':'otherRoom',\n" +
                "  'saved':true,\n" +
                "  'score':12,\n" +
                "  'teamId':3,\n" +
                "  'ticksInactive':3\n" +
                "}, {\n" +
                "  'active':false,\n" +
                "  'aiPlayer':false,\n" +
                "  'callbackUrl':'http://savedInOtherRoom:2345',\n" +
                "  'code':'code_savedInOtherRoom',\n" +
                "  'email':'savedInOtherRoom@email.com',\n" +
                "  'game':'saved game otherRoom',\n" +
                "  'gameOnly':'saved game otherRoom',\n" +
                "  'hidden':false,\n" +
                "  'id':'savedInOtherRoom',\n" +
                "  'lastResponse':6000,\n" +
                "  'notNullReadableName':'readable_savedInOtherRoom',\n" +
                "  'readableName':'readable_savedInOtherRoom',\n" +
                "  'room':'otherRoom',\n" +
                "  'saved':true,\n" +
                "  'score':26,\n" +
                "  'teamId':0,\n" +
                "  'ticksInactive':0\n" +
                "}]");
    }

    private void createUser(String id) {
        Registration.User user = new Registration.User() {{
            setId(id);
            setCode("code_" + id);
            setReadableName("readable_" + id);
            setEmail(id + "@email.com");
        }};
        users.add(user);

        when(registration.getUserById(id)).thenReturn(Optional.of(user));
        when(registration.getUsers()).thenReturn(users);
        setup(users).when(registration).getUsers(anyCollection());
    }

    public Stubber setup(List<Registration.User> input) {
        return doAnswer(inv -> {
            List<Registration.User> result = new LinkedList<>(input);
            Collection<String> argument = inv.getArgument(0);
            result.removeIf(it -> !argument.contains(it.getId()));
            return result;
        });
    }

    private void teamId(Player player, int teamId) {
        when(player.getTeamId()).thenReturn(teamId);
    }

    private void scores(Player player, Object score) {
        when(player.getScore()).thenReturn(score);
    }

    @Test
    public void shouldSaveAll() {
        // given
        createPlayer("first");
        createPlayer("second");

        fieldSave(0, "{'key':'value1'}");
        fieldSave(1, "{'key':'value2'}");

        // when
        long time = saveService.saveAll();

        // then
        assertSaveAll(time, "[first, second]");
    }

    public void assertSaveAll(long time, String expected) {
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(saver).saveGames(captor.capture(), eq(time));
        List<Deal> deals = captor.getValue();
        assertEquals(expected,
                deals.stream()
                        .map(Deal::getPlayer)
                        .map(Player::getId)
                        .collect(toList())
                        .toString());
    }

    @Test
    public void shouldSaveAll_forRoomName() {
        // given
        createPlayer("first", "room1");
        createPlayer("second", "room1");
        createPlayer("third", "room2");

        fieldSave(0, "{'key':'value1'}");
        fieldSave(1, "{'key':'value2'}");
        fieldSave(2, "{'key':'value3'}");

        // when
        long time = saveService.saveAll("room1");

        // then
        assertSaveAll(time, "[first, second]");
        verifyNoMoreInteractions(saver);
    }

    private void fieldSave(int index, String save) {
        when(fields.get(index).getSave()).thenReturn(new JSONObject(save));
    }

    @Test
    public void shouldLoadAll() {
        // given
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second"));
        allPlayersNotRegistered();

        PlayerSave first = save("room", "first");
        PlayerSave second = save("room", "second");

        setup(first, second).when(saver).loadAll(anyList());

        // when
        saveService.loadAll();

        // then
        verify(playerService).contains("first");
        verify(playerService).register(first);
        verify(playerService).contains("second");
        verify(playerService).register(second);
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldLoadAll_byRoomName() {
        // given
        when(saver.getSavedList("room1")).thenReturn(Arrays.asList("first", "third"));
        when(saver.getSavedList("room2")).thenReturn(Arrays.asList("second"));
        createPlayer("first", "room1");
        createPlayer("second", "room2");
        createPlayer("third", "room1");

        PlayerSave first = save("room1", "first");
        PlayerSave second = save("room2", "second");
        PlayerSave third = save("room1", "third");

        setup(first, second, third).when(saver).loadAll(anyList());

        // when
        saveService.loadAll("room1");

        // then
        verify(playerService).contains("first");
        verify(playerService).register(first);

        verify(playerService).contains("third");
        verify(playerService).register(third);

        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldLoadAll_byRoomName_whenNoPlayersInGame() {
        // given
        when(saver.getSavedList("room1")).thenReturn(Arrays.asList("first", "third"));
        when(saver.getSavedList("room2")).thenReturn(Arrays.asList("second"));

        PlayerSave first = save("room1", "first");
        PlayerSave second = save("room2", "second");
        PlayerSave third = save("room1", "third");

        setup(first, second, third).when(saver).loadAll(anyList());

        // when
        saveService.loadAll("room1");

        // then
        verify(playerService).contains("first");
        verify(playerService).register(first);

        verify(playerService).contains("third");
        verify(playerService).register(third);

        verifyNoMoreInteractions(playerService);
    }

    public Stubber setup(PlayerSave... saves) {
        return doAnswer(inv -> {
            List<PlayerSave> result = new LinkedList<>(){{
                addAll(Arrays.asList(saves));
            }};
            List<String> argument = inv.getArgument(0);
            result.removeIf(it -> !argument.contains(it.getId()));
            return result;
        });
    }

    public PlayerSave save(String room, String id) {
        PlayerSave result = mock(PlayerSave.class);
        when(result.getRoom()).thenReturn(room);
        when(result.getId()).thenReturn(id);
        return result;
    }

    private void allPlayersNotRegistered() {
        when(playerService.contains(anyString())).thenReturn(NOT_REGISTERED);
    }

    @Test
    public void shouldLoadAll_whenRegistered() {
        // given
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second"));
        allPlayersRegistered();

        PlayerSave first = save("room", "first");
        PlayerSave second = save("room", "second");

        setup(first, second).when(saver).loadAll(anyList());

        // when
        saveService.loadAll();

        // then
        verify(playerService).contains("first");
        verify(playerService).remove("first");
        verify(playerService).register(first);
        verify(playerService).contains("second");
        verify(playerService).remove("second");
        verify(playerService).register(second);
        verifyNoMoreInteractions(playerService);
    }

    private void allPlayersRegistered() {
        boolean registered = true;
        when(playerService.contains(anyString())).thenReturn(registered);
    }

    @Test
    public void shouldRemoveSave() {
        // when
        saveService.removeSave("player");

        // then
        verify(saver).delete("player");
    }

    @Test
    public void shouldRemoveAllSaves() {
        // given
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second", "third"));
        createPlayer("first", "room1");
        createPlayer("second", "room2");
        createPlayer("third", "room1");

        // when
        saveService.removeAllSaves();

        // then
        verify(saver).getSavedList();
        verify(saver).delete("first");
        verify(saver).delete("second");
        verify(saver).delete("third");
        verifyNoMoreInteractions(saver, playerService);
    }

    @Test
    public void shouldRemoveAllSaves_byRoomName() {
        // given
        when(saver.getSavedList("room1")).thenReturn(Arrays.asList("first", "third"));
        when(saver.getSavedList("room2")).thenReturn(Arrays.asList("second"));
        createPlayer("first", "room1");
        createPlayer("second", "room2");
        createPlayer("third", "room1");

        // when
        saveService.removeAllSaves("room1");

        // then
        verify(saver).getSavedList("room1");
        verify(saver).delete("first");
        verify(saver).delete("third");
        verifyNoMoreInteractions(saver, playerService);
    }

    @Test
    public void shouldRemoveAllSaves_byRoomName_whenNoPlayersInGame() {
        // given
        when(saver.getSavedList("room1")).thenReturn(Arrays.asList("first", "third"));
        when(saver.getSavedList("room2")).thenReturn(Arrays.asList("second"));

        // when
        saveService.removeAllSaves("room1");

        // then
        verify(saver).getSavedList("room1");
        verify(saver).delete("first");
        verify(saver).delete("third");
        verifyNoMoreInteractions(saver, playerService);
    }

    @Test
    public void testSaveGame_passTeamIdFromDeal() {
        // given
        setupTimeService(timeService);

        int teamId = 3;
        Player player = createPlayer("player");
        Deal deal = deals.get(player.getId());
        when(deal.getTeamId()).thenReturn(teamId);

        // when
        saveService.save(player.getId());

        // then
        verify(saver).saveGame(player, teamId, "{}", 2000L);
    }
}