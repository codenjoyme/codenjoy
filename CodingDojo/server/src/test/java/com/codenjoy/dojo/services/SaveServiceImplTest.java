package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatMessage;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.mocks.MockChatService;
import com.codenjoy.dojo.services.mocks.MockGameSaver;
import com.codenjoy.dojo.services.mocks.MockPlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {
        SaveServiceImpl.class,
        MockPlayerService.class,
        MockChatService.class,
        MockRegistration.class,
        MockGameSaver.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class SaveServiceImplTest {

    @Autowired
    private SaveServiceImpl saveService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ChatService chat;

    @Autowired
    private GameSaver saver;

    private List<Player> players;

    @Before
    public void setUp() throws IOException {
        reset(playerService, chat, saver);
        players = new LinkedList<Player>();
        when(playerService.getAll()).thenReturn(players);
        when(playerService.get(anyString())).thenReturn(NullPlayer.INSTANCE);
    }

    @Test
    public void shouldSavePlayerWhenExists() {
        Player player = createPlayer("vasia");

        saveService.save("vasia");

        verify(saver).saveGame(player);
    }

    private Player createPlayer(String name) {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn(name);
        when(player.getGameName()).thenReturn(name + " game");
        when(player.getCallbackUrl()).thenReturn("http://" + name + ":1234");
        when(playerService.get(name)).thenReturn(player);
        players.add(player);
        return player;
    }

    @Test
    public void shouldNotSavePlayerWhenNotExists() {
        saveService.save("cocacola");

        verify(saver, never()).saveGame(any(Player.class));
    }

    @Test
    public void shouldLoadPlayer() {
        // given
        PlayerSave save = new PlayerSave("vasia", "url", "game", 100, "http");
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

        saveService.saveAll();

        verify(saver).saveGame(players.get(0));
        verify(saver).saveGame(players.get(1));

        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        verify(saver).saveChat(captor.capture());
        assertEquals("[[[**:**] player_one: message_one, [**:**] player_two: message_two]]",
                captor.getAllValues().toString().replaceAll("[0-9]", "*"));
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
