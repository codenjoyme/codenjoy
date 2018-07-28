package com.codenjoy.dojo.tetris.client;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.model.Hero;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        asertAI("       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "       ",
                "T",
                pt(1, 2),
                Direction.DOWN);
    }

    private void asertAI(String board, String figureType, Point point, Direction expected) {
        JSONObject result = new JSONObject();

        JSONArray array = new JSONArray();
        result.put("layers", array);
        array.put(board);
        array.put(board);

        result.put("currentFigureType", figureType);
        result.put("currentFigurePoint", point);
        result.put("futureFigures", "[]");

        String actual = ai.get(board(result.toString()));
        assertEquals(String.format("message('%s')", expected.toString()),
                actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
