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


import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PlayerGamesTest {

    private PlayerGames playerGames;
    private Player player;
    private List<GameType> gameTypes = new LinkedList<>();
    private Map<Player, Closeable> ais = new HashMap<>();
    private List<Joystick> joysticks = new LinkedList<>();
    private List<Joystick> lazyJoysticks = new LinkedList<>();

    @Before
    public void setUp() throws Exception {
        playerGames = new PlayerGames();
        player = createPlayer("game", "player");
    }

    private PlayerGame removed;

    @Test
    public void testRemove() throws Exception {
        assertFalse(playerGames.isEmpty());
        assertEquals(1, playerGames.size());
        PlayerGame playerGame = playerGames.get(player.getName());
        GameField field = playerGame.getGame().getField();
        playerGames.onRemove(pg -> removed = pg);

        playerGames.remove(player);

        assertTrue(playerGames.isEmpty());
        assertEquals(0, playerGames.size());

        verifyRemove(playerGame, field);
        assertSame(removed, playerGame);
    }

    @Test
    public void testGet() throws Exception {
        assertSame(NullPlayerGame.INSTANCE, playerGames.get("bla"));

        PlayerGame playerGame = playerGames.get(player.getName());

        assertSame(player, playerGame.getPlayer());
//        assertSame(game, playerGame.getGame());

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
        return createPlayer(game, "player" + Calendar.getInstance().getTimeInMillis());
    }

    private Player createPlayer(String game, String name) {
        GameService gameService = mock(GameService.class);
        GameType gameType = mock(GameType.class);
        gameTypes.add(gameType);
        PlayerScores scores = mock(PlayerScores.class);
        when(gameType.getPlayerScores(anyInt())).thenReturn(scores);
        when(gameType.name()).thenReturn(game);
        when(gameService.getGame(anyString())).thenReturn(gameType);

        Player player = new Player(name, "url", gameType, scores, mock(Information.class));
        Closeable ai = mock(Closeable.class);
        ais.put(player, ai);
        player.setAI(ai);

        playerGames.onAdd(playerGame -> lazyJoysticks.add(playerGame.getJoystick()));

        TestUtils.Env env = TestUtils.getPlayerGame(playerGames, player, inv -> mock(GameField.class));
        joysticks.add(env.joystick);

        return player;
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
    public void testGetAll() throws Exception {
        Player secondPlayer = addOtherPlayer();
        Player thirdPlayer = addOtherPlayer("game2");

        List<PlayerGame> result = playerGames.getAll("game");

        assertEquals(2, result.size());
        assertSame(player, result.get(0).getPlayer());
        assertSame(secondPlayer, result.get(1).getPlayer());

        List<PlayerGame> result2 = playerGames.getAll("game2");

        assertEquals(1, result2.size());
        assertSame(thirdPlayer, result2.get(0).getPlayer());
    }

    private Player addOtherPlayer() {
        return addOtherPlayer("game");
    }

    @Test
    public void testClear() throws Exception {
        Player player2 = addOtherPlayer();
        Player player3 = addOtherPlayer();

        PlayerGame playerGame1 = playerGames.get(player.getName());
        GameField field1 = playerGame1.getGame().getField();

        PlayerGame playerGame2 = playerGames.get(player2.getName());
        GameField field2 = playerGame2.getGame().getField();

        PlayerGame playerGame3 = playerGames.get(player3.getName());
        GameField field3 = playerGame3.getGame().getField();

        assertEquals(3, playerGames.size());

        playerGames.clear();

        assertEquals(0, playerGames.size());

        verifyRemove(playerGame1, field1);
        verifyRemove(playerGame2, field2);
        verifyRemove(playerGame3, field3);
    }

    private void verifyRemove(PlayerGame playerGame, GameField field) {
        verify(field).remove(playerGame.getGame().getPlayer());
        verify(ais.get(playerGame.getPlayer())).close();
    }

    @Test
    public void testGetGameTypes() {
        Player player2 = addOtherPlayer("game2");
        playerGames.add(player2, null);

        List<GameType> gameTypes = playerGames.getGameTypes();

        assertEquals(2, gameTypes.size());
        assertEquals("game", gameTypes.get(0).name());
        assertEquals("game2", gameTypes.get(1).name());
    }

    @Test
    public void shouldTickLazyJoystickWhenTick() {
        // given
        lazyJoysticks.get(0).right();

        // when
        playerGames.tick();

        // then
        verify(joysticks.get(0)).right();
    }

    @Test
    public void shouldTickGameType() {
        // given
        addOtherPlayer("game2");
        addOtherPlayer("game3");
        addOtherPlayer("game2"); // второй игрок к уже существующей game2

        // when
        playerGames.tick();

        // then

        InOrder order = inOrder(gameTypes.get(0), gameTypes.get(1), gameTypes.get(2));

        order.verify(gameTypes.get(0)).quietTick();
        order.verify(gameTypes.get(1)).quietTick();
        order.verify(gameTypes.get(2)).quietTick();
    }


}
