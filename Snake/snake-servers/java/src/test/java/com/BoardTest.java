package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 2:03 AM
 */
public class BoardTest {
    @Test
    public void should() {
        Board board = new Board(
                "****" +
                "*X0*" +
                "*@#*" +
                "****");
        assertEquals(
                "****\n" +
                "*X0*\n" +
                "*@#*\n" +
                "****\n", board.fix());

        assertEquals("[1,1]", board.getApple().toString());
        assertEquals("[2,1]", board.getHead().toString());
        assertEquals(4, board.size());
        assertEquals("[1,2]", board.getStone().toString());

        assertTrue(board.isAt(0, 0, Board.WALL));
        assertTrue(board.isAt(0, 1, Board.WALL));
        assertTrue(board.isAt(0, 2, Board.WALL));
        assertTrue(board.isAt(0, 3, Board.WALL));

        assertTrue(board.isAt(3, 0, Board.WALL));
        assertTrue(board.isAt(3, 1, Board.WALL));
        assertTrue(board.isAt(3, 2, Board.WALL));
        assertTrue(board.isAt(3, 3, Board.WALL));

        assertTrue(board.isAt(0, 0, Board.WALL));
        assertTrue(board.isAt(1, 0, Board.WALL));
        assertTrue(board.isAt(2, 0, Board.WALL));
        assertTrue(board.isAt(3, 0, Board.WALL));

        assertTrue(board.isAt(0, 3, Board.WALL));
        assertTrue(board.isAt(1, 3, Board.WALL));
        assertTrue(board.isAt(2, 3, Board.WALL));
        assertTrue(board.isAt(3, 3, Board.WALL));

        assertTrue(board.isAt(1, 1, Board.APPLE));
        assertTrue(board.isAt(1, 2, Board.STONE));
        assertTrue(board.isAt(2, 1, Board.HEAD));
        assertTrue(board.isAt(2, 2, Board.BODY));

        assertEquals(Direction.DOWN, board.getSnakeDirection());
    }

}
