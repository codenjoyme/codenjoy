package com.codenjoy.dojo.quake2d.client;

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

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 05.10.13
 * Time: 11:56
 */
public class SolverTest {

    private Dice dice;
    private Solver ai;

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
        asertAI("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☻  ☼" +
                "☼☼☼☼☼☼☼", Direction.UP);

//        asertAI("☼☼☼☼☼☼☼" +
//                "☼  X  ☼" +
//                "☼     ☼" +
//                "☼     ☼" +
//                "☼ ☺   ☼" +
//                "☼  ☻  ☼" +
//                "☼☼☼☼☼☼☼", Direction.UP);

//        asertAI("☼☼☼☼☼☼☼" +
//                "☼  x  ☼" +
//                "☼ $   ☼" +
//                "☼ ☺   ☼" +
//                "☼   $ ☼" +
//                "☼  ☻  ☼" +
//                "☼☼☼☼☼☼☼", Direction.UP);
//
//        asertAI("☼☼☼☼☼☼☼" +
//                "☼  x  ☼" +
//                "☼ ☺   ☼" +
//                "☼     ☼" +
//                "☼   $ ☼" +
//                "☼$ ☻  ☼" +
//                "☼☼☼☼☼☼☼", Direction.UP);
//
//        asertAI("☼☼☼☼☼☼☼" +
//                "☼ ☺x  ☼" +
//                "☼     ☼" +
//                "☼     ☼" +
//                "☼   $ ☼" +
//                "☼$ ☻  ☼" +
//                "☼☼☼☼☼☼☼", Direction.UP);
    }

    private void asertAI(String board, Direction expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
