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


import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;

public class PlayerGamesTest {

    private PlayerGames playerGames;
    private Player player;
    private Game game;
    private PlayerController controller;
    private PlayerController screen;
    private LazyJoystick lazyJoystick;
    private Joystick joystick;
    private PlayerSpy playerSpy;
    private Statistics statistics;
    private List<GameType> gameTypes = new LinkedList<>();
    private List<Game> games = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        Arrays.asList(joystick, game, controller, screen, statistics, playerSpy)
                .forEach(it -> {
                    if (it != null) reset(it);
                });

        player = createPlayer("game", "player");

        game = mock(Game.class);
        games.add(game);

        joystick = mock(Joystick.class);
        when(game.getJoystick()).thenReturn(joystick);

        controller = mock(PlayerController.class);
        screen = mock(PlayerController.class);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                lazyJoystick = invocation.getArgumentAt(1, LazyJoystick.class);
                return null;
            }
        }).when(controller).registerPlayerTransport(eq(player), any(LazyJoystick.class));


        statistics = mock(Statistics.class);
        playerSpy = mock(PlayerSpy.class);
        when(statistics.newPlayer(any(Player.class))).thenReturn(playerSpy);
        playerGames = new PlayerGames(statistics);

        playerGames.onAddPlayer((player, joystick) -> {
            controller.registerPlayerTransport(player, joystick);
            screen.registerPlayerTransport(player, null);
        });
        playerGames.onRemovePlayer((player, joystick) -> {
            controller.unregisterPlayerTransport(player);
            screen.unregisterPlayerTransport(player);
        });

        playerGames.add(player, game);
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
        assertSame(NullPlayerGame.INSTANCE, playerGames.get("bla"));

        PlayerGame playerGame = playerGames.get(player.getName());

        assertSame(player, playerGame.getPlayer());
        assertSame(game, playerGame.getGame());

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

    private Player addOtherPlayer(String game) {
        Player otherPlayer = createPlayer(game, "player" + Calendar.getInstance().getTimeInMillis());

        Game anotherGame = mock(Game.class);
        games.add(anotherGame);

        playerGames.add(otherPlayer, anotherGame);
        return otherPlayer;
    }

    private Player createPlayer(String game, String name) {
        GameService gameService = mock(GameService.class);
        GameType gameType = mock(GameType.class);
        gameTypes.add(gameType);
        PlayerScores scores = mock(PlayerScores.class);
        when(gameType.getPlayerScores(anyInt())).thenReturn(scores);
        when(gameType.name()).thenReturn(game);
        when(gameService.getGame(anyString())).thenReturn(gameType);

        return new Player(name, "url", gameType, scores, mock(Information.class));
    }

    @Test
    public void testPlayers() throws Exception {
        Player otherPlayer = addOtherPlayer();

        List<Player> players = playerGames.players();

        assertSame(player, players.get(0));
        assertSame(otherPlayer, players.get(1));
        assertEquals(2, players.size());
    }

    private Player addOtherPlayer() {
        return addOtherPlayer("game");
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

    private void verifyRemove(PlayerGame playerGame) {
        verify(playerGame.getGame()).destroy();
        verify(controller).unregisterPlayerTransport(playerGame.getPlayer());
        verify(screen).unregisterPlayerTransport(playerGame.getPlayer());
    }

    @Test
    public void testGetGameTypes() {
        Player player2 = addOtherPlayer("game2");
        playerGames.add(player2, mock(Game.class));

        List<GameType> gameTypes = playerGames.getGameTypes();

        assertEquals(2, gameTypes.size());
        assertEquals("game", gameTypes.get(0).name());
        assertEquals("game2", gameTypes.get(1).name());
    }

    @Test
    public void shouldTickLazyJoystickWhenTick() {
        // given
        lazyJoystick.right();

        // when
        playerGames.tick();

        // then
        verify(joystick).right();
    }

    @Test
    public void shouldQuietTickPlayerSpyWhenTick() {
        // given
        lazyJoystick.right();
        doThrow(new RuntimeException()).when(playerSpy).act();

        // when
        playerGames.tick();

        // then
        verify(playerSpy).act();
    }

    @Test
    public void shouldTickPlayerSpyWhenTick() {
        // given
        lazyJoystick.right();

        // when
        playerGames.tick();

        // then
        verify(playerSpy).act();
    }

    @Test
    public void shouldTickStatisticsWhenTick() {
        // when
        playerGames.tick();

        // then
        verify(statistics).tick();
    }

    @Test
    public void shouldQuietTickStatisticsWhenTick() {
        // given
        doThrow(new RuntimeException()).when(statistics).tick();

        // when
        playerGames.tick();

        // then
        verify(statistics).tick();
    }

    @Test
    public void shouldNewGameWhenGameOverWhenTick() {
        // given
        when(game.isGameOver()).thenReturn(true);

        // when
        playerGames.tick();

        // then
        verify(game).newGame();
    }

    @Test
    public void shouldQuietNewGameWhenGameOverWhenTick() {
        // given
        when(game.isGameOver()).thenReturn(true);
        doThrow(new RuntimeException()).when(game).newGame();

        // when
        playerGames.tick();

        // then
        verify(game).newGame();
    }

    @Test
      public void shouldTickGameWhenTickIfSingleGameType() {
        // given
        GameType gameType = playerGames.getGameTypes().get(0);
        when(gameType.getMultiplayerType()).thenReturn(MultiplayerType.MULTIPLE);

        // when
        playerGames.tick();

        // then
        verify(game).tick();
    }

    @Test
    public void shouldTickGameWhenTickIfNotSingleGameType() {
        // given
        GameType gameType = playerGames.getGameTypes().get(0);
        when(gameType.getMultiplayerType()).thenReturn(MultiplayerType.SINGLE);

        // when
        playerGames.tick();

        // then
        verify(game).tick();
    }

    @Test
    public void shouldNotRemovePlayerIfNoActive() {
        // given
        when(statistics.getPlayers(Statistics.WAIT_TICKS_MORE_OR_EQUALS, PlayerGames.TICKS_FOR_REMOVE)).thenReturn(Arrays.asList(player));
        assertEquals(1, playerGames.size());

        // when
        playerGames.tick();

        // then
        assertEquals(1, playerGames.size());
    }

    @Test
    public void shouldTickGameTypeAfterAllGames() {
        // given
        addOtherPlayer();
        addOtherPlayer();

        // when
        playerGames.tick();

        // then

        InOrder order = inOrder(games.get(0), games.get(1), games.get(2),
                gameTypes.get(0), gameTypes.get(1), gameTypes.get(2));

        order.verify(games.get(0)).tick();
        order.verify(games.get(1)).tick();
        order.verify(games.get(2)).tick();
        order.verify(gameTypes.get(0)).tick();
        order.verify(gameTypes.get(1)).tick();
        order.verify(gameTypes.get(2)).tick();
    }


}
