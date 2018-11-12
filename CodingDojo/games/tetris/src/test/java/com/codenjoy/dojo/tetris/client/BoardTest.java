package com.codenjoy.dojo.tetris.client;

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

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.tetris.model.Elements;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

/**
 * Created by indigo on 2018-11-09.
 */
public class BoardTest {

    @Test
    public void test() {
        Board board = getBoard(
                "......." +
                "......I" +
                "..LL..I" +
                "...LI.I" +
                ".SSLI.I" +
                "SSOOIOO" +
                "..OOIOO",
                "T",
                pt(1, 2),
                new String[]{"I", "O", "L", "Z"});

        GlassBoard glass = board.getGlass();

        assertEquals(Elements.NONE, glass.getAt(0, 0));
        assertEquals(false, glass.isAt(0, 0, Elements.YELLOW));
        assertEquals(true, glass.isAt(0, 0, Elements.NONE));
        assertEquals(true, glass.isAt(0, 0, Elements.ORANGE, Elements.NONE));
        assertEquals(true, glass.isFree(0, 0));

        assertEquals(Elements.YELLOW, glass.getAt(2, 0));
        assertEquals(true, glass.isAt(2, 0, Elements.YELLOW));
        assertEquals(false, glass.isAt(2, 0, Elements.NONE));
        assertEquals(false, glass.isAt(2, 0, Elements.ORANGE, Elements.NONE));
        assertEquals(false, glass.isFree(2, 0));

        assertEquals(Elements.GREEN, glass.getAt(2, 2));
        assertEquals(false, glass.isAt(2, 2, Elements.YELLOW));
        assertEquals(false, glass.isAt(2, 2, Elements.NONE));
        assertEquals(false, glass.isAt(2, 2, Elements.ORANGE, Elements.NONE));
        assertEquals(false, glass.isFree(2, 2));

        assertEquals(Elements.ORANGE, glass.getAt(3, 4));
        assertEquals(false, glass.isAt(3, 4, Elements.YELLOW));
        assertEquals(false, glass.isAt(3, 4, Elements.NONE));
        assertEquals(true, glass.isAt(3, 4, Elements.ORANGE, Elements.NONE));
        assertEquals(false, glass.isFree(3, 0));

        assertEquals("[[0,1], [1,1], [1,2], [2,2]]",
                glass.get(Elements.GREEN).toString());

        assertEquals("[[2,4], [3,2], [3,3], [3,4]]",
                glass.get(Elements.ORANGE).toString());

        assertEquals("[[0,1], [1,1], [1,2], [2,2], [2,4], [3,2], [3,3], [3,4]]",
                glass.get(Elements.GREEN, Elements.ORANGE).toString());

        assertEquals("[., L, ., L, ., I, ., .]",
                glass.getNear(3, 4).toString());
        assertEquals(false, glass.isNear(3, 4, Elements.PURPLE));
        assertEquals(true, glass.isNear(3, 4, Elements.BLUE));
        assertEquals(1, glass.countNear(3, 4, Elements.BLUE));
        assertEquals(2, glass.countNear(3, 4, Elements.ORANGE));

        assertEquals("[S, S, ., O, ., O, L, L]",
                glass.getNear(pt(2, 2)).toString());
        assertEquals(true, glass.isNear(pt(2, 2), Elements.ORANGE));
        assertEquals(false, glass.isNear(pt(2, 2), Elements.BLUE));
        assertEquals(2, glass.countNear(pt(2, 2), Elements.ORANGE));
        assertEquals(2, glass.countNear(pt(2, 2), Elements.GREEN));

        assertEquals("[1,2]", board.getCurrentFigurePoint().toString());
        assertEquals("T", board.getCurrentFigureType().toString());
        assertEquals("[I, O, L, Z]", board.getFutureFigures().toString());

        assertEquals("[[0,1], [1,1], [1,2], [2,0], " +
                "[2,1], [2,2], [2,4], [3,0], " +
                "[3,1], [3,2], [3,3], [3,4], " +
                "[4,0], [4,1], [4,2], [4,3], " +
                "[5,0], [5,1], [6,0], [6,1], " +
                "[6,2], [6,3], [6,4], [6,5]]", glass.getFigures().toString());

        assertEquals("[[0,0], [0,2], [0,3], [0,4], " +
                "[0,5], [0,6], [1,0], [1,3], " +
                "[1,4], [1,5], [1,6], [2,3], " +
                "[2,5], [2,6], [3,5], [3,6], " +
                "[4,4], [4,5], [4,6], [5,2], " +
                "[5,3], [5,4], [5,5], [5,6], " +
                "[6,6]]", glass.getFreeSpace().toString());

    }

    public static Board getBoard(String glass, String figureType,
                                 Point point, String[] futureFigures)
    {
        JSONObject result = getJson(glass, figureType, point, futureFigures);
        return (Board) new Board().forString(result.toString());
    }

    public static JSONObject getJson(final String glass, final String figureType, final Point point, final String[] futureFigures) {
        return new JSONObject(){{
                put("layers", new JSONArray(){{
                    put(glass);
                }});
                put("currentFigureType", figureType);
                put("currentFigurePoint", new JSONObject(point));
                put("futureFigures", new JSONArray(){{
                    Arrays.stream(futureFigures)
                            .forEach(s -> this.put(s));
                }});
            }};
    }

}
