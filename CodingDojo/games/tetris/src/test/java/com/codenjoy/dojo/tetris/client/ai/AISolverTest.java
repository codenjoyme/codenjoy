package com.codenjoy.dojo.tetris.client.ai;

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
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.client.BoardTest;
import com.codenjoy.dojo.tetris.client.YourSolver;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
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

    @Test
    public void should() {
        asertAI("...OO..." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........",
                "O",
                pt(3, 8),
                new String[] {"O", "O", "O", "O"},
                "LEFT,LEFT,LEFT,DOWN");

        asertAI("..OO...." +
                "..OO...." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "........",
                "O",
                pt(2, 7),
                new String[] {"O", "O", "O", "O"},
                "LEFT,LEFT,DOWN");

        asertAI("........" +
                ".OO....." +
                ".OO....." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........",
                "O",
                pt(1, 6),
                new String[] {"O", "O", "O", "O"},
                "LEFT,DOWN");

        asertAI("........" +
                "........" +
                "OO......" +
                "OO......" +
                "........" +
                "........" +
                "........" +
                "........",
                "O",
                pt(0, 5),
                new String[] {"O", "O", "O", "O"},
                "DOWN");

        asertAI("...OO..." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "OO......" +
                "OO......",
                "O",
                pt(3, 8),
                new String[] {"O", "O", "O", "O"},
                "LEFT,DOWN");

        asertAI("..OO...." +
                "..OO...." +
                "........" +
                "........" +
                "........" +
                "........" +
                "OO......" +
                "OO......",
                "O",
                pt(2, 7),
                new String[] {"O", "O", "O", "O"},
                "DOWN");

        asertAI("...OO..." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "OOOO...." +
                "OOOO....",
                "O",
                pt(3, 8),
                new String[] {"O", "O", "O", "O"},
                "RIGHT,DOWN");

        asertAI("....OO.." +
                "....OO.." +
                "........" +
                "........" +
                "........" +
                "........" +
                "OOOO...." +
                "OOOO....",
                "O",
                pt(4, 7),
                new String[] {"O", "O", "O", "O"},
                "DOWN");

        asertAI("...OO..." +
                "........" +
                "........" +
                "........" +
                "........" +
                "........" +
                "OOOOOO.." +
                "OOOOOO..",
                "O",
                pt(3, 8),
                new String[] {"O", "O", "O", "O"},
                "RIGHT,RIGHT,RIGHT,DOWN");

        asertAI("....OO.." +
                "....OO.." +
                "........" +
                "........" +
                "........" +
                "........" +
                "OOOOOO.." +
                "OOOOOO..",
                "O",
                pt(4, 7),
                new String[] {"O", "O", "O", "O"},
                "RIGHT,RIGHT,DOWN");

        asertAI("........" +
                ".....OO." +
                ".....OO." +
                "........" +
                "........" +
                "........" +
                "OOOOOO.." +
                "OOOOOO..",
                "O",
                pt(5, 6),
                new String[] {"O", "O", "O", "O"},
                "RIGHT,DOWN");

        asertAI(".................." + // TODO не может у стенки развернуться
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                ".................." +
                "................L." +
                "................L." +
                "................LL" +
                ".................." +
                ".................." +
                "I......I..I.I....." +
                "I.....II..I.I...I." +
                "IJOOOOIILLI.IIOOI." +
                ".IOOOOJILLJIIIJIJJ" +
                ".IIIJJJIIJJIIJJIJI",
                "L",
                pt(16, 8),
                new String[] {"O", "O", "O", "O"},
                "ACT(2),RIGHT,DOWN");
    }

    private void asertAI(String glass, String figureType,
                         Point point, String[] futureFigures,
                         String expected)
    {
        String actual = ai.get(BoardTest.getBoard(glass, figureType, point, futureFigures));
        assertEquals(expected, actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
