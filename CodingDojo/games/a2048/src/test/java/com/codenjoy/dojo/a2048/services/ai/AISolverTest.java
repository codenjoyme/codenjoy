package com.codenjoy.dojo.a2048.services.ai;

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
import com.codenjoy.dojo.games.a2048.Board;
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
        ai = new AISolver(dice, 7);
    }

    private Board givenBd(String board) {
        return (Board)new Board().forString(board);
    }

    @Test
    public void should2() {
        asertAI("2  I4" +
                "4  HA" +
                "8  CC" +
                "A   G" +
                "B   8", Direction.DOWN);

        asertAI("2   4" +
                "4   A" +
                "8  IC" +
                "A  HG" +
                "B  C8", Direction.UP); // TODO вот мы и зависли :)
    }

    @Test
    public void should3() {
        asertAI("A4  A" +
                "B2  B" +
                "C   C" +
                "4   4" +
                "2   2", Direction.DOWN);

        asertAI("A   A" +
                "B   B" +
                "C   C" +
                "44  4" +
                "22  2", Direction.RIGHT);
    }

    @Test
    public void should() {
        assertEquals(1, givenBd(
                " 22  " +
                "     " +
                "     " +
                "     " +
                "     ").getSumCountFor(Direction.LEFT));

        assertEquals(2, givenBd(
                " 22  " +
                "  22 " +
                "     " +
                "     " +
                "     ").getSumCountFor(Direction.LEFT));

        assertEquals(2, givenBd(
                " 222 " +
                "  22 " +
                "     " +
                "     " +
                "     ").getSumCountFor(Direction.LEFT));


    }


    private void asertAI(String board, Direction expected) {
        String actual = ai.get(givenBd(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction direction) {
        dice.then(direction.value());
    }
}
