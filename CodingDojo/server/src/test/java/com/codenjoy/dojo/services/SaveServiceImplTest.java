package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.mocks.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.*;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {
        SaveServiceImpl.class,
        MockPlayerService.class,
        MockPlayerGames.class,
        MockChatService.class,
        MockStatistics.class,
        MockRegistration.class,
        MockGameSaver.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class SaveServiceImplTest {

    @Autowired
    private SaveServiceImpl saveService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlayerGames playerGames;

    @Autowired
    private ChatService chat;

    @Autowired
    private GameSaver saver;

    private List<Player> players;
    private List<Game> games;

    @Before
    public void setUp() throws IOException {
        reset(playerService, chat, saver);
        players = new LinkedList<Player>();
        games = new LinkedList<Game>();
        when(playerService.getAll()).thenReturn(players);
        when(playerService.get(anyString())).thenReturn(NullPlayer.INSTANCE);
    }

    @Test
    public void shouldSavePlayerWhenExists() {
        Player player = createPlayer("vasia");
        when(games.get(0).getSave()).thenReturn("{'key':'value'}");

        saveService.save("vasia");

        verify(saver).saveGame(player, "{'key':'value'}");
    }

    private Player createPlayer(String name) {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        when(player.getGameName()).thenReturn(name + " game");
        when(player.getCallbackUrl()).thenReturn("http://" + name + ":1234");
        when(playerService.get(name)).thenReturn(player);
        players.add(player);

        Game game = mock(Game.class);
        games.add(game);

        playerGames.add(player, game, mock(PlayerController.class));

        return player;
    }

    @Test
    public void shouldNotSavePlayerWhenNotExists() {
        saveService.save("cocacola");

        verify(saver, never()).saveGame(any(Player.class), any(String.class));
    }

    @Test
    public void shouldLoadPlayer() {
        // given
        PlayerSave save = new PlayerSave("vasia", "url", "game", 100, "http", null);
        when(saver.loadGame("vasia")).thenReturn(save);

        // when
        saveService.load("vasia");

        // then
        verify(playerService).register(save);
    }

    @Test
    public void shouldGetAllActivePlayersWithSavedGamesDataSortedByName() {
        // given
        createPlayer("activeSaved"); // check sorting order (activeSaved > active)
        createPlayer("active");

        when(saver.getSavedList()).thenReturn(Arrays.asList("activeSaved", "saved"));

        // when
        List<PlayerInfo> games = saveService.getSaves();

        // then
        assertEquals(3, games.size());

        PlayerInfo active = games.get(0);
        PlayerInfo activeSaved = games.get(1);
        PlayerInfo saved = games.get(2);

        assertEquals("active", active.getName());
        assertEquals("http://active:1234", active.getCallbackUrl());
        assertEquals("active game", active.getGameName());
        assertTrue(active.isActive());
        assertFalse(active.isSaved());

        assertEquals("activeSaved", activeSaved.getName());
        assertEquals("http://activeSaved:1234", activeSaved.getCallbackUrl());
        assertEquals("activeSaved game", activeSaved.getGameName());
        assertTrue(activeSaved.isActive());
        assertTrue(activeSaved.isSaved());

        assertEquals("saved", saved.getName());
        assertNull(saved.getCallbackUrl());
        assertNull(saved.getGameName());
        assertFalse(saved.isActive());
        assertTrue(saved.isSaved());
    }

    @Test
    public void testSaveAll() {
        createPlayer("first");
        createPlayer("second");
        when(chat.getMessages()).thenReturn(Arrays.asList(
                new ChatMessage(new Date(), "player_one", "message_one"),
                new ChatMessage(new Date(), "player_two", "message_two")));
        when(games.get(0).getSave()).thenReturn("{'key':'value1'}");
        when(games.get(1).getSave()).thenReturn("{'key':'value2'}");

        saveService.saveAll();

        verify(saver).saveGame(players.get(0), "{'key':'value1'}");
        verify(saver).saveGame(players.get(1), "{'key':'value2'}");

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(saver).saveChat(captor.capture());
        try {
            assertEquals("[[[**:**] player_one: message_one, [**:**] player_two: message_two]]",
                    captor.getAllValues().toString().replaceAll("[0-9]", "*"));
        } catch (Error e) {
            assertEquals("[[[**** days ago, **:**] player_one: message_one, [**** days ago, **:**] player_two: message_two]]",
                    captor.getAllValues().toString().replaceAll("[0-9]", "*"));
        }
    }

    @Test
    public void testLoadAll() {
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second"));

        PlayerSave first = mock(PlayerSave.class);
        PlayerSave second = mock(PlayerSave.class);
        when(saver.loadGame("first")).thenReturn(first);
        when(saver.loadGame("second")).thenReturn(second);

        List<ChatMessage> list = new LinkedList<ChatMessage>();
        when(saver.loadChat()).thenReturn(list);

        saveService.loadAll();

        verify(playerService).register(first);
        verify(playerService).register(second);
        verifyNoMoreInteractions(playerService);

        verify(saver).loadChat();
        verify(chat).setMessages(list);
    }

    @Test
    public void testRemoveSave() {
        saveService.removeSave("player");

        verify(saver).delete("player");
    }

    @Test
    public void testRemoveAllSaves() {
        when(saver.getSavedList()).thenReturn(Arrays.asList("first", "second"));

        saveService.removeAllSaves();

        verify(saver).delete("first");
        verify(saver).delete("second");
        verifyNoMoreInteractions(playerService);
    }

}
