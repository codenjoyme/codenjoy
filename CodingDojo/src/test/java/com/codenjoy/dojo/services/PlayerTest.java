package com.codenjoy.dojo.services;

import com.codenjoy.dojo.snake.services.SnakePlayerScores;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:45 AM
 */
public class PlayerTest {

    @Test
    public void shouldCollectAllData() {
        SnakePlayerScores scores = mock(SnakePlayerScores.class);
        when(scores.getScore()).thenReturn(123);

        Information info = mock(Information.class);
        Player player = new Player("vasia", "http://valia:8888/", scores, info);

        assertEquals("vasia", player.toString());

        assertEquals("http://valia:8888/", player.getCallbackUrl());
        assertEquals("vasia", player.getName());
        assertEquals(0, player.getCurrentLevel());
        assertEquals(123, player.getScore());

        player.setName("katya");
        assertEquals("katya", player.getName());

        player.setCallbackUrl("http://katya:8888/");
        assertEquals("http://katya:8888/", player.getCallbackUrl());
    }
}
