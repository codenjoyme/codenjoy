package com.codenjoy.dojo.icancode.client;

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
                "--B---X^---" +
                "-----------",

                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------");
    }

    @Test
    public void shouldWorkToString() {
        assertEquals(" Layer1        Layer2       Layer3\n" +
                        "  01234567890   01234567890   01234567890\n" +
                        "10╔═════════┐ 10╔═════════┐ 10╔═════════┐ Robots: [2,8],[6,1], [9,3], [7,1]\n" +
                        " 9║....◄...S│  9║-----↑---│  9║---------│ Gold: [3,1], [9,4], [9,7]\n" +
                        " 8║.S.┌─╗...│  8║-☺-┌─╗---│  8║---┌─╗---│ Starts: [2,8], [9,9]\n" +
                        " 7║...│ ║˄.$│  7║---│ ║---│  7║---│ ║---│ Exits: [1,5], [9,1]\n" +
                        " 6║.┌─┘ └─╗&│  6║-┌─┘ └─╗←│  6║-┌─┘ └─╗-│ Boxes: [2,1], [7,3]\n" +
                        " 5║E│     ║.│  5║-│     ║-│  5║-│     ║-│ Holes: [1,1], [3,3], [9,3]\n" +
                        " 4║.╚═┐ ╔═╝$│  4║-╚═┐ ╔═╝→│  4║-╚═┐ ╔═╝-│ LaserMachine: [5,9], [7,7]\n" +
                        " 3║..O│ ║..O│  3║---│ ║B-x│  3║---│ ║---│ Lasers: [6,9], [8,2], [9,4], [9,6]\n" +
                        " 2║...╚═╝...│  2║---╚═╝-↓-│  2║---╚═╝---│ Zombies: \n" +
                        " 1║O.$.....E│  1║-B---X^--│  1║---------│\n" +
                        " 0└─────────┘  0└─────────┘  0└─────────┘\n" +
                        "  01234567890   01234567890   01234567890",
                board.toString());
    }

    @Test
    public void shouldGetMe() {
        assertEquals("[2,8]", board.getMe().toString());
    }

    @Test
    public void shouldGetOtherHeroes() {
        assertEquals("[[6,1], [9,3], [7,1]]", board.getOtherHeroes().toString());
    }

    @Test
    public void shouldGetExit() {
        assertEquals("[[1,5], [9,1]]", board.getExits().toString());
    }

    @Test
    public void shouldGetStart() {
        assertEquals("[[2,8], [9,9]]", board.getStarts().toString());
    }

    @Test
    public void shouldGetGold() {
        assertEquals("[[3,1], [9,4], [9,7]]", board.getGold().toString());
    }

    @Test
    public void shouldGetHoles() {
        assertEquals("[[1,1], [3,3], [9,3]]", board.getHoles().toString());
    }

    @Test
    public void shouldGetBoxes() {
        assertEquals("[[2,1], [7,3]]", board.getBoxes().toString());
    }

    @Test
    public void shouldGetLaser() {
        assertEquals("[[6,9], [8,2], [9,4], [9,6]]", board.getLasers().toString());
    }

    @Test
    public void shouldGetLaserMashines() {
        assertEquals("[[5,9], [7,7]]", board.getLaserMachines().toString());
    }

    @Test
    public void shouldBeBarriers() {
        assertEquals(true, board.isBarrierAt(0, 10));
        assertEquals(true, board.isBarrierAt(1, 10));
        assertEquals(true, board.isBarrierAt(0, 9));

        assertEquals(true, board.isBarrierAt(4, 6));
        assertEquals(true, board.isBarrierAt(5, 6));
        assertEquals(true, board.isBarrierAt(4, 5));

        assertEquals(true, board.isBarrierAt(5, 2));
        assertEquals(true, board.isBarrierAt(6, 2));
        assertEquals(true, board.isBarrierAt(6, 3));

        assertEquals(true, board.isBarrierAt(10, 0));
        assertEquals(true, board.isBarrierAt(9, 0));
        assertEquals(true, board.isBarrierAt(10, 1));

        assertEquals(true, board.isBarrierAt(5, 5));

        assertEquals(false, board.isBarrierAt(2, 8));//there is my robot

        assertEquals(false, board.isBarrierAt(1, 9));
        assertEquals(false, board.isBarrierAt(9, 1));

        assertEquals(false, board.isBarrierAt(1, 1));
        assertEquals(true, board.isBarrierAt(2, 1));
        assertEquals(false, board.isBarrierAt(3, 1));
    }

    @Test
    public void shouldNotBeGameOver() {
        assertEquals(true, board.isMeAlive());
    }

}
