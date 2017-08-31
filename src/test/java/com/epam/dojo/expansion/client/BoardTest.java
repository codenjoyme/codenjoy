package com.epam.dojo.expansion.client;

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
import com.codenjoy.dojo.services.Point;
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
                "║........1│" +
                "║.2.┌─╗...│" +
                "║...│ ║..$│" +
                "║.┌─┘ └─╗.│" +
                "║E│     ║.│" +
                "║.╚═┐ ╔═╝$│" +
                "║..O│ ║..O│" +
                "║4..╚═╝.3.│" +
                "║O.$.....E│" +
                "└─────────┘",
                "-----------" +
                "-----------" +
                "--☺--------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-------B---" +
                "-----------" +
                "--B--♥♦♣♠--" +
                "-----------");
    }

    @Test
    public void shouldWorkToString() {
        assertEquals(" Layer1        Layer2\n" +
                    "  01234567890   01234567890\n" +
                    "10╔═════════┐ 10╔═════════┐ My Forces: [2,2]\n" +
                    " 9║........1│  9║---------│ Enemy Forces: [5,9], [6,9], [7,9], [8,9]\n" +
                    " 8║.2.┌─╗...│  8║-☺-┌─╗---│ Gold: [3,9], [9,3], [9,6]\n" +
                    " 7║...│ ║..$│  7║---│ ║---│ Bases: [1,8], [2,2], [8,8], [9,1]\n" +
                    " 6║.┌─┘ └─╗.│  6║-┌─┘ └─╗-│ Exits: [1,5], [9,9]\n" +
                    " 5║E│     ║.│  5║-│     ║-│ Breaks: [2,9], [7,7]\n" +
                    " 4║.╚═┐ ╔═╝$│  4║-╚═┐ ╔═╝-│ Holes: [1,9], [3,7], [9,7]\n" +
                    " 3║..O│ ║..O│  3║---│ ║B--│\n" +
                    " 2║4..╚═╝.3.│  2║---╚═╝---│\n" +
                    " 1║O.$.....E│  1║-B--♥♦♣♠-│\n" +
                    " 0└─────────┘  0└─────────┘\n" +
                    "  01234567890   01234567890",
                board.toString());
    }

    @Test
    public void shouldGetMe() {
        assertEquals("[[2,2]]", board.getMyForces().toString());
    }

    @Test
    public void shouldGetOtherHeroes() {
        assertEquals("[[5,9], [6,9], [7,9], [8,9]]", board.getEnemyForces().toString());
    }

    @Test
    public void shouldGetExits() {
        assertEquals("[[1,5], [9,9]]", board.getExits().toString());
    }

    @Test
    public void shouldGetBases() {
        assertEquals("[[1,8], [2,2], [8,8], [9,1]]", board.getBases().toString());
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
        assertEquals("[[2,9], [7,7]]", board.getBreaks().toString());
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

        assertEquals(false, board.isBarrierAt(2, 2)); //there is my forces

        assertEquals(false, board.isBarrierAt(1, 1));
        assertEquals(false, board.isBarrierAt(9, 9));

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
        Point from = board.getMyForces().get(0);
        List<Direction> way = board.getShortestWay(from, board.getGold());

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
        Point from = board.getMyForces().get(0);
        List<Direction> way = board.getShortestWay(from, board.getGold());

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
        Point from = board.getMyForces().get(0);
        List<Direction> way = board.getShortestWay(from, board.getGold());

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
                "-♦----" +
                "------" +
                "------" +
                "------");

        // when
        Point from = board.getMyForces().get(0);
        List<Direction> way = board.getShortestWay(from, board.getGold());

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
                "------" +
                "------");

        // when
        Point from = board.getMyForces().get(0);
        List<Direction> way = board.getShortestWay(from, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withStartEnd() {
        // given
        board = board(
                "╔════┐" +
                "║....│" +
                "║1...│" +
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
        Point from = board.getMyForces().get(0);
        List<Direction> way = board.getShortestWay(from, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }
}
