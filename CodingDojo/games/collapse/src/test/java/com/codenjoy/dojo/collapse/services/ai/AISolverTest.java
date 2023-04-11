package com.codenjoy.dojo.collapse.services.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.games.collapse.Board;
import com.codenjoy.dojo.games.collapse.YourSolver;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.dice.MockDice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AISolverTest {

    private MockDice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = new MockDice();
        ai = new YourSolver(dice);
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void should() {
        asertAI("☼☼☼☼☼☼☼" +
                "☼12345☼" +
                "☼67891☼" +
                "☼23456☼" +
                "☼78912☼" +
                "☼34567☼" +
                "☼☼☼☼☼☼☼", "ACT(3,4),RIGHT");
    }

    private void asertAI(String board, String expected) {
        String actual = ai.get(board(board));
        assertEquals(expected, actual);
    }

    private void dice(Direction direction) {
        dice.then(direction.value());
    }
}
