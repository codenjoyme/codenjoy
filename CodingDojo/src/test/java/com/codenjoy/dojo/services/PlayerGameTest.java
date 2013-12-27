package com.codenjoy.dojo.services;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 23:37
 */
public class PlayerGameTest {

    private Player player;
    private Game game;
    private PlayerController controller;
    private PlayerGame playerGame;

    @Before
    public void setup() {
        player = new Player("player", "pass", "url", PlayerScores.NULL, Information.NULL, Protocol.WS);
        game = mock(Game.class);
        controller = mock(PlayerController.class);

        playerGame = new PlayerGame(player, game, controller);
    }

    @Test
    public void testEquals() throws Exception {
        assertFalse(playerGame.equals(null));
        assertFalse(playerGame.equals(new Object()));

        assertFalse(playerGame.equals(PlayerGame.NULL));
        assertTrue(PlayerGame.NULL.equals(PlayerGame.NULL));
        assertTrue(PlayerGame.NULL.equals(Player.NULL));

        Player otherPlayer = new Player("other player", "some other pass", "other url", PlayerScores.NULL, Information.NULL, Protocol.WS);
        assertFalse(playerGame.equals(otherPlayer));
        assertTrue(playerGame.equals(player));

        PlayerGame otherPlayerGame = new PlayerGame(otherPlayer, Game.NULL, PlayerController.NULL);
        assertFalse(playerGame.equals(otherPlayerGame));
        assertTrue(playerGame.equals(playerGame));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(2062563134, playerGame.hashCode());
    }

    @Test
    public void testRemove() throws Exception {
        playerGame.remove();

        verify(game).destroy();
        verify(controller).unregisterPlayerTransport(player);
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
    public void testGetController() throws Exception {
        assertSame(controller, playerGame.getController());
    }

    @Test
    public void testToString() throws Exception {
        assertEquals(String.format("PlayerGame[player=player, game=%s, controller=%s]",
                game.getClass().getSimpleName(),
                controller.getClass().getSimpleName()),
                playerGame.toString());
    }
}
