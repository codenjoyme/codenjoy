package com.codenjoy.dojo.services;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 23:50
 */
public class PlayerGamesTest {

    private PlayerGames playerGames;
    private Player player;
    private Game game;
    private PlayerController controller;

    @Before
    public void setUp() throws Exception {
        player = new Player("player", "pass", "url", "game", PlayerScores.NULL, Information.NULL, Protocol.WS);
        game = mock(Game.class);
        controller = mock(PlayerController.class);

        playerGames = new PlayerGames();
        playerGames.add(player, game, controller);
    }

    @Test
    public void testRemove() throws Exception {
        assertFalse(playerGames.isEmpty());
        assertEquals(1, playerGames.size());
        PlayerGame playerGame = playerGames.get(player.getName());

        playerGames.remove(player);

        assertTrue(playerGames.isEmpty());
        assertEquals(0, playerGames.size());

        verifyRemove(playerGame);
    }

    @Test
    public void testGet() throws Exception {
        assertSame(PlayerGame.NULL, playerGames.get("bla"));

        PlayerGame playerGame = playerGames.get(player.getName());

        assertSame(player, playerGame.getPlayer());
        assertSame(game, playerGame.getGame());
        assertSame(controller, playerGame.getController());

    }

    @Test
    public void testAdd() throws Exception {
        Player otherPlayer = addOtherPlayer();

        assertFalse(playerGames.isEmpty());
        assertEquals(2, playerGames.size());

        PlayerGame playerGame = playerGames.get(otherPlayer.getName());

        assertSame(otherPlayer, playerGame.getPlayer());
    }

    @Test
    public void testIterator() throws Exception {
        Player otherPlayer = addOtherPlayer();

        Iterator<PlayerGame> iterator = playerGames.iterator();
        assertTrue(iterator.hasNext());
        assertSame(player, iterator.next().getPlayer());

        assertTrue(iterator.hasNext());
        assertSame(otherPlayer, iterator.next().getPlayer());

        assertFalse(iterator.hasNext());
    }

    private Player addOtherPlayer() {
        Player otherPlayer = new Player("player" + Calendar.getInstance().getTimeInMillis(), "pass", "url", "game",
                PlayerScores.NULL, Information.NULL, Protocol.WS);
        playerGames.add(otherPlayer, mock(Game.class), mock(PlayerController.class));
        return otherPlayer;
    }

    @Test
    public void testPlayers() throws Exception {
        Player otherPlayer = addOtherPlayer();

        List<Player> players = playerGames.players();

        assertSame(player, players.get(0));
        assertSame(otherPlayer, players.get(1));
        assertEquals(2, players.size());
    }

    @Test
    public void testClear() throws Exception {
        Player player2 = addOtherPlayer();
        Player player3 = addOtherPlayer();

        PlayerGame playerGame1 = playerGames.get(player.getName());
        PlayerGame playerGame2 = playerGames.get(player2.getName());
        PlayerGame playerGame3 = playerGames.get(player3.getName());

        assertEquals(3, playerGames.size());

        playerGames.clear();

        assertEquals(0, playerGames.size());

        verifyRemove(playerGame1);
        verifyRemove(playerGame2);
        verifyRemove(playerGame3);
    }

    private void verifyRemove(PlayerGame playerGame2) {
        verify(playerGame2.getGame()).destroy();
        verify(playerGame2.getController()).unregisterPlayerTransport(playerGame2.getPlayer());
    }
}
