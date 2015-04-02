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
    private Tickable lazyJoystick;

    @Before
    public void setup() {
        player = new Player("player", "url", PlayerTest.mockGameType("game"), 
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE, Protocol.WS);
        game = mock(Game.class);
        lazyJoystick = mock(Tickable.class);
        controller = mock(PlayerController.class);

        playerGame = new PlayerGame(player, game, controller, lazyJoystick);
    }

    @Test
    public void testEquals() throws Exception {
        assertFalse(playerGame.equals(null));
        assertFalse(playerGame.equals(new Object()));

        assertFalse(playerGame.equals(NullPlayerGame.INSTANCE));
        assertTrue(NullPlayerGame.INSTANCE.equals(NullPlayerGame.INSTANCE));
        assertTrue(NullPlayerGame.INSTANCE.equals(NullPlayer.INSTANCE));

        Player otherPlayer = new Player("other player", "other url", PlayerTest.mockGameType("game"),
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE, Protocol.WS);
        assertFalse(playerGame.equals(otherPlayer));
        assertTrue(playerGame.equals(player));

        PlayerGame otherPlayerGame = new PlayerGame(otherPlayer, NullGame.INSTANCE, NullPlayerController.INSTANCE, lazyJoystick);
        assertFalse(playerGame.equals(otherPlayerGame));
        assertTrue(playerGame.equals(playerGame));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(2096629736, playerGame.hashCode());
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
