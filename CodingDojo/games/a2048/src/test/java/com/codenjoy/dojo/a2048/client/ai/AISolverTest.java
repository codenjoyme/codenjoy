package com.codenjoy.dojo.a2048.client.ai;

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


import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AISolverTest {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new AISolver(dice);
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
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
