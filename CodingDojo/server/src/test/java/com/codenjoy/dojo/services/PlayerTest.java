package com.codenjoy.dojo.services;

import com.codenjoy.dojo.snake.services.Scores;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.*;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:45 AM
 */
public class PlayerTest {

    @Test
    public void shouldCollectAllData() {
        Scores scores = mock(Scores.class);
        when(scores.getScore()).thenReturn(123);

        Information info = mock(Information.class);
        String game1 = "game";
        Player player = new Player("vasia", "http://valia:8888/", mockGameType(game1), scores, info, null);

        assertEquals("vasia", player.toString());

        assertEquals("http://valia:8888/", player.getCallbackUrl());
        assertEquals("vasia", player.getName());
        assertEquals(null, player.getPassword());
        assertNull(player.getCode());
        assertEquals(0, player.getCurrentLevel());
        assertEquals(123, player.getScore());
        assertEquals("game", player.getGameName());

        player.setName("katya");
        assertEquals("katya", player.getName());

        player.setCallbackUrl("http://katya:8888/");
        assertEquals("http://katya:8888/", player.getCallbackUrl());

        player.setPassword("passpass");
        assertNull(player.getCode());
        assertEquals("passpass", player.getPassword());

        player.clearScore();
        verify(scores).clear();
    }

    public static GameType mockGameType(String name) {
        GameType game = mock(GameType.class);
        when(game.name()).thenReturn(name);
        return game;
    }
}
