package com.epam.dojo.icancode.client;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mikhail_Udalyi on 20.09.2016.
 */
public class BoardTest {
    private Board board;

    public static Board board(String... boardString) {
        return (Board) new Board().forString(boardString);
    }

    @Before
    public void before() {
        board = board(
                "╔═════════┐" +
                "║....◄...S│" +
                "║.S.┌─╗...│" +
                "║...│ ║˄.$│" +
                "║.┌─┘ └─╗&│" +
                "║E│     ║.│" +
                "║.╚═┐ ╔═╝$│" +
                "║..O│ ║..O│" +
                "║...╚═╝...│" +
                "║O.$.....E│" +
                "└─────────┘",
                "-----------" +
                "------↑----" +
                "--☺--------" +
                "-----------" +
                "---------←-" +
                "-----------" +
                "---------→-" +
                "-------B-x-" +
                "--------↓--" +
                "--B---X^-%-" +
                "-----------");
    }

    @Test
    public void shouldWorkToString() {
        assertEquals("Layer1 Layer2\n" +
                        " 01234567890   01234567890\n" +
                        " 0╔═════════┐  0----------- Robots: [2,2],[6,9], [7,9], [9,7], [9,9]\n" +
                        " 1║....◄...S│  1------↑---- Gold: [3,9], [9,3], [9,6]\n" +
                        " 2║.S.┌─╗...│  2--☺-------- Start: [2,2], [9,1]\n" +
                        " 3║...│ ║˄.$│  3----------- Exit: [1,5], [9,9]\n" +
                        " 4║.┌─┘ └─╗&│  4---------←- Boxes: [2,9], [7,7], [9,9]\n" +
                        " 5║E│     ║.│  5----------- Holes: [1,9], [3,7], [9,7]\n" +
                        " 6║.╚═┐ ╔═╝$│  6---------→- LaserMachine: [5,1], [7,3]\n" +
                        " 7║..O│ ║..O│  7-------B-x- Laser: [6,1], [8,8], [9,4], [9,6]\n" +
                        " 8║...╚═╝...│  8--------↓--\n" +
                        " 9║O.$.....E│  9--B---X^-%-\n" +
                        "10└─────────┘ 10-----------",
                board.toString());
    }

    @Test
    public void shouldGetMe() {
        assertEquals("[2,2]", board.getMe().toString());
    }

    @Test
    public void shouldGetOtherHeroes() {
        assertEquals("[[6,9], [7,9], [9,7], [9,9]]", board.getOtherHeroes().toString());
    }

    @Test
    public void shouldGetExit() {
        assertEquals("[[1,5], [9,9]]", board.getExit().toString());
    }

    @Test
    public void shouldGetStart() {
        assertEquals("[[2,2], [9,1]]", board.getStart().toString());
    }

    @Test
    public void shouldGetGold() {
        assertEquals("[[3,9], [9,3], [9,6]]", board.getGold().toString());
    }

    @Test
    public void shouldGetHoles() {
        assertEquals("[[1,9], [3,7], [9,7]]", board.getHoles().toString());
    }

    @Test
    public void shouldGetBoxes() {
        assertEquals("[[2,9], [7,7], [9,9]]", board.getBoxes().toString());
    }

    @Test
    public void shouldGetLaser() {
        assertEquals("[[6,1], [8,8], [9,4], [9,6]]", board.getLaser().toString());
    }

    @Test
    public void shouldGetLaserMashines() {
        assertEquals("[[5,1], [7,3]]", board.getLaserMachines().toString());
    }

    @Test
    public void shouldBeBarriers() {
        assertEquals(true, board.isBarrierAt(0, 0));
        assertEquals(true, board.isBarrierAt(1, 0));
        assertEquals(true, board.isBarrierAt(0, 1));

        assertEquals(true, board.isBarrierAt(4, 4));
        assertEquals(true, board.isBarrierAt(5, 4));
        assertEquals(true, board.isBarrierAt(4, 5));

        assertEquals(true, board.isBarrierAt(5, 8));
        assertEquals(true, board.isBarrierAt(6, 8));
        assertEquals(true, board.isBarrierAt(6, 7));

        assertEquals(true, board.isBarrierAt(10, 10));
        assertEquals(true, board.isBarrierAt(9, 10));
        assertEquals(true, board.isBarrierAt(10, 9));

        assertEquals(true, board.isBarrierAt(5, 5));

        assertEquals(true, board.isBarrierAt(2, 2));//there is my robot

        assertEquals(false, board.isBarrierAt(1, 1));
        assertEquals(true, board.isBarrierAt(9, 9));

        assertEquals(true, board.isBarrierAt(1, 9));
        assertEquals(true, board.isBarrierAt(2, 9));
        assertEquals(false, board.isBarrierAt(3, 9));
    }

    @Test
    public void shouldNotBeGameOver() {
        assertEquals(true, board.isMeAlive());
    }
}
