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

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

/**
 * Created by indigo on 2016-09-22.
 */
public class AbstractBoardTest {
    private AbstractBoard board;

    public static AbstractBoard board(String boardString) {
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
                "1111");
    }

    @Test
    public void shouldWork_toString() {
        assertEquals(
                "Board:\n" +
                "1111\n" +
                "1221\n" +
                "1331\n" +
                "1111\n", board.toString());
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
        assertEquals(Arrays.asList(Elements.ONE), board.getAllAt(0, 0));
        assertEquals(Arrays.asList(Elements.TWO), board.getAllAt(2, 1));
        assertEquals(Arrays.asList(Elements.THREE), board.getAllAt(2, 2));
    }

    @Test
    public void shouldWork_getAt() {
        assertEquals(Elements.ONE, board.getAt(0, 0));
        assertEquals(Elements.TWO, board.getAt(2, 1));
        assertEquals(Elements.THREE, board.getAt(2, 2));
    }

    @Test
    public void shouldWork_isAt() {
        assertEquals(true, board.isAt(0, 0, Elements.ONE));
        assertEquals(false, board.isAt(1, 1, Elements.ONE));
        assertEquals(false, board.isAt(2, 2, Elements.ONE));

        assertEquals(true, board.isAt(1, 1, Elements.TWO, Elements.THREE));
        assertEquals(true, board.isAt(2, 2, Elements.TWO, Elements.THREE));
        assertEquals(false, board.isAt(2, 2, Elements.TWO, Elements.ONE));
    }

    @Test
    public void shouldWork_isNear() {
        assertEquals(true, board.isNear(1, 1, Elements.ONE));
        assertEquals(false, board.isNear(5, 5, Elements.TWO));
    }

    @Test
    public void shouldWork_getNear() {
        assertEquals("[1, 1, 1, 1, 3, 1, 2, 3]", board.getNear(1, 1).toString());
        assertEquals("[2, 3, 1, 2, 1, 1, 1, 1]", board.getNear(2, 2).toString());
        assertEquals("[3, 1, 1]", board.getNear(3, 3).toString());
        assertEquals("[]", board.getNear(5, 5).toString());
        assertEquals("[1]", board.getNear(-1, -1).toString());
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
        assertEquals(2, board.countNear(0, 0, Elements.ONE));
        assertEquals(1, board.countNear(0, 0, Elements.TWO));
        assertEquals(0, board.countNear(0, 0, Elements.THREE));

        assertEquals(5, board.countNear(1, 1, Elements.ONE));
        assertEquals(1, board.countNear(1, 1, Elements.TWO));
        assertEquals(2, board.countNear(1, 1, Elements.THREE));

        assertEquals(5, board.countNear(2, 2, Elements.ONE));
        assertEquals(2, board.countNear(2, 2, Elements.TWO));
        assertEquals(1, board.countNear(2, 2, Elements.THREE));

        assertEquals(2, board.countNear(3, 3, Elements.ONE));
        assertEquals(0, board.countNear(3, 3, Elements.TWO));
        assertEquals(1, board.countNear(3, 3, Elements.THREE));

        assertEquals(0, board.countNear(-1, -1, Elements.THREE));
    }

    @Test
    public void shouldWork_get() {
        assertEquals("[[0,0], [0,1], [0,2], [0,3], [1,0], [1,3], " +
                        "[2,0], [2,3], [3,0], [3,1], [3,2], [3,3]]",
                board.get(Elements.ONE).toString());

        assertEquals("[[1,1], [2,1]]",
                board.get(Elements.TWO).toString());

        assertEquals("[[1,2], [2,2]]",
                board.get(Elements.THREE).toString());
    }

    @Test
    public void shouldWork_size() {
        assertEquals(4, board.size());
    }

    @Test
    public void shouldWork_set() {
        // given
        assertEquals("[[1,2], [2,2]]",
                board.get(Elements.THREE).toString());

        // when
        board.set(1, 1, Elements.THREE.ch());

        // then
        assertEquals("[[1,1], [1,2], [2,2]]",
                board.get(Elements.THREE).toString());
    }

    @Test
    public void shouldWorks_removeDuplicates() {
        List<Point> input = Arrays.asList(pt(1, 1), pt(1, 2), pt(2, 2), pt(1, 1));

        assertEquals("[[1,1], [1,2], [2,2]]", AbstractBoard.removeDuplicates(input).toString());
    }

    @Test
    public void getBoardAsString() {
        assertEquals(
                "1111\n" +
                "1221\n" +
                "1331\n" +
                "1111\n", board.boardAsString());
    }

    @Test
    public void shouldWorks_countLayers() {
        assertEquals(1, board.countLayers());
    }

}
