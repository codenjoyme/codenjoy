package com.codenjoy.dojo.reversi.client.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.reversi.client.Board;
import com.codenjoy.dojo.reversi.client.YourSolver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApofigSolverTest {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new ApofigSolver(dice);
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void should() {
        asertAI("    " +
                " xO " +
                " Ox " +
                "    ", "ACT(0,2)");

        asertAI("    " +
                "ooo " +
                " oX " +
                "    ", "ACT(0,1)");

        asertAI("    " +
                "OOO " +
                "xxx " +
                "    ", "ACT(0,0)");

        asertAI("    " +
                "ooo " +
                "ooX " +
                "o   ", "ACT(0,3)");

        asertAI("x   " +
                "OxO " +
                "OOx " +
                "O   ", "ACT(1,3)");

        asertAI("Xo  " +
                "ooo " +
                "ooX " +
                "o   ", "ACT(2,3)");

        asertAI("xxx " +
                "OOx " +
                "OOx " +
                "O   ", "ACT(3,0)");

        asertAI("XXX " +
                "ooX " +
                "ooo " +
                "o  o", "ACT(1,0)");

        asertAI("xxx " +
                "Oxx " +
                "OxO " +
                "Ox O", "ACT(2,0)");

        asertAI("XXX " +
                "oXX " +
                "ooo " +
                "oooo", "");

        asertAI("xxx " +
                "Oxx " +
                "OOO " +
                "OOOO", "ACT(3,2)");

        asertAI("XXX " +
                "oooo" +
                "ooo " +
                "oooo", "ACT(3,1)");

        asertAI("xxx " +
                "OOxO" +
                "OOOx" +
                "OOOO", "ACT(3,3)");

        asertAI("XXXo" +
                "oooo" +
                "oooX" +
                "oooo", "");
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
