package com.codenjoy.dojo.expansion.client;

import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AISolverTest {

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
                "'forces':'" +
                    "-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#-=#" +
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
        List<Direction> way = AISolver.getShortestWay(board, from, board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, RIGHT, DOWN]", way.toString());
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
        List<Direction> way = AISolver.getShortestWay(board, from, board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, RIGHT, DOWN]", way.toString());
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
                "║1OOO│" +
                "║....│" +
                "║OOO.│" +
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
        List<Direction> way = AISolver.getShortestWay(board, from, board.getGold());

        // then
        assertEquals("[DOWN, RIGHT, RIGHT, RIGHT, DOWN, DOWN]", way.toString());
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
        List<Direction> way = AISolver.getShortestWay(board, from, board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, RIGHT, DOWN]", way.toString());
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
        List<Direction> way = AISolver.getShortestWay(board, from, board.getGold());

        // then
        assertEquals("[RIGHT, RIGHT, DOWN, RIGHT, DOWN]", way.toString());
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
        List<Direction> way = AISolver.getShortestWay(board, from, board.getGold());

        // then
        assertEquals("[RIGHT, DOWN, RIGHT, DOWN, RIGHT, DOWN]", way.toString());
    }
}