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


import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.utils.JsonUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.PlayerSave.NULL;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SaveServiceImplTest {

    private Registration registration;
    private SaveServiceImpl saveService;
    private PlayerService playerService;
    private PlayerGames playerGames;
    private GameSaver saver;

    private List<Player> players;
    private List<GameField> fields;
    public static final boolean NOT_REGISTERED = false;

    @Before
    public void setUp() {
        saveService = new SaveServiceImpl(){{
            this.playerGames = SaveServiceImplTest.this.playerGames = new PlayerGames();
            this.players = SaveServiceImplTest.this.playerService = mock(PlayerService.class);
            this.saver = SaveServiceImplTest.this.saver = mock(GameSaver.class);
            this.registration = SaveServiceImplTest.this.registration = mock(Registration.class);
        }};

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
        verify(saver).saveGame(player, "{\"key\":\"value\"}", time);
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
        when(player.getEventListener()).thenReturn(mock(InformationCollector.class));
        when(playerService.get(id)).thenReturn(player);
        players.add(player);

        Answer<Object> answerCreateGame = inv1 -> {
            GameField field = mock(GameField.class);
            fields.add(field);
            return field;
        };

        TestUtils.Env env = TestUtils.getPlayerGame(
                playerGames,
                player,
                room,
                answerCreateGame,
                MultiplayerType.SINGLE,
                null,
                parameters -> "board"
        );
        PlayerGame playerGame = env.playerGame;

        return playerGame.getPlayer();
    }

    @Test
    public void shouldNotSavePlayer_whenNotExists() {
        // when
        saveService.save("cocacola");

        // then
        verify(saver, never()).saveGame(any(Player.class), any(String.class), anyLong());
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
                "'score':0}", JsonUtils.cleanSorted(actual));
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
                "'score':0}", JsonUtils.cleanSorted(actual));
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
                "'score':0}", JsonUtils.cleanSorted(actual));
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldGetAllActivePlayersWithSavedGamesDataSortedByName() {
        // given
        Player activeSavedPlayer = createPlayer("activeSaved"); // check sorting order (activeSaved > active)
        Player activePlayer = createPlayer("active");
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
        assertEquals(3, games.size());

        PlayerInfo active = games.get(0);
        PlayerInfo activeSaved = games.get(1);
        PlayerInfo saved = games.get(2);

        assertEquals("active", active.getId());
        assertEquals("code_active", active.getCode());
        assertEquals("readable_active", active.getReadableName());
        assertEquals("http://active:1234", active.getCallbackUrl());
        assertEquals("game room", active.getGame());
        assertEquals("{\"data\":2}", active.getData());
        assertEquals(11, active.getScore());
        assertEquals("room", active.getRoom());
        assertEquals(true, active.isAiPlayer());
        assertTrue(active.isActive());
        assertFalse(active.isSaved());

        assertEquals("activeSaved", activeSaved.getId());
        assertEquals("code_activeSaved", activeSaved.getCode());
        assertEquals("readable_activeSaved", activeSaved.getReadableName());
        assertEquals("http://activeSaved:1234", activeSaved.getCallbackUrl());
        assertEquals("game room", activeSaved.getGame());
        assertEquals("{\"data\":1}", activeSaved.getData());
        assertEquals(10, activeSaved.getScore());
        assertEquals("room", activeSaved.getRoom());
        assertEquals(true, activeSaved.isAiPlayer());
        assertTrue(activeSaved.isActive());
        assertTrue(activeSaved.isSaved());

        assertEquals("saved", saved.getId());
        assertEquals("code_saved", saved.getCode());
        assertEquals("readable_saved", saved.getReadableName());
        assertEquals("http://saved:1234", saved.getCallbackUrl());
        assertEquals("saved game room", saved.getGame());
        assertNull(saved.getData());
        assertEquals(15, saved.getScore());
        assertEquals("room", saved.getRoom());
        assertEquals(false, saved.isAiPlayer());
        assertFalse(saved.isActive());
        assertTrue(saved.isSaved());
    }

    @Test
    public void shouldGetAllActivePlayersWithSavedGamesDataSortedByName_byRoomName() {
        // given
        Player activeSavedPlayer = createPlayer("activeSaved", "room"); // check sorting order (activeSaved > active)
        Player activePlayer = createPlayer("active", "room");
        Player activeSavedPlayerInOtherRoom = createPlayer("activeSavedInOtherRoom", "otherRoom");
        Player activePlayerInOtherRoom = createPlayer("activeInOtherRoom", "otherRoom");
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
        assertEquals(3, games.size());

        PlayerInfo active = games.get(0);
        PlayerInfo activeSaved = games.get(1);
        PlayerInfo saved = games.get(2);

        assertEquals("active", active.getId());
        assertEquals("code_active", active.getCode());
        assertEquals("readable_active", active.getReadableName());
        assertEquals("http://active:1234", active.getCallbackUrl());
        assertEquals("game room", active.getGame());
        assertEquals("{\"data\":2}", active.getData());
        assertEquals(11, active.getScore());
        assertEquals("room", active.getRoom());
        assertEquals(true, active.isAiPlayer());
        assertTrue(active.isActive());
        assertFalse(active.isSaved());

        assertEquals("activeSaved", activeSaved.getId());
        assertEquals("code_activeSaved", activeSaved.getCode());
        assertEquals("readable_activeSaved", activeSaved.getReadableName());
        assertEquals("http://activeSaved:1234", activeSaved.getCallbackUrl());
        assertEquals("game room", activeSaved.getGame());
        assertEquals("{\"data\":1}", activeSaved.getData());
        assertEquals(10, activeSaved.getScore());
        assertEquals("room", activeSaved.getRoom());
        assertEquals(true, activeSaved.isAiPlayer());
        assertTrue(activeSaved.isActive());
        assertTrue(activeSaved.isSaved());

        assertEquals("saved", saved.getId());
        assertEquals("code_saved", saved.getCode());
        assertEquals("readable_saved", saved.getReadableName());
        assertEquals("http://saved:1234", saved.getCallbackUrl());
        assertEquals("saved game room", saved.getGame());
        assertNull(saved.getData());
        assertEquals(15, saved.getScore());
        assertEquals("room", saved.getRoom());
        assertEquals(false, saved.isAiPlayer());
        assertFalse(saved.isActive());
        assertTrue(saved.isSaved());

        // when
        games = saveService.getSaves("otherRoom");

        // then
        assertEquals(3, games.size());

        active = games.get(0);
        activeSaved = games.get(1);
        saved = games.get(2);

        assertEquals("activeInOtherRoom", active.getId());
        assertEquals("code_activeInOtherRoom", active.getCode());
        assertEquals("readable_activeInOtherRoom", active.getReadableName());
        assertEquals("http://activeInOtherRoom:1234", active.getCallbackUrl());
        assertEquals("game otherRoom", active.getGame());
        assertEquals("{\"data\":4}", active.getData());
        assertEquals(13, active.getScore());
        assertEquals("otherRoom", active.getRoom());
        assertEquals(true, active.isAiPlayer());
        assertTrue(active.isActive());
        assertFalse(active.isSaved());

        assertEquals("activeSavedInOtherRoom", activeSaved.getId());
        assertEquals("code_activeSavedInOtherRoom", activeSaved.getCode());
        assertEquals("readable_activeSavedInOtherRoom", activeSaved.getReadableName());
        assertEquals("http://activeSavedInOtherRoom:1234", activeSaved.getCallbackUrl());
        assertEquals("game otherRoom", activeSaved.getGame());
        assertEquals("{\"data\":3}", activeSaved.getData());
        assertEquals(12, activeSaved.getScore());
        assertEquals("otherRoom", activeSaved.getRoom());
        assertEquals(true, activeSaved.isAiPlayer());
        assertTrue(activeSaved.isActive());
        assertTrue(activeSaved.isSaved());

        assertEquals("savedInOtherRoom", saved.getId());
        assertEquals("code_savedInOtherRoom", saved.getCode());
        assertEquals("readable_savedInOtherRoom", saved.getReadableName());
        assertEquals("http://savedInOtherRoom:2345", saved.getCallbackUrl());
        assertEquals("saved game otherRoom", saved.getGame());
        assertNull(saved.getData());
        assertEquals(26, saved.getScore());
        assertEquals("otherRoom", saved.getRoom());
        assertEquals(false, saved.isAiPlayer());
        assertFalse(saved.isActive());
        assertTrue(saved.isSaved());
    }

    private void createUser(String id) {
        Optional<Registration.User> user = Optional.of(new Registration.User() {{
            setCode("code_" + id);
            setReadableName("readable_" + id);
        }});

        when(registration.getUserById(id)).thenReturn(user);
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
        verify(saver).saveGame(players.get(0), "{\"key\":\"value1\"}", time);
        verify(saver).saveGame(players.get(1), "{\"key\":\"value2\"}", time);
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
        verify(saver).saveGame(players.get(0), "{\"key\":\"value1\"}", time);
        verify(saver).saveGame(players.get(1), "{\"key\":\"value2\"}", time);
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

        PlayerSave first = mock(PlayerSave.class);
        PlayerSave second = mock(PlayerSave.class);
        when(saver.loadGame("first")).thenReturn(first);
        when(saver.loadGame("second")).thenReturn(second);

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

        PlayerSave first = mock(PlayerSave.class);
        when(first.getRoom()).thenReturn("room1");

        PlayerSave second = mock(PlayerSave.class);
        when(second.getRoom()).thenReturn("room2");

        PlayerSave third = mock(PlayerSave.class);
        when(third.getRoom()).thenReturn("room1");

        when(saver.loadGame("first")).thenReturn(first);
        when(saver.loadGame("second")).thenReturn(second);
        when(saver.loadGame("third")).thenReturn(third);

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

        PlayerSave first = mock(PlayerSave.class);
        when(first.getRoom()).thenReturn("room1");

        PlayerSave second = mock(PlayerSave.class);
        when(second.getRoom()).thenReturn("room2");

        PlayerSave third = mock(PlayerSave.class);
        when(third.getRoom()).thenReturn("room1");

        when(saver.loadGame("first")).thenReturn(first);
        when(saver.loadGame("second")).thenReturn(second);
        when(saver.loadGame("third")).thenReturn(third);

        // when
        saveService.loadAll("room1");

        // then
        verify(playerService).contains("first");
        verify(playerService).register(first);

        verify(playerService).contains("third");
        verify(playerService).register(third);

        verifyNoMoreInteractions(playerService);
    }


    private void allPlayersNotRegistered() {
        when(playerService.contains(anyString())).thenReturn(NOT_REGISTERED);
    }

    @Test
    public void shouldLoadAll_whenRegistered() {
        // given
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second"));
        allPlayersRegistered();

        PlayerSave first = mock(PlayerSave.class);
        PlayerSave second = mock(PlayerSave.class);
        when(saver.loadGame("first")).thenReturn(first);
        when(saver.loadGame("second")).thenReturn(second);

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
}