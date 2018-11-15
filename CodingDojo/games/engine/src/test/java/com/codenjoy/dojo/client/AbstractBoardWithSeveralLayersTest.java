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
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.client.Elements.*;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

public class AbstractBoardWithSeveralLayersTest {
    private AbstractBoard board;

    public static AbstractBoard board(String ...boardString) {
        return (AbstractBoard)new AbstractBoard<Elements>(){
            @Override
            public Elements valueOf(char ch) {
                return Elements.valueOf(ch);
            }
        }.forString(boardString);
    }

    @Before
    public void before() {
        board = board(
                "1111" +
                "1221" +
                "1331" +
                "1111",
                "4444" +
                "4554" +
                "4664" +
                "4444");
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
                "4444\n" +
                "4554\n" +
                "4664\n" +
                "4444\n", board.toString());
    }

    @Test
    public void shouldWork_getField() {
        assertEquals(
                "[[1, 1, 1, 1], " +
                "[1, 2, 3, 1], " +
                "[1, 2, 3, 1], " +
                "[1, 1, 1, 1]]",
                Arrays.deepToString(board.getField(0)));
    }

    @Test
    public void shouldWork_getAllAt() {
        assertEquals(Arrays.asList(ONE, FOUR), board.getAllAt(0, 0));
        assertEquals(Arrays.asList(TWO, FIVE), board.getAllAt(2, 1));
        assertEquals(Arrays.asList(THREE, SIX), board.getAllAt(2, 2));
    }

    @Test
    public void shouldWork_getAt() {
        assertEquals(ONE, board.getAt(0, 0));
        assertEquals(TWO, board.getAt(2, 1));
        assertEquals(THREE, board.getAt(2, 2));
    }

    @Test
    public void shouldWork_isAt() {
        assertEquals(true, board.isAt(0, 0, ONE));
        assertEquals(true, board.isAt(0, 0, FOUR));

        assertEquals(false, board.isAt(1, 1, ONE));
        assertEquals(false, board.isAt(1, 1, FOUR));

        assertEquals(false, board.isAt(2, 2, ONE));
        assertEquals(false, board.isAt(2, 2, FOUR));

        // -
        assertEquals(true, board.isAt(1, 1, TWO, THREE));
        assertEquals(true, board.isAt(1, 1, FIVE, SIX));

        assertEquals(true, board.isAt(2, 2, TWO, THREE));
        assertEquals(true, board.isAt(2, 2, FIVE, SIX));

        assertEquals(false, board.isAt(2, 2, TWO, ONE));
        assertEquals(false, board.isAt(2, 2, FIVE, FOUR));
    }

    @Test
    public void shouldWork_isNear() {
        assertEquals(true, board.isNear(1, 1, ONE));
        assertEquals(true, board.isNear(1, 1, FOUR));

        assertEquals(false, board.isNear(5, 5, TWO));
        assertEquals(false, board.isNear(5, 5, FIVE));
    }

    @Test
    public void shouldWork_getNear() {
        assertEquals("[1, 1, 1, 1, 3, 1, 2, 3, " +
                        "4, 4, 4, 4, 6, 4, 5, 6]", board.getNear(1, 1).toString());
        assertEquals("[2, 3, 1, 2, 1, 1, 1, 1, " +
                        "5, 6, 4, 5, 4, 4, 4, 4]", board.getNear(2, 2).toString());
        assertEquals("[3, 1, 1, 6, 4, 4]", board.getNear(3, 3).toString());
        assertEquals("[]", board.getNear(5, 5).toString());
        assertEquals("[1, 4]", board.getNear(-1, -1).toString());
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
    public void shouldWork_countNear() {
        assertEquals(2, board.countNear(0, 0, ONE));
        assertEquals(2, board.countNear(0, 0, FOUR));

        assertEquals(1, board.countNear(0, 0, TWO));
        assertEquals(1, board.countNear(0, 0, FIVE));

        assertEquals(0, board.countNear(0, 0, THREE));
        assertEquals(0, board.countNear(0, 0, SIX));

        // -
        assertEquals(5, board.countNear(1, 1, ONE));
        assertEquals(5, board.countNear(1, 1, FOUR));

        assertEquals(1, board.countNear(1, 1, TWO));
        assertEquals(1, board.countNear(1, 1, FIVE));

        assertEquals(2, board.countNear(1, 1, THREE));
        assertEquals(2, board.countNear(1, 1, SIX));

        // -
        assertEquals(5, board.countNear(2, 2, ONE));
        assertEquals(5, board.countNear(2, 2, FOUR));

        assertEquals(2, board.countNear(2, 2, TWO));
        assertEquals(2, board.countNear(2, 2, FIVE));

        assertEquals(1, board.countNear(2, 2, THREE));
        assertEquals(1, board.countNear(2, 2, SIX));

        // -
        assertEquals(2, board.countNear(3, 3, ONE));
        assertEquals(2, board.countNear(3, 3, FOUR));

        assertEquals(0, board.countNear(3, 3, TWO));
        assertEquals(0, board.countNear(3, 3, FIVE));

        assertEquals(1, board.countNear(3, 3, THREE));
        assertEquals(1, board.countNear(3, 3, SIX));

        // -
        assertEquals(0, board.countNear(-1, -1, THREE));
        assertEquals(0, board.countNear(-1, -1, SIX));
    }

    @Test
    public void shouldWork_get() {
        String expected1 = "[[0,0], [0,1], [0,2], [0,3], [1,0], [1,3], " +
                "[2,0], [2,3], [3,0], [3,1], [3,2], [3,3]]";
        assertEquals(expected1, board.get(ONE).toString());
        assertEquals(expected1, board.get(FOUR).toString());

        String expected2 = "[[1,1], [2,1]]";
        assertEquals(expected2, board.get(TWO).toString());
        assertEquals(expected2, board.get(FIVE).toString());

        String expected3 = "[[1,2], [2,2]]";
        assertEquals(expected3, board.get(THREE).toString());
        assertEquals(expected3, board.get(SIX).toString());
    }

    @Test
    public void shouldWork_size() {
        assertEquals(4, board.size());
    }

    @Test
    public void shouldWork_set_default() {
        // given
        assertEquals("[[1,2], [2,2]]",
                board.get(THREE).toString());

        // when
        board.set(1, 1, THREE.ch());

        // then
        assertEquals("[[1,1], [1,2], [2,2]]",
                board.get(THREE).toString());
    }

    @Test
    public void shouldWork_set_atLayer1() {
        // given
        assertEquals("[[1,2], [2,2]]",
                board.get(THREE).toString());

        // when
        board.set(0, 1, 1, THREE.ch());

        // then
        assertEquals("[[1,1], [1,2], [2,2]]",
                board.get(THREE).toString());
    }

    @Test
    public void shouldWork_set_atLayer2() {
        // given
        assertEquals("[[1,2], [2,2]]",
                board.get(THREE).toString());

        // when
        board.set(1, 1, 1, THREE.ch());

        // then
        assertEquals("[[1,2], [2,2], [1,1]]", // TODO think about order
                board.get(THREE).toString());
    }

    @Test
    public void shouldWorks_removeDuplicates() {
        List<Point> input = Arrays.asList(pt(1, 1), pt(1, 2), pt(2, 2), pt(1, 1));

        assertEquals("[[1,1], [1,2], [2,2]]", AbstractBoard.removeDuplicates(input).toString());
    }

    @Test
    public void shouldWorks_countLayers() {
        assertEquals(2, board.countLayers());
    }

    @Test
    public void getBoardAsString() {
        assertEquals(
                "1111\n" +
                "1221\n" +
                "1331\n" +
                "1111\n" +
                "\n" +
                "4444\n" +
                "4554\n" +
                "4664\n" +
                "4444\n", board.boardAsString());
    }

}
