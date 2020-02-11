package com.codenjoy.dojo.bomberman.client;

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


import com.codenjoy.dojo.bomberman.model.Elements;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

/**
 * User: sanja
 * Date: 25.04.13
 * Time: 13:50
 */
public class BoardTest {

    private Board board;

    public static Board board(String boardString) {
        return (Board)new Board().forString(boardString);
    }

    @Before
    public void before() {
        board = board(
                "☼☼☼☼☼☼☼☼☼" +
                "☼1 ♣   ♠☼" +
                "☼#2  &  ☼" +
                "☼# 3 ♣ ♠☼" +
                "☼☺  4   ☼" +
                "☼   ♥ H☻☼" +
                "☼x H ҉҉҉☼" +
                "☼& &    ☼" +
                "☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldWorkToString() {
        assertEquals(
           /*8*/"☼☼☼☼☼☼☼☼☼\n" +
           /*7*/"☼1 ♣   ♠☼\n" +
           /*6*/"☼#2  &  ☼\n" +
           /*5*/"☼# 3 ♣ ♠☼\n" +
           /*4*/"☼☺  4   ☼\n" +
           /*3*/"☼   ♥ H☻☼\n" +
           /*2*/"☼x H ҉҉҉☼\n" +
           /*1*/"☼& &    ☼\n" +
           /*0*/"☼☼☼☼☼☼☼☼☼\n" +
               /*012345678*/
                "\n" +
                "Bomberman at: [1,4]\n" +
                "Other bombermans at: [[3,7], [4,3], [5,5], [7,5], [7,7]]\n" +
                        "Meat choppers at: [[1,1], [3,1], [5,6]]\n" +
                        "Destroy walls at: [[1,5], [1,6]]\n" +
                        "Bombs at: [[1,7], [2,6], [3,5], [4,4], [7,3], [7,5], [7,7]]\n" +
                        "Blasts: [[5,2], [6,2], [7,2]]\n" +
                        "Expected blasts at: [[1,6], [1,7], [2,5], [2,6], " +
                                "[2,7], [3,4], [3,5], [3,6], [4,3], [4,4], [4,5], " +
                                "[5,4], [6,3], [6,5], [6,7], [7,2], [7,3], [7,4], " +
                                "[7,5], [7,6], [7,7]]", board.toString());
    }

    @Test
    public void shouldWork_getAt() {
        assertEquals(Elements.BOMB_BOMBERMAN, board.getAt(7, 3));
        assertEquals(Elements.WALL, board.getAt(0, 8));
        assertEquals(Elements.MEAT_CHOPPER, board.getAt(5, 6));
    }

    @Test
    public void shouldWork_getAt_point() {
        assertEquals(Elements.BOMB_BOMBERMAN, board.getAt(pt(7, 3)));
        assertEquals(Elements.WALL, board.getAt(pt(0, 8)));
        assertEquals(Elements.MEAT_CHOPPER, board.getAt(pt(5, 6)));
    }

    @Test
    public void shouldWork_getNear() {
        assertEquals("[H, ҉,  , ☼]", board.getNear(7, 3).toString());
        assertEquals("[☼, ☼]", board.getNear(0, 8).toString());
        assertEquals("[ , ♣,  ,  ]", board.getNear(5, 6).toString());
    }

    @Test
    public void shouldWork_getNear_point() {
        assertEquals("[H, ҉,  , ☼]", board.getNear(pt(7, 3)).toString());
        assertEquals("[☼, ☼]", board.getNear(pt(0, 8)).toString());
        assertEquals("[ , ♣,  ,  ]", board.getNear(pt(5, 6)).toString());
    }

    @Test
    public void shouldWork_getAt_outOfBoard() {
        assertEquals(Elements.WALL, board.getAt(-1, 1));
        assertEquals(Elements.WALL, board.getAt(1, -1));
        assertEquals(Elements.WALL, board.getAt(100, 1));
        assertEquals(Elements.WALL, board.getAt(1, 100));

        assertEquals(Elements.WALL, board.getAt(pt(-1, 1)));
        assertEquals(Elements.WALL, board.getAt(pt(1, -1)));
        assertEquals(Elements.WALL, board.getAt(pt(100, 1)));
        assertEquals(Elements.WALL, board.getAt(pt(1, 100)));
    }

    @Test
    public void shouldWork_getOtherBombermans() {
        assertEquals("[[3,7], [4,3], [5,5], [7,5], [7,7]]", board.getOtherBombermans().toString());
    }

    @Test
    public void shouldWork_getBarriers() {
        assertEquals("[[0,0], [0,1], [0,2], [0,3], [0,4], [0,5], " +
                "[0,6], [0,7], [0,8], [1,0], [1,1], [1,5], [1,6], " +
                "[1,7], [1,8], [2,0], [2,6], [2,8], [3,0], [3,1], " +
                "[3,5], [3,7], [3,8], [4,0], [4,3], [4,4], [4,8], " +
                "[5,0], [5,5], [5,6], [5,8], [6,0], [6,8], [7,0], " +
                "[7,3], [7,5], [7,7], [7,8], [8,0], [8,1], [8,2], " +
                "[8,3], [8,4], [8,5], [8,6], [8,7], [8,8]]", board.getBarriers().toString());
    }

    @Test
    public void shouldWork_isBarrierAt() {
        assertEquals(true, board.isBarrierAt(1, 1));
        assertEquals(false, board.isBarrierAt(5, 1));
    }

    @Test
    public void shouldWork_isBarrierAt_point() {
        assertEquals(true, board.isBarrierAt(pt(1, 1)));
        assertEquals(false, board.isBarrierAt(pt(5, 1)));
    }

    @Test
    public void shouldWork_getBlasts() {
        assertEquals("[[5,2], [6,2], [7,2]]", board.getBlasts().toString());
    }

    @Test
    public void shouldWork_getBomberman() {
        assertEquals("[1,4]", board.getBomberman().toString());
        assertEquals("[0,0]", board("☺").getBomberman().toString());
        assertEquals("[0,0]", board("☻").getBomberman().toString());
        assertEquals("[0,0]", board("Ѡ").getBomberman().toString());
    }

    @Test
    public void shouldWork_getBombs() {
        assertEquals("[[1,7], [2,6], [3,5], [4,4], [7,3], [7,5], [7,7]]", board.getBombs().toString());
    }

    @Test
    public void shouldWork_getDestroyWalls() {
        assertEquals("[[1,5], [1,6]]", board.getDestroyableWalls().toString());
    }

    @Test
    public void shouldWork_getFutureBlasts() {
        assertEquals("[[1,6], [1,7], [2,5], [2,6], [2,7], [3,4], [3,5], [3,6], " +
                "[4,3], [4,4], [4,5], [5,4], [6,3], [6,5], [6,7], [7,2], [7,3], " +
                "[7,4], [7,5], [7,6], [7,7]]", board.getFutureBlasts().toString());
    }

    @Test
    public void shouldWork_getMeatChoppers() {
        assertEquals("[[1,1], [3,1], [5,6]]", board.getMeatChoppers().toString());
    }

    @Test
    public void shouldWork_countNear() {
        assertEquals(0, board.countNear(0, 0, Elements.MEAT_CHOPPER));
        assertEquals(2, board.countNear(2, 1, Elements.MEAT_CHOPPER));
        assertEquals(1, board.countNear(4, 1, Elements.MEAT_CHOPPER));

        assertEquals(2, board.countNear(1, 7, Elements.WALL));
        assertEquals(2, board.countNear(1, 1, Elements.WALL));
        assertEquals(2, board.countNear(7, 7, Elements.WALL));
        assertEquals(2, board.countNear(7, 1, Elements.WALL));
        assertEquals(1, board.countNear(1, 6, Elements.WALL));
    }

    @Test
    public void shouldWork_countNear_point() {
        assertEquals(0, board.countNear(pt(0, 0), Elements.MEAT_CHOPPER));
        assertEquals(2, board.countNear(pt(2, 1), Elements.MEAT_CHOPPER));
        assertEquals(1, board.countNear(pt(4, 1), Elements.MEAT_CHOPPER));

        assertEquals(2, board.countNear(pt(1, 7), Elements.WALL));
        assertEquals(2, board.countNear(pt(1, 1), Elements.WALL));
        assertEquals(2, board.countNear(pt(7, 7), Elements.WALL));
        assertEquals(2, board.countNear(pt(7, 1), Elements.WALL));
        assertEquals(1, board.countNear(pt(1, 6), Elements.WALL));
    }

    @Test
    public void shouldWork_isAt() {
        assertEquals(true, board.isAt(3, 1, Elements.MEAT_CHOPPER));
        assertEquals(false, board.isAt(2, 1, Elements.MEAT_CHOPPER));

        assertEquals(true, board.isAt(3, 1, Elements.BOMB_BOMBERMAN, Elements.MEAT_CHOPPER));
        assertEquals(false, board.isAt(2, 1, Elements.BOMB_BOMBERMAN, Elements.MEAT_CHOPPER));
    }

    @Test
    public void shouldWork_isAt_point() {
        assertEquals(true, board.isAt(pt(3, 1), Elements.MEAT_CHOPPER));
        assertEquals(false, board.isAt(pt(2, 1), Elements.MEAT_CHOPPER));

        assertEquals(true, board.isAt(pt(3, 1), Elements.BOMB_BOMBERMAN, Elements.MEAT_CHOPPER));
        assertEquals(false, board.isAt(pt(2, 1), Elements.BOMB_BOMBERMAN, Elements.MEAT_CHOPPER));
    }

    @Test
    public void shouldWork_isNear() {
        assertEquals(true, board.isNear(1, 1, Elements.WALL));
        assertEquals(false, board.isNear(5, 5, Elements.WALL));
    }

    @Test
    public void shouldWork_isNear_point() {
        assertEquals(true, board.isNear(pt(1, 1), Elements.WALL));
        assertEquals(false, board.isNear(pt(5, 5), Elements.WALL));
    }

    @Test
    public void shouldWork_getWalls() {
        assertEquals("[[0,0], [0,1], [0,2], [0,3], [0,4], [0,5], [0,6], [0,7], [0,8], " +
                "[1,0], [1,8], [2,0], [2,8], [3,0], [3,8], [4,0], [4,8], [5,0], [5,8], " +
                "[6,0], [6,8], [7,0], [7,8], [8,0], [8,1], [8,2], [8,3], [8,4], [8,5], " +
                "[8,6], [8,7], [8,8]]", board.getWalls().toString());
    }

    @Test
    public void shouldWork_isMyBombermanDead() {
        assertEquals(false, board.isMyBombermanDead());
        assertEquals(true, board("Ѡ").isMyBombermanDead());
    }
    
}
