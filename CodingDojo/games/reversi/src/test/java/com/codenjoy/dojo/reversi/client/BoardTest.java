package com.codenjoy.dojo.reversi.client;

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

public class BoardTest {

    private Board board;

    @Test
    public void test_isMyTurn_myColor() {
        shouldB("    " +
                " xO " +
                " Ox " +
                "    ");

        assertEquals(true, board.isMyTurn());
        assertEquals(true, board.myColor());
        assertEquals(false, board.isWhite(1, 2));
        assertEquals(true, board.isBlack(1, 2));
        assertEquals(true, board.isWhite(2, 2));
        assertEquals(false, board.isBlack(2, 2));

        shouldB("    " +
                " +O " +
                " O+ " +
                "    ");

        assertEquals(false, board.isMyTurn());
        assertEquals(false, board.myColor());
        assertEquals(false, board.isWhite(1, 2));
        assertEquals(true, board.isBlack(1, 2));
        assertEquals(true, board.isWhite(2, 2));
        assertEquals(false, board.isBlack(2, 2));

        shouldB("    " +
                " Xo " +
                " oX " +
                "    ");

        assertEquals(true, board.isMyTurn());
        assertEquals(false, board.myColor());
        assertEquals(false, board.isWhite(1, 2));
        assertEquals(true, board.isBlack(1, 2));
        assertEquals(true, board.isWhite(2, 2));
        assertEquals(false, board.isBlack(2, 2));

        shouldB("    " +
                " X. " +
                " .X " +
                "    ");

        assertEquals(false, board.isMyTurn());
        assertEquals(true, board.myColor());
        assertEquals(false, board.isWhite(1, 2));
        assertEquals(true, board.isBlack(1, 2));
        assertEquals(true, board.isWhite(2, 2));
        assertEquals(false, board.isBlack(2, 2));
    }

    private void shouldB(String string) {
        board = (Board)new Board().forString(string);
    }
}
