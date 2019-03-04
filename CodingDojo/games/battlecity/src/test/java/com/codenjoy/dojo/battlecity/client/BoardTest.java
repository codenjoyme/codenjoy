package com.codenjoy.dojo.battlecity.client;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.battlecity.model.Elements;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

public class BoardTest {

    private Board board;

    public static Board board(String boardString) {
        return (Board)new Board().forString(boardString);
    }

    @Before
    public void before() {
        board = board(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼     »   » « ☼" +
                "☼ ╬ ╬?╬ ╬ ╬ ╬?☼" +
                "☼ ╬ ╬ ╬☼╬ ╬ ╬ ☼" +
                "☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼" +
                "☼▲╬ ╬     ╬ ╬ ☼" +
                "☼•    ╬ ╬     ☼" +
                "☼   ╬     ╬   ☼" +
                "☼     ╬ ╬     ☼" +
                "☼ ╬ ╬ ╬╬╬ ╬ ╬ ☼" +
                "☼˅╬ ╬ ╬ ╬ ╬ ╬ ☼" +
                "☼ ╬         ╬ ☼" +
                "☼ ╬?  ╬╬╬   ╬ ☼" +
                "☼     ╬ ╬     ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldWorkToString() {
        assertEquals(
               /*14*/"☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
               /*13*/"☼     »   » « ☼\n" +
               /*12*/"☼ ╬ ╬?╬ ╬ ╬ ╬?☼\n" +
               /*11*/"☼ ╬ ╬ ╬☼╬ ╬ ╬ ☼\n" +
               /*10*/"☼ ╬ ╬ ╬ ╬ ╬ ╬ ☼\n" +
                /*9*/"☼▲╬ ╬     ╬ ╬ ☼\n" +
                /*8*/"☼•    ╬ ╬     ☼\n" +
                /*7*/"☼   ╬     ╬   ☼\n" +
                /*6*/"☼     ╬ ╬     ☼\n" +
                /*5*/"☼ ╬ ╬ ╬╬╬ ╬ ╬ ☼\n" +
                /*4*/"☼˅╬ ╬ ╬ ╬ ╬ ╬ ☼\n" +
                /*3*/"☼ ╬         ╬ ☼\n" +
                /*2*/"☼ ╬?  ╬╬╬   ╬ ☼\n" +
                /*1*/"☼     ╬ ╬     ☼\n" +
                /*0*/"☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                    /*012345678901234*/
                "\n" +
                "My tank at: [1,9]\n" +
                "Enemies at: [[1,4], [3,2], [5,12], [6,13], [10,13], [12,13], [13,12]]\n" +
                "Bullets at: [[1,8]]\n", board.toString());
    }

    @Test
    public void shouldWork_getAt() {
        assertEquals(Elements.OTHER_TANK_DOWN, board.getAt(1, 4));
        assertEquals(Elements.BATTLE_WALL, board.getAt(0, 8));
        assertEquals(Elements.CONSTRUCTION, board.getAt(6, 1));
    }

    @Test
    public void shouldWork_getAt_point() {
        assertEquals(Elements.OTHER_TANK_DOWN, board.getAt(pt(1, 4)));
        assertEquals(Elements.BATTLE_WALL, board.getAt(pt(0, 8)));
        assertEquals(Elements.CONSTRUCTION, board.getAt(pt(6, 1)));
    }

    @Test
    public void shouldWork_getNear() {
        assertEquals("[•, ▲,  ,  , ╬,  ,  ,  ]", board.getNear(2, 9).toString());
        assertEquals("[ , ?,  , ╬, », ☼,  ,  ]", board.getNear(6, 12).toString());
        assertEquals("[ ,  ,  , ╬, «,  , ?,  ]", board.getNear(12, 12).toString());
    }

    @Test
    public void shouldWork_getNear_point() {
        assertEquals("[•, ▲,  ,  , ╬,  ,  ,  ]", board.getNear(pt(2, 9)).toString());
        assertEquals("[ , ?,  , ╬, », ☼,  ,  ]", board.getNear(pt(6, 12)).toString());
        assertEquals("[ ,  ,  , ╬, «,  , ?,  ]", board.getNear(pt(12, 12)).toString());
    }

    @Test
    public void shouldWork_getAt_outOfBoard() {
        assertEquals(Elements.BATTLE_WALL, board.getAt(-1, 1));
        assertEquals(Elements.BATTLE_WALL, board.getAt(1, -1));
        assertEquals(Elements.BATTLE_WALL, board.getAt(100, 1));
        assertEquals(Elements.BATTLE_WALL, board.getAt(1, 100));

        assertEquals(Elements.BATTLE_WALL, board.getAt(pt(-1, 1)));
        assertEquals(Elements.BATTLE_WALL, board.getAt(pt(1, -1)));
        assertEquals(Elements.BATTLE_WALL, board.getAt(pt(100, 1)));
        assertEquals(Elements.BATTLE_WALL, board.getAt(pt(1, 100)));
    }

    @Test
    public void shouldWork_getEnemies() {
        assertEquals("[[1,4], [3,2], [5,12], [6,13], [10,13], [12,13], [13,12]]",
                board.getEnemies().toString());
    }

    @Test
    public void shouldWork_getBullets() {
        assertEquals("[[1,8]]", board.getBullets().toString());
    }

    @Test
    public void shouldWork_getMe() {
        assertEquals("[1,9]", board.getMe().toString());

        assertEquals(null, board(" ").getMe());
    }

    @Test
    public void shouldWork_isBulletAt() {
        assertEquals(true, board.isBulletAt(1, 8));
        assertEquals(false, board.isBulletAt(2, 8));
        assertEquals(false, board.isBulletAt(-2, -8));
    }

    @Test
    public void shouldWork_isBarrierAt() {
        assertEquals(false, board.isBarrierAt(1, 8));
        assertEquals(true, board.isBarrierAt(2, 2));
        assertEquals(true, board.isBarrierAt(-2, 2));
    }

    @Test
    public void shouldWork_countNear() {
        assertEquals(0, board.countNear(0, 0, Elements.CONSTRUCTION));
        assertEquals(1, board.countNear(2, 1, Elements.CONSTRUCTION));
        assertEquals(5, board.countNear(5, 5, Elements.CONSTRUCTION));
        assertEquals(5, board.countNear(7, 6, Elements.CONSTRUCTION));

        assertEquals(1, board.countNear(0, 7, Elements.BULLET));
        assertEquals(1, board.countNear(0, 8, Elements.BULLET));
        assertEquals(1, board.countNear(0, 9, Elements.BULLET));
        assertEquals(1, board.countNear(1, 7, Elements.BULLET));
        assertEquals(0, board.countNear(1, 8, Elements.BULLET)); // сам снаряд
        assertEquals(1, board.countNear(1, 9, Elements.BULLET));
        assertEquals(1, board.countNear(2, 7, Elements.BULLET));
        assertEquals(1, board.countNear(2, 8, Elements.BULLET));
        assertEquals(1, board.countNear(2, 9, Elements.BULLET));
    }

    @Test
    public void shouldWork_countNear_point() {
        assertEquals(0, board.countNear(pt(0, 0), Elements.CONSTRUCTION));
        assertEquals(1, board.countNear(pt(2, 1), Elements.CONSTRUCTION));
        assertEquals(5, board.countNear(pt(5, 5), Elements.CONSTRUCTION));
        assertEquals(5, board.countNear(pt(7, 6), Elements.CONSTRUCTION));
    }

    @Test
    public void shouldWork_isAt_point() {
        assertEquals(true, board.isAt(pt(1, 4), Elements.OTHER_TANK_DOWN));
        assertEquals(true, board.isAt(pt(0, 8), Elements.BATTLE_WALL));
        assertEquals(true, board.isAt(pt(6, 1), Elements.CONSTRUCTION));

        assertEquals(false, board.isAt(pt(1, 4), Elements.AI_TANK_LEFT));
        assertEquals(false, board.isAt(pt(0, 8), Elements.AI_TANK_LEFT));
        assertEquals(false, board.isAt(pt(6, 1), Elements.AI_TANK_LEFT));

        assertEquals(true, board.isAt(pt(1, 4), Elements.OTHER_TANK_DOWN, Elements.AI_TANK_LEFT));
        assertEquals(true, board.isAt(pt(0, 8), Elements.AI_TANK_LEFT, Elements.BATTLE_WALL));
        assertEquals(true, board.isAt(pt(6, 1), Elements.CONSTRUCTION, Elements.AI_TANK_LEFT));
    }

    @Test
    public void shouldWork_isAt() {
        assertEquals(true, board.isAt(1, 4, Elements.OTHER_TANK_DOWN));
        assertEquals(true, board.isAt(0, 8, Elements.BATTLE_WALL));
        assertEquals(true, board.isAt(6, 1, Elements.CONSTRUCTION));

        assertEquals(false, board.isAt(1, 4, Elements.AI_TANK_LEFT));
        assertEquals(false, board.isAt(0, 8, Elements.AI_TANK_LEFT));
        assertEquals(false, board.isAt(6, 1, Elements.AI_TANK_LEFT));

        assertEquals(true, board.isAt(1, 4, Elements.OTHER_TANK_DOWN, Elements.AI_TANK_LEFT));
        assertEquals(true, board.isAt(0, 8, Elements.AI_TANK_LEFT, Elements.BATTLE_WALL));
        assertEquals(true, board.isAt(6, 1, Elements.CONSTRUCTION, Elements.AI_TANK_LEFT));
    }

    @Test
    public void shouldWork_isNear() {
        assertEquals(true, board.isNear(1, 1, Elements.CONSTRUCTION));
        assertEquals(true, board.isNear(9, 3, Elements.CONSTRUCTION));
        assertEquals(false, board.isNear(12, 7, Elements.CONSTRUCTION));
    }

    @Test
    public void shouldWork_isNear_point() {
        assertEquals(true, board.isNear(pt(1, 1), Elements.CONSTRUCTION));
        assertEquals(true, board.isNear(pt(9, 3), Elements.CONSTRUCTION));
        assertEquals(false, board.isNear(pt(12, 7), Elements.CONSTRUCTION));
    }

    @Test
    public void shouldWork_isGameOver() {
        assertEquals(false, board.isGameOver());
        assertEquals(true, board(" ").isGameOver());
    }
}
