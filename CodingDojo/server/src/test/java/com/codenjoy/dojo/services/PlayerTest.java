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


import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class PlayerTest {

    @Test
    public void shouldCollectAllData() {
        PlayerScores scores = mock(PlayerScores.class);
        when(scores.getScore()).thenReturn(123);

        Information info = mock(Information.class);
        String game1 = "game";
        Player player = new Player("vasia", "http://valia:8888/", mockGameType(game1), scores, info);

        assertEquals("vasia", player.toString());

        assertEquals("http://valia:8888/", player.getCallbackUrl());
        assertEquals("vasia", player.getId());
        assertEquals(null, player.getPassword());
        assertNull(player.getCode());
        assertEquals(123, player.getScore());
        assertEquals("game", player.getGameName());

        player.setId("katya");
        assertEquals("katya", player.getId());

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
