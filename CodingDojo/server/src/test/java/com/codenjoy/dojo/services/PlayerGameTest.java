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


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class PlayerGameTest {

    private Player player;
    private Game game;
    private PlayerController controller;
    private PlayerController screen;
    private PlayerGame playerGame;
    private LazyJoystick lazyJoystick;

    @Before
    public void setup() {
        player = new Player("player", "url", PlayerTest.mockGameType("game"), 
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE);
        game = mock(Game.class);
        lazyJoystick = mock(LazyJoystick.class);
        controller = mock(PlayerController.class);
        screen = mock(PlayerController.class);

        playerGame = new PlayerGame(player, game, lazyJoystick);
    }

    @Test
    public void testEquals() throws Exception {
        assertFalse(playerGame.equals(null));
        assertFalse(playerGame.equals(new Object()));

        assertFalse(playerGame.equals(NullPlayerGame.INSTANCE));
        assertTrue(NullPlayerGame.INSTANCE.equals(NullPlayerGame.INSTANCE));
        assertTrue(NullPlayerGame.INSTANCE.equals(NullPlayer.INSTANCE));

        Player otherPlayer = new Player("other player", "other url", PlayerTest.mockGameType("game"),
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE);
        assertFalse(playerGame.equals(otherPlayer));
        assertTrue(playerGame.equals(player));

        PlayerGame otherPlayerGame = new PlayerGame(otherPlayer, NullGame.INSTANCE,
                lazyJoystick);
        assertFalse(playerGame.equals(otherPlayerGame));
        assertTrue(playerGame.equals(playerGame));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(2096629736, playerGame.hashCode());
    }

    @Test
    public void testRemove() throws Exception {
        playerGame.remove((player, joystick) -> {
            screen.unregisterPlayerTransport(player);
            controller.unregisterPlayerTransport(player);
        });

        verify(game).close();
        verify(controller).unregisterPlayerTransport(player);
        verify(screen).unregisterPlayerTransport(player);
    }

    @Test
    public void testGetPlayer() throws Exception {
        assertSame(player, playerGame.getPlayer());
    }

    @Test
    public void testGetGame() throws Exception {
        assertSame(game, playerGame.getGame());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("PlayerGame[player=player, game=%s]",
                game.getClass().getSimpleName()),
                playerGame.toString());
    }
}
