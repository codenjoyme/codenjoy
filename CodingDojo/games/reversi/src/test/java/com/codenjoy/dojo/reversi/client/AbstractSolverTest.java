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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class AbstractSolverTest {
    protected Dice dice;
    protected Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = getSolver();
    }

    protected abstract Solver getSolver();

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    protected void asertAI(String board) {
        String expected = getExpected(board);
        asertAI(board.replace('+', ' '), expected);
    }

    private String getExpected(String board) {
        LengthToXY xy = new LengthToXY((int) Math.sqrt(board.length()));
        int index = board.indexOf('+');
        if (index == -1) {
            return "";
        }
        Point pt = xy.getXY(index);
        return String.format("ACT(%s,%s)", pt.getX(), pt.getY());
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
