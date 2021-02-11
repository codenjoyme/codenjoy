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


import com.codenjoy.dojo.services.lock.LockedGame;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.nullobj.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class PlayerGameTest {

    private Player player;
    private Game game;
    private PlayerGame playerGame;
    private GameType gameType;

    @Before
    public void setup() {
        gameType = PlayerTest.mockGameType("game");
        player = new Player("player", "url", gameType,
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE);
        game = mock(Game.class);

        playerGame = new PlayerGame(player, game, "room");
    }

    @Test
    public void testWorkWithJoystick() {
        // given
        Joystick joystick = playerGame.getJoystick();
        Joystick real = mock(Joystick.class);
        when(game.getJoystick()).thenReturn(real);

        // when
        joystick.act(1, 2, 3);
        joystick.down();
        playerGame.tick();

        // then
        InOrder inOrder = inOrder(real);
        inOrder.verify(real).act(1, 2, 3);
        inOrder.verify(real).down();
    }

    @Test
    public void testGetField() {
        // given
        GameField field = mock(GameField.class);
        when(game.getField()).thenReturn(field);

        // when then
        assertSame(field, playerGame.getField());
    }

    @Test
    public void getGetGameType() {
        // when then
        assertSame(gameType, playerGame.getGameType());
    }

    @Test
    public void testEquals_null() {
        // when then
        assertEquals(false, playerGame.equals(null));
    }

    @Test
    public void testEquals_withOtherObject() {
        // when then
        assertEquals(false, playerGame.equals(new Object()));
    }

    @Test
    public void testEquals_nullInstance() {
        // when then
        assertEquals(false, playerGame.equals(NullPlayerGame.INSTANCE));
        assertEquals(true, NullPlayerGame.INSTANCE.equals(NullPlayerGame.INSTANCE));
        assertEquals(true, NullPlayerGame.INSTANCE.equals(NullPlayer.INSTANCE));
    }
    @Test
    public void testEquals_withPlayer() {
        // given
        Player otherPlayer = new Player("other player", "other url", PlayerTest.mockGameType("game"),
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE);

        // when then
        assertEquals(false, playerGame.equals(otherPlayer));
        assertEquals(true, playerGame.equals(player));
    }

    @Test
    public void testEquals_playerGameWithPlayer() {
        // given
        GameType gameType = PlayerTest.mockGameType("game");
        Player otherPlayer = new Player("other player", "other url", gameType,
                NullPlayerScores.INSTANCE, NullInformation.INSTANCE);
        PlayerGame player = new PlayerGame(otherPlayer, NullGame.INSTANCE, gameType.name());

        // when then
        assertEquals(false, playerGame.equals(player));
        assertEquals(true, playerGame.equals(playerGame));
    }

    @Test
    public void testEquals_roomName() {
        // when then
        assertEquals(true, playerGame.equals("room"));
        assertEquals(false, playerGame.equals("otherRoom"));
    }

    @Test
    public void testEquals_playerGameWithGame() {
        // given
        Game game = new LockedGame(new ReentrantReadWriteLock()).wrap(mock(Game.class));
        PlayerGame player = new PlayerGame(null, game, null);

        // when then
        assertEquals(false, playerGame.equals(player));
        assertEquals(false, player.equals(playerGame));
    }

    @Test
    public void testEquals_player() {
        // given
        PlayerGame player = new PlayerGame(null, game, null);
        PlayerGame player2 = new PlayerGame(null, mock(Game.class), null);

        // when then
        assertEquals(true, playerGame.equals(player));
        assertEquals(true, player.equals(playerGame));
        assertEquals(false, playerGame.equals(player2));
        assertEquals(false, player2.equals(playerGame));
    }

    @Test
    public void testEquals_field() {
        // given
        Game realGame = mock(Game.class);
        GameField field = mock(GameField.class);
        when(realGame.getField()).thenReturn(field);
        Game game = new LockedGame(new ReentrantReadWriteLock()).wrap(realGame);
        PlayerGame player = new PlayerGame(null, game, null);

        // when then
        assertEquals(false, playerGame.equals(field));
        assertEquals(true, player.equals(field));
    }

    @Test
    public void testHashCode() {
        // when then
        assertEquals(2096629736, playerGame.hashCode());
    }

    @Test
    public void testRemove() {
        // given
        boolean[] removed = {false};
        Consumer<PlayerGame> onRemove = playerGame -> removed[0] = true;

        // when
        playerGame.remove(onRemove);

        // then
        verify(game).close();
        assertEquals(true, removed[0]);
    }

    @Test
    public void testGetPlayer() {
        // when then
        assertSame(player, playerGame.getPlayer());
    }

    @Test
    public void testGetGame() {
        // when then
        assertSame(game, playerGame.getGame());
    }

    @Test
    public void testToString() {
        // when then
        assertEquals(String.format("PlayerGame[player=player, roomName=room, game=%s]",
                game.getClass().getSimpleName()),
                playerGame.toString());
    }
    
    @Test 
    public void testSetRoomName_alsoUpdatePlayer() {
        // given
        assertEquals("room", playerGame.getRoomName());
        assertEquals("room", playerGame.getPlayer().getRoomName());
        
        // when 
        playerGame.setRoomName("otherRoom");
        
        // then
        assertEquals("otherRoom", playerGame.getRoomName());
        assertEquals("otherRoom", playerGame.getPlayer().getRoomName());
    }
}
