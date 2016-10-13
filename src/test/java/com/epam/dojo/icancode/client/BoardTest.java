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


import com.codenjoy.dojo.client.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

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
        assertEquals("Board layer 1:\n" +
                        "╔═════════┐\n" +
                        "║....◄...S│\n" +
                        "║.S.┌─╗...│\n" +
                        "║...│ ║˄.$│\n" +
                        "║.┌─┘ └─╗&│\n" +
                        "║E│     ║.│\n" +
                        "║.╚═┐ ╔═╝$│\n" +
                        "║..O│ ║..O│\n" +
                        "║...╚═╝...│\n" +
                        "║O.$.....E│\n" +
                        "└─────────┘\n" +
                        "\n" +
                        "Board layer 2:\n" +
                        "-----------\n" +
                        "------↑----\n" +
                        "--☺--------\n" +
                        "-----------\n" +
                        "---------←-\n" +
                        "-----------\n" +
                        "---------→-\n" +
                        "-------B-x-\n" +
                        "--------↓--\n" +
                        "--B---X^-%-\n" +
                        "-----------\n" +
                        "\n" +
                        "Start at: [[2,2], [9,1]]\n" +
                        "Exit at: [[1,5], [9,9]]\n" +
                        "Gold at: [[3,9], [9,3], [9,6]]\n" +
                        "Boxes at: [[2,9], [7,7], [9,9]]\n" +
                        "Holes at: [[1,9], [3,7], [9,7]]\n" +
                        "Robot at: [2,2]\n" +
                        "Other robots at: [[6,9], [7,9], [9,7], [9,9]]\n" +
                        "LaserMachine at: [[5,1], [7,3]]\n" +
                        "Laser at: [[6,1], [8,8], [9,4], [9,6]]",
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

        assertEquals(false, board.isBarrierAt(2, 2));//there is my robot

        assertEquals(false, board.isBarrierAt(1, 1));
        assertEquals(true, board.isBarrierAt(9, 9));

        assertEquals(false, board.isBarrierAt(1, 9));
        assertEquals(true, board.isBarrierAt(2, 9));
        assertEquals(false, board.isBarrierAt(3, 9));
    }

    @Test
    public void shouldNotBeGameOver() {
        assertEquals(true, board.isMeAlive());
    }

    @Test
    public void shouldFindWay_withoutBarriers() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withBoxes() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "-B----" +
                "--B---" +
                "------" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, DOWN, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withHoles() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║O...│" +
                "║O...│" +
                "║OOO$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withOtherRobots() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "-X----" +
                "------" +
                "-^-&--" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withRobots() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "-☺----" +
                "------" +
                "-*-☻--" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withRobotsFalling() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║O...│" +
                "║O...│" +
                "║OOO$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "-x----" +
                "-o----" +
                "-xox--" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withRobotsOverBox() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "-%----" +
                "--№---" +
                "------" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, DOWN, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withLasers() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "-←----" +
                "-→----" +
                "-↑↓---" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withLaserMachines() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║.$..│" +
                "║....│" +
                "└────┘",
                "------" +
                "-☺----" +
                "-˂˃˄--" +
                "---˅--" +
                "------" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[RIGHT, RIGHT, RIGHT, DOWN, DOWN, DOWN, LEFT, LEFT, UP]", way.toString());
    }

    @Test
    public void shouldFindWay_withLaserMachinesCharged() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║....│" +
                "║.$..│" +
                "║....│" +
                "└────┘",
                "------" +
                "-☺----" +
                "-◄►▲--" +
                "---▼--" +
                "------" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[RIGHT, RIGHT, RIGHT, DOWN, DOWN, DOWN, LEFT, LEFT, UP]", way.toString());
    }

    @Test
    public void shouldFindWay_withStartEnd() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║S...│" +
                "║E...│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        List<Direction> way = board.getShortestWay(board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }
}
