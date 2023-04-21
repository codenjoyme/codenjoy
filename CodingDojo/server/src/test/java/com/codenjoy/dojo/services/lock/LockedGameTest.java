package com.codenjoy.dojo.services.lock;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.services.Game;
import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class LockedGameTest {

    public static LockedGame getLockedGame() {
        ReadWriteLock lock = mock(ReadWriteLock.class);
        when(lock.readLock()).thenReturn(mock(Lock.class));
        when(lock.writeLock()).thenReturn(mock(Lock.class));
        return new LockedGame(lock);
    }

    @Test
    public void testWrapUnwrap() {
        // given
        Game game = mock(Game.class);

        // when
        Game lockedGame = getLockedGame().wrap(game);

        // then
        assertSame(game, ((LockedGame)lockedGame).getWrapped());
        assertSame(game, LockedGame.unwrap(lockedGame));
    }

    @Test
    public void testEquals() {
        // given
        Game game = mock(Game.class);
        Game game2 = mock(Game.class);
        Game lockedGame = getLockedGame().wrap(game);

        // when then
        assertEquals(true, LockedGame.equals(game, lockedGame));
        assertEquals(true, LockedGame.equals(lockedGame, game));
        assertEquals(true, LockedGame.equals(game, game));
        assertEquals(true, LockedGame.equals(lockedGame, lockedGame));

        assertEquals(false, LockedGame.equals(lockedGame, null));
        assertEquals(false, LockedGame.equals(null, lockedGame));
        assertEquals(false, LockedGame.equals(game, null));
        assertEquals(false, LockedGame.equals(null, game));

        assertEquals(false, LockedGame.equals(lockedGame, game2));
        assertEquals(false, LockedGame.equals(game2, lockedGame));
        assertEquals(false, LockedGame.equals(game, game2));
        assertEquals(false, LockedGame.equals(game2, game));
    }

    @Test
    public void testDelegate_getBoardAsString() {
        // given
        Game game = mock(Game.class);
        when(game.getBoardAsString(any())).thenReturn("board");
        Game lockedGame = getLockedGame().wrap(game);

        // when
        String result = (String) lockedGame.getBoardAsString(true, 1, "string");

        // then
        assertEquals("board", result);
        verify(game).getBoardAsString(true, 1, "string");
    }

    @Test
    public void testDelegate_sameBoard() {
        // given
        Game game = mock(Game.class);
        when(game.sameBoard()).thenReturn(true);
        Game lockedGame = getLockedGame().wrap(game);

        // when
        boolean result = lockedGame.sameBoard();

        // then
        assertEquals(true, result);
        verify(game).sameBoard();
    }
}