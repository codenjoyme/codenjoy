package com.codenjoy.dojo.quadro.client;

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
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class YourSolverTest {

    private Solver ai;
    private void dice(int number) {
        when(dice.next(anyInt())).thenReturn(number);
    }

    private Dice dice;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new YourSolver(dice);
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void should() {
        dice(1);
        assertAI("    " +
                "   o" +
                "   o" +
                " xxo", "ACT(1)");

        dice(2);
        assertAI("     " +
                "     " +
                "   o " +
                "   ox" +
                " xxoo", "ACT(2)");

        dice(0);
        assertAI("      " +
                "      " +
                "      " +
                "   o  " +
                "   ox " +
                " xxoox", "ACT(0)");

        dice(5);
        assertAI("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "   o     " +
                "   ox    " +
                " xxoox   ", "ACT(5)");

        dice(6);
        assertAI("                " +
                "                " +
                "                " +
                "                " +
                "                " +
                "                " +
                "   o            " +
                "   ox           " +
                " xxoox          " +
                "                " +
                "                " +
                "                " +
                "                " +
                "                " +
                "                " +
                "                ", "ACT(6)");
    }

    private void assertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }
}
