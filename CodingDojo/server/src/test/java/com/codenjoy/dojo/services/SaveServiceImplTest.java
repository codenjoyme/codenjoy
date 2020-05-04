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
        Player player = createPlayer("vasia");
        fieldSave(0, "{'key':'value'}");

        long time = saveService.save("vasia");

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
        when(player.getGameName()).thenReturn("game " + room);
        when(player.getRoomName()).thenReturn(room);
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
        PlayerSave save = new PlayerSave("vasia", "url", "room", "game", 100, null);
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
        PlayerSave save = new PlayerSave("vasia", "127.0.0.2", "room", "game", 100, null);
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
        PlayerSave save = new PlayerSave("vasia", "127.0.0.2", "room", "game", 0, "{'save':'data'}");
        when(saver.loadGame("vasia")).thenReturn(save);
        allPlayersNotRegistered();

        // when
        saveService.load("vasia", "room", "game", "{'save':'data'}");

        // then
        verify(saver).loadGame("vasia");
        verifyNoMoreInteractions(saver);

        verify(playerService).contains("vasia");
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);
        verify(playerService).register(captor.capture());
        PlayerSave actual = captor.getValue();
        assertEquals("{'callbackUrl':'127.0.0.2'," +
                "'gameName':'game'," +
                "'id':'vasia'," +
                "'roomName':'room'," +
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
        saveService.load("vasia", "room", "game", "{'save':'data'}");

        // then
        verify(saver).loadGame("vasia");
        verifyNoMoreInteractions(saver);

        verify(playerService).contains("vasia");
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);
        verify(playerService).register(captor.capture());
        PlayerSave actual = captor.getValue();
        assertEquals("{'callbackUrl':'" + SaveServiceImpl.DEFAULT_CALLBACK_URL + "'," +
                "'gameName':'game'," +
                "'id':'vasia'," +
                "'roomName':'room'," +
                "'save':'{'save':'data'}'," +
                "'score':0}", JsonUtils.cleanSorted(actual));
        verifyNoMoreInteractions(playerService);
    }

    @Test
    public void shouldLoadPlayerWithExternalSave_whenRegistered() {
        // given
        PlayerSave save = new PlayerSave("vasia", "127.0.0.2", "room", "game", 0, "{'save':'data'}");
        when(saver.loadGame("vasia")).thenReturn(save);
        allPlayersRegistered();

        // when
        saveService.load("vasia", "room", "game", "{'save':'data'}");

        // then
        verify(saver).loadGame("vasia");
        verifyNoMoreInteractions(saver);

        verify(playerService).contains("vasia");
        verify(playerService).remove("vasia");  // << difference
        ArgumentCaptor<PlayerSave> captor = ArgumentCaptor.forClass(PlayerSave.class);
        verify(playerService).register(captor.capture());
        PlayerSave actual = captor.getValue();
        assertEquals("{'callbackUrl':'127.0.0.2'," +
                "'gameName':'game'," +
                "'id':'vasia'," +
                "'roomName':'room'," +
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
        PlayerSave save3 = new PlayerSave("saved", "http://saved:1234", "room", "saved game room", 15, "data for saved");

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
        assertEquals("game room", active.getGameName());
        assertEquals("{\"data\":2}", active.getData());
        assertEquals(11, active.getScore());
        assertEquals("room", active.getRoomName());
        assertEquals(true, active.isAiPlayer());
        assertTrue(active.isActive());
        assertFalse(active.isSaved());

        assertEquals("activeSaved", activeSaved.getId());
        assertEquals("code_activeSaved", activeSaved.getCode());
        assertEquals("readable_activeSaved", activeSaved.getReadableName());
        assertEquals("http://activeSaved:1234", activeSaved.getCallbackUrl());
        assertEquals("game room", activeSaved.getGameName());
        assertEquals("{\"data\":1}", activeSaved.getData());
        assertEquals(10, activeSaved.getScore());
        assertEquals("room", activeSaved.getRoomName());
        assertEquals(true, activeSaved.isAiPlayer());
        assertTrue(activeSaved.isActive());
        assertTrue(activeSaved.isSaved());

        assertEquals("saved", saved.getId());
        assertEquals("code_saved", saved.getCode());
        assertEquals("readable_saved", saved.getReadableName());
        assertEquals("http://saved:1234", saved.getCallbackUrl());
        assertEquals("saved game room", saved.getGameName());
        assertNull(saved.getData());
        assertEquals(15, saved.getScore());
        assertEquals("room", saved.getRoomName());
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
        createPlayer("first");
        createPlayer("second");

        fieldSave(0, "{'key':'value1'}");
        fieldSave(1, "{'key':'value2'}");

        long time = saveService.saveAll();

        verify(saver).saveGame(players.get(0), "{\"key\":\"value1\"}", time);
        verify(saver).saveGame(players.get(1), "{\"key\":\"value2\"}", time);
    }

    @Test
    public void shouldSaveAll_forRoomName() {
        createPlayer("first", "room1");
        createPlayer("second", "room1");
        createPlayer("third", "room2");

        fieldSave(0, "{'key':'value1'}");
        fieldSave(1, "{'key':'value2'}");
        fieldSave(2, "{'key':'value3'}");

        long time = saveService.saveAll("room1");

        verify(saver).saveGame(players.get(0), "{\"key\":\"value1\"}", time);
        verify(saver).saveGame(players.get(1), "{\"key\":\"value2\"}", time);
        verifyNoMoreInteractions(saver);
    }

    private void fieldSave(int index, String save) {
        when(fields.get(index).getSave()).thenReturn(new JSONObject(save));
    }

    @Test
    public void shouldLoadAll() {
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second"));
        allPlayersNotRegistered();

        PlayerSave first = mock(PlayerSave.class);
        PlayerSave second = mock(PlayerSave.class);
        when(saver.loadGame("first")).thenReturn(first);
        when(saver.loadGame("second")).thenReturn(second);

        saveService.loadAll();

        verify(playerService).contains("first");
        verify(playerService).register(first);
        verify(playerService).contains("second");
        verify(playerService).register(second);
        verifyNoMoreInteractions(playerService);
    }

    private void allPlayersNotRegistered() {
        boolean NOT_REGISTERED = false;
        when(playerService.contains(anyString())).thenReturn(NOT_REGISTERED);
    }

    @Test
    public void shouldLoadAll_whenRegistered() {
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second"));
        allPlayersRegistered();

        PlayerSave first = mock(PlayerSave.class);
        PlayerSave second = mock(PlayerSave.class);
        when(saver.loadGame("first")).thenReturn(first);
        when(saver.loadGame("second")).thenReturn(second);

        saveService.loadAll();

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
        saveService.removeSave("player");

        verify(saver).delete("player");
    }

    @Test
    public void shouldRemoveAllSaves() {
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second"));

        saveService.removeAllSaves();

        verify(saver).delete("first");
        verify(saver).delete("second");
        verifyNoMoreInteractions(playerService);
    }

}
