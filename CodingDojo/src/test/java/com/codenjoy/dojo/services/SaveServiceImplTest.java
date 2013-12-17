package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.mocks.MockChatService;
import com.codenjoy.dojo.services.mocks.MockGameSaver;
import com.codenjoy.dojo.services.mocks.MockPlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
        MockChatService.class,
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
        Player.PlayerBuilder playerBuilder = new Player.PlayerBuilder("vasia", "password", "url", 100, "http");
        playerBuilder.setInformation("info");
        when(saver.loadGame("vasia")).thenReturn(playerBuilder);

        // when
        saveService.load("vasia");

        // then
        verify(playerService).register(playerBuilder);
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

}
