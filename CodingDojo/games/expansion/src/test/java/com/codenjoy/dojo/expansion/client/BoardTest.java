package com.codenjoy.dojo.expansion.client;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mikhail_Udalyi on 20.09.2016.
 */
public class BoardTest {
    private Board board;

    public static Board board(String json, String... boardString) {
        Board board = (Board) new Board().forString(boardString);
        board.setSource(new JSONObject(json));
        return board;
    }

    @Before
    public void before() {
        board = board(
                "{'myBase':{'x':2,'y':8}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
                "'forces':'-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#00B-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#00C00D00E-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔═════════┐" +
                "║........1│" +
                "║.2.┌─╗...│" +
                "║...│ ║..$│" +
                "║.┌─┘ └─╗.│" +
                "║E│     ║.│" +
                "║.╚═┐ ╔═╝$│" +
                "║..O│ ║B.O│" +
                "║4..╚═╝.3.│" +
                "║OB$.....E│" +
                "└─────────┘",
                "-----------" +
                "-----------" +
                "--♥--------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "------♦♣♠--" +
                "-----------");
    }

    @Test
    public void shouldWorkToString() {
        assertEquals(" Layer1        Layer1        Layer3\n" +
                        "  01234567890   01234567890     0    1    2    3    4    5    6    7    8    9    0  \n" +
                        "10╔═════════┐ 10╔═════════┐ 10    |    |    |    |    |    |    |    |    |    |    | My Forces: [2,8]=11\n" +
                        " 9║........1│  9║---------│  9    |    |    |    |    |    |    |    |    |    |    | Enemy Forces: [6,1]=12, [7,1]=13, [8,1]=14\n" +
                        " 8║.2.┌─╗...│  8║-♥-┌─╗---│  8    |    |  11|    |    |    |    |    |    |    |    | Gold: [3,1], [9,4], [9,7]\n" +
                        " 7║...│ ║..$│  7║---│ ║---│  7    |    |    |    |    |    |    |    |    |    |    | Bases: [1,2], [2,8], [8,2], [9,9]\n" +
                        " 6║.┌─┘ └─╗.│  6║-┌─┘ └─╗-│  6    |    |    |    |    |    |    |    |    |    |    | Exits: [1,5], [9,1]\n" +
                        " 5║E│     ║.│  5║-│     ║-│  5    |    |    |    |    |    |    |    |    |    |    | Breaks: [2,1], [7,3]\n" +
                        " 4║.╚═┐ ╔═╝$│  4║-╚═┐ ╔═╝-│  4    |    |    |    |    |    |    |    |    |    |    | Holes: [1,1], [3,3], [9,3]\n" +
                        " 3║..O│ ║B.O│  3║---│ ║---│  3    |    |    |    |    |    |    |    |    |    |    |\n" +
                        " 2║4..╚═╝.3.│  2║---╚═╝---│  2    |    |    |    |    |    |    |    |    |    |    |\n" +
                        " 1║OB$.....E│  1║-----♦♣♠-│  1    |    |    |    |    |    |  12|  13|  14|    |    |\n" +
                        " 0└─────────┘  0└─────────┘  0    |    |    |    |    |    |    |    |    |    |    |\n" +
                        "  01234567890   01234567890     0    1    2    3    4    5    6    7    8    9    0   Tick#10",
                board.toString());
    }

    @Test
    public void shouldGetTick() {
        assertEquals(10, board.getTick());
    }

    @Test
    public void shouldGetRound() {
        assertEquals("[1,10]", board.getRound().toString());
    }

    @Test
    public void shouldGetMyForces() {
        assertEquals("[[2,8]=11]", board.getMyForces().toString());
    }

    @Test
    public void shouldGetMyBasePosition() {
        assertEquals("[2,8]", board.getMyBasePosition().toString());
    }

    @Test
    public void shouldGetEnemyForces() {
        assertEquals("[[6,1]=12, [7,1]=13, [8,1]=14]", board.getEnemyForces().toString());
    }

    @Test
    public void shouldGetExits() {
        assertEquals("[[1,5], [9,1]]", board.getExits().toString());
    }

    @Test
    public void shouldGetBases() {
        assertEquals("[[1,2], [2,8], [8,2], [9,9]]", board.getBases().toString());
    }

    @Test
    public void shouldGetGold() {
        assertEquals("[[3,1], [9,4], [9,7]]", board.getGold().toString());
    }

    @Test
    public void shouldIsInLobby() {
        assertEquals(false, board.isInLobby());
    }

    @Test
    public void shouldGetMyAvailable() {
        assertEquals(10, board.getForcesAvailable());
    }

    @Test
    public void shouldGetHoles() {
        assertEquals("[[1,1], [3,3], [9,3]]", board.getHoles().toString());
    }

    @Test
    public void shouldGetBreaks() {
        assertEquals("[[2,1], [7,3]]", board.getBreaks().toString());
    }

    @Test
    public void shouldGetFreeSpaces() {
        assertEquals("[[1,3], [1,4], [1,6], [1,7], [1,8], [1,9], " +
                "[2,2], [2,3], [2,7], [2,9], " +
                "[3,2], [3,7], [3,8], [3,9], " +
                "[4,1], [4,9], " +
                "[5,1], [5,9], " +
                "[6,9], " +
                "[7,2], [7,7], [7,8], [7,9], " +
                "[8,3], [8,7], [8,8], [8,9], " +
                "[9,2], [9,5], [9,6], [9,8]]", board.getFreeSpaces().toString());
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
        assertEquals(true, board.isBarrierAt(2, 1));
        assertEquals(false, board.isBarrierAt(3, 9));
    }

    @Test
    public void shouldNotBeGameOver() {
        assertEquals(true, board.isMeAlive());
    }

    @Test
    public void shouldIsGameOver() {
        board = board(
                "{'myBase':{'x':4,'y':4}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
                "'forces':'" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔════┐" +
                "║2..1│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘",
                "------" +
                "-♦----" +
                "------" +
                "------" +
                "------" +
                "------");

        // when then
        assertEquals(false, board.isMeAlive());
    }

    @Test
    public void shouldFindWay_withoutBarriers() {
        // given
        board = board(
                "{'myBase':{'x':1,'y':4}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
                "'forces':'" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#'" +
                        "}",
                "╔════┐" +
                "║1...│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-♥----" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        Point from = board.getMyForces().get(0).getRegion();
        List<Direction> way = board.getShortestWay(from, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withBoxes() {
        // given
        board = board(
                "{'myBase':{'x':1,'y':4}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
                "'forces':'" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#'" +
                        "}",
                "╔════┐" +
                "║1...│" +
                "║B...│" +
                "║.B..│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-♥----" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        Point from = board.getMyForces().get(0).getRegion();
        List<Direction> way = board.getShortestWay(from, board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, DOWN, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withHoles() {
        // given
        board = board(
                "{'myBase':{'x':1,'y':4}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
                "'forces':'" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#'" +
                        "}",
                "╔════┐" +
                "║1...│" +
                "║O...│" +
                "║O...│" +
                "║OOO$│" +
                "└────┘",
                "------" +
                "-♥----" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        Point from = board.getMyForces().get(0).getRegion();
        List<Direction> way = board.getShortestWay(from, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withOtherForces() {
        // given
        board = board(
                "{'myBase':{'x':1,'y':4}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
                "'forces':'" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#'" +
                        "}",
                "╔════┐" +
                "║1...│" +
                "║2...│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-♥----" +
                "-♦----" +
                "------" +
                "------" +
                "------");

        // when
        Point from = board.getMyForces().get(0).getRegion();
        List<Direction> way = board.getShortestWay(from, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withForces() {
        // given
        board = board(
                "{'myBase':{'x':1,'y':4}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
                "'forces':'" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#'" +
                "}",
                "╔════┐" +
                "║1...│" +
                "║....│" +
                "║....│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-♥----" +
                "-♥----" +
                "------" +
                "------" +
                "------");

        // when
        Point from = board.getMyForces().get(0).getRegion();
        assertEquals("[1,3]", from.toString());
        List<Direction> way = board.getShortestWay(from, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }

    @Test
    public void shouldFindWay_withStartEnd() {
        // given
        board = board(
                "{'myBase':{'x':1,'y':4}," +
                "'myColor':0," +
                "'available':10," +
                "'inLobby':false," +
                "'round':1," +
                "'rounds':10," +
                "'tick':10," +
                "'forces':'" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#00A-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#" +
                        "-=#-=#-=#-=#-=#-=#'" +
                        "}",
                "╔════┐" +
                "║1...│" +
                "║2...│" +
                "║E...│" +
                "║...$│" +
                "└────┘",
                "------" +
                "-♥----" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        Point from = board.getMyForces().get(0).getRegion();
        List<Direction> way = board.getShortestWay(from, board.getGold());

        // then
        assertEquals("[DOWN, DOWN, DOWN, RIGHT, RIGHT, RIGHT]", way.toString());
    }
}
