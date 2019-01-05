package com.codenjoy.dojo.client;

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
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

/**
 * Created by indigo on 2016-09-22.
 */
public class AbstractLayeredBoardTest {

    public static final int LAYER_1 = 0;
    public static final int LAYER_2 = 1;
    public static final int LAYER_3 = 2;
    public static final int LAYER_4 = 3;

    private AbstractLayeredBoard board;

    public static AbstractLayeredBoard board(String boardString) {
        return (AbstractLayeredBoard)new AbstractLayeredBoard<Elements>(){
            @Override
            public Elements valueOf(char ch) {
                return Elements.valueOf(ch);
            }
        }.forString(boardString);
    }

    @Before
    public void before() {
        board = board("{\"layers\":[\"" +
                "1111" +
                "1221" +
                "1331" +
                "1111" +
                "\", \"" +
                "    " +
                " 4  " +
                "  4 " +
                "    " +
                "\"]}");
    }

    @Test
    public void shouldWork_toString() {
        assertEquals(
                "Board:\n" +
                "1111\n" +
                "1221\n" +
                "1331\n" +
                "1111\n" +
                "\n" +
                "    \n" +
                " 4  \n" +
                "  4 \n" +
                "    \n", board.toString());
    }

    @Test
    public void shouldWork_toString_whenThreeLayers() {
        board = board("{\"layers\":[\"" +
                "1111" +
                "1221" +
                "1331" +
                "1111" +
                "\", \"" +
                "    " +
                " 4  " +
                "  4 " +
                "    " +
                "\", \"" +
                "    " +
                "  5 " +
                " 5  " +
                "    " +
                "\"]}");

        assertEquals(
                "[[1, 1, 1, 1], " +
                "[1, 2, 3, 1], " +
                "[1, 2, 3, 1], " +
                "[1, 1, 1, 1]]",
                Arrays.deepToString(board.getField(LAYER_1)));

        assertEquals(
                "[[ ,  ,  ,  ], " +
                "[ , 4,  ,  ], " +
                "[ ,  , 4,  ], " +
                "[ ,  ,  ,  ]]",
                Arrays.deepToString(board.getField(LAYER_2)));

        assertEquals(
                "[[ ,  ,  ,  ], " +
                "[ ,  , 5,  ], " +
                "[ , 5,  ,  ], " +
                "[ ,  ,  ,  ]]",
                Arrays.deepToString(board.getField(LAYER_3)));
    }

    @Test
    public void shouldWork_toString_whenOneLayer() {
        board = board(
                "1111" +
                "1221" +
                "1331" +
                "1111");

        assertEquals(
                "Board:\n" +
                "1111\n" +
                "1221\n" +
                "1331\n" +
                "1111\n", board.toString());
    }

    @Test
    public void shouldWork_canGetSourceJson() {
        board = board("{\"layers\":[\"" +
                "1111" +
                "1221" +
                "1331" +
                "1111" +
                "\", \"" +
                "    " +
                " 4  " +
                "  4 " +
                "    " +
                "\"], \"key\":\"value\"}");

        assertEquals("value", board.source.get("key"));
        assertEquals("[\"1111122113311111\",\"     4    4     \"]", board.source.getJSONArray("layers").toString());
    }

    @Test
    public void shouldWork_getField() {
        assertEquals(
                "[[1, 1, 1, 1], " +
                "[1, 2, 3, 1], " +
                "[1, 2, 3, 1], " +
                "[1, 1, 1, 1]]",
                Arrays.deepToString(board.getField(LAYER_1)));

        assertEquals(
                "[[ ,  ,  ,  ], " +
                "[ , 4,  ,  ], " +
                "[ ,  , 4,  ], " +
                "[ ,  ,  ,  ]]",
                Arrays.deepToString(board.getField(LAYER_2)));
    }

    @Test
    public void shouldWork_getAt_layer1() {
        assertEquals(Elements.ONE, board.getAt(LAYER_1, 0, 0));
        assertEquals(Elements.TWO, board.getAt(LAYER_1, 2, 1));
        assertEquals(Elements.THREE, board.getAt(LAYER_1, 2, 2));
    }

    @Test
    public void shouldWork_getAt_layer2() {
        assertEquals(Elements.FOUR, board.getAt(LAYER_2, 2, 2));
        assertEquals(Elements.NONE, board.getAt(LAYER_2, 1, 2));
    }

    @Test
    public void shouldWork_isAt_layer1() {
        assertEquals(true, board.isAt(LAYER_1, 0, 0, Elements.ONE));
        assertEquals(false, board.isAt(LAYER_1, 1, 1, Elements.ONE));
        assertEquals(false, board.isAt(LAYER_1, 2, 2, Elements.ONE));

        assertEquals(true, board.isAt(LAYER_1, 1, 1, Elements.TWO, Elements.THREE));
        assertEquals(true, board.isAt(LAYER_1, 2, 2, Elements.TWO, Elements.THREE));
        assertEquals(false, board.isAt(LAYER_1, 2, 2, Elements.TWO, Elements.ONE));
    }

    @Test
    public void shouldWork_isAt_layer2() {
        assertEquals(true, board.isAt(LAYER_2, 1, 1, Elements.FOUR));
        assertEquals(false, board.isAt(LAYER_2, 1, 2, Elements.FOUR));
        assertEquals(false, board.isAt(LAYER_2, 2, 2, Elements.NONE));

        assertEquals(true, board.isAt(LAYER_2, 1, 1, Elements.FOUR, Elements.NONE));
        assertEquals(true, board.isAt(LAYER_2, 2, 2, Elements.FOUR, Elements.NONE));
        assertEquals(false, board.isAt(LAYER_2, 2, 2, Elements.TWO, Elements.ONE));
    }

    @Test
    public void shouldWork_isNear_layer1() {
        assertEquals(true, board.isNear(LAYER_1, 1, 1, Elements.ONE));
        assertEquals(false, board.isNear(LAYER_1, 5, 5, Elements.TWO));
    }

    @Test
    public void shouldWork_isNear_layer2() {
        assertEquals(true, board.isNear(LAYER_2, 1, 1, Elements.NONE));
        assertEquals(false, board.isNear(LAYER_2, 0, 3, Elements.FOUR));
    }

    @Test
    public void shouldWork_getNear_layer1() {
        assertEquals("[1, 1, 1, 1, 3, 1, 2, 3]", board.getNear(LAYER_1, 1, 1).toString());
        assertEquals("[2, 3, 1, 2, 1, 1, 1, 1]", board.getNear(LAYER_1, 2, 2).toString());
        assertEquals("[3, 1, 1]", board.getNear(LAYER_1, 3, 3).toString());
        assertEquals("[]", board.getNear(LAYER_1, 5, 5).toString());
        assertEquals("[1]", board.getNear(LAYER_1, -1, -1).toString());
    }

    @Test
    public void shouldWork_getNear_layer2() {
        assertEquals("[ ,  ,  ,  ,  ,  ,  , 4]", board.getNear(LAYER_2, 1, 1).toString());
        assertEquals("[4,  ,  ,  ,  ,  ,  ,  ]", board.getNear(LAYER_2, 2, 2).toString());
        assertEquals("[4,  ,  ]", board.getNear(LAYER_2, 3, 3).toString());
        assertEquals("[]", board.getNear(LAYER_2, 5, 5).toString());
        assertEquals("[ ]", board.getNear(LAYER_2, -1, -1).toString());
    }

    @Test
    public void shouldWork_isOutOfField() {
        assertEquals(true, board.isOutOfField(-1, 1));
        assertEquals(true, board.isOutOfField(1, -1));
        assertEquals(true, board.isOutOfField(4, 1));
        assertEquals(true, board.isOutOfField(1, 4));

        assertEquals(false, board.isOutOfField(0, 1));
        assertEquals(false, board.isOutOfField(1, 0));
        assertEquals(false, board.isOutOfField(3, 1));
        assertEquals(false, board.isOutOfField(1, 3));
    }

    @Test
    public void shouldWork_countNear_layer1() {
        assertEquals(2, board.countNear(LAYER_1, 0, 0, Elements.ONE));
        assertEquals(1, board.countNear(LAYER_1, 0, 0, Elements.TWO));
        assertEquals(0, board.countNear(LAYER_1, 0, 0, Elements.THREE));

        assertEquals(5, board.countNear(LAYER_1, 1, 1, Elements.ONE));
        assertEquals(1, board.countNear(LAYER_1, 1, 1, Elements.TWO));
        assertEquals(2, board.countNear(LAYER_1, 1, 1, Elements.THREE));

        assertEquals(5, board.countNear(LAYER_1, 2, 2, Elements.ONE));
        assertEquals(2, board.countNear(LAYER_1, 2, 2, Elements.TWO));
        assertEquals(1, board.countNear(LAYER_1, 2, 2, Elements.THREE));

        assertEquals(2, board.countNear(LAYER_1, 3, 3, Elements.ONE));
        assertEquals(0, board.countNear(LAYER_1, 3, 3, Elements.TWO));
        assertEquals(1, board.countNear(LAYER_1, 3, 3, Elements.THREE));

        assertEquals(0, board.countNear(LAYER_1, -1, -1, Elements.THREE));
    }

    @Test
    public void shouldWork_countNear_layer2() {
        assertEquals(0, board.countNear(LAYER_2, 0, 0, Elements.ONE));
        assertEquals(1, board.countNear(LAYER_2, 0, 0, Elements.FOUR));
        assertEquals(2, board.countNear(LAYER_2, 0, 0, Elements.NONE));

        assertEquals(0, board.countNear(LAYER_2, 1, 1, Elements.ONE));
        assertEquals(1, board.countNear(LAYER_2, 1, 1, Elements.FOUR));
        assertEquals(7, board.countNear(LAYER_2, 1, 1, Elements.NONE));

        assertEquals(0, board.countNear(LAYER_2, 2, 1, Elements.ONE));
        assertEquals(2, board.countNear(LAYER_2, 2, 1, Elements.FOUR));
        assertEquals(6, board.countNear(LAYER_2, 2, 1, Elements.NONE));

        assertEquals(0, board.countNear(LAYER_2, 3, 3, Elements.ONE));
        assertEquals(1, board.countNear(LAYER_2, 3, 3, Elements.FOUR));
        assertEquals(2, board.countNear(LAYER_2, 3, 3, Elements.NONE));

        assertEquals(0, board.countNear(LAYER_2, -1, -1, Elements.THREE));
    }

    @Test
    public void shouldWork_get_layer1() {
        assertEquals("[[0,0], [0,1], [0,2], [0,3], [1,0], [1,3], " +
                        "[2,0], [2,3], [3,0], [3,1], [3,2], [3,3]]",
                board.get(LAYER_1, Elements.ONE).toString());

        assertEquals("[[1,1], [2,1]]",
                board.get(LAYER_1, Elements.TWO).toString());

        assertEquals("[[1,2], [2,2]]",
                board.get(LAYER_1, Elements.THREE).toString());

        assertEquals("[]",
                board.get(LAYER_1, Elements.NONE).toString());
    }

    @Test
    public void shouldWork_get_layer2() {
        assertEquals("[]",
                board.get(LAYER_2, Elements.ONE).toString());

        assertEquals("[[0,0], [0,1], [0,2], [0,3], " +
                        "[1,0], [1,2], [1,3], " +
                        "[2,0], [2,1], [2,3], " +
                        "[3,0], [3,1], [3,2], [3,3]]",
                board.get(LAYER_2, Elements.NONE).toString());

        assertEquals("[[1,1], [2,2]]",
                board.get(LAYER_2, Elements.FOUR).toString());
    }

    @Test
    public void shouldWork_size() {
        assertEquals(4, board.size());
    }

    @Test
    public void shouldWork_set_layer1() {
        // given
        assertEquals("[[1,2], [2,2]]",
                board.get(LAYER_1, Elements.THREE).toString());

        // when
        board.set(LAYER_1, 1, 1, Elements.THREE.ch());

        // then
        assertEquals("[[1,1], [1,2], [2,2]]",
                board.get(LAYER_1, Elements.THREE).toString());
    }

    @Test
    public void shouldWork_set_layer2() {
        // given
        assertEquals("[]",
                board.get(LAYER_2, Elements.THREE).toString());

        // when
        board.set(LAYER_2, 1, 1, Elements.THREE.ch());

        // then
        assertEquals("[[1,1]]",
                board.get(LAYER_2, Elements.THREE).toString());
    }

    @Test
    public void shouldWorks_removeDuplicates() {
        List<Point> input = Arrays.asList(pt(1, 1), pt(1, 2), pt(2, 2), pt(1, 1));

        assertEquals("[[1,1], [1,2], [2,2]]", AbstractBoard.removeDuplicates(input).toString());
    }

}
