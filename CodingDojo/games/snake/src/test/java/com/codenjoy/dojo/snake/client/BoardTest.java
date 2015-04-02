package com.codenjoy.dojo.snake.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
                "☼☼☼☼" +
                "☼☻○☼" +
                "☼☺▼☼" +
                "☼☼☼☼");
        assertEquals(
                "☼☼☼☼\n" +
                "☼☻○☼\n" +
                "☼☺▼☼\n" +
                "☼☼☼☼\n", board.fix());

        assertEquals("[1,1]", board.getApple().toString());
        assertEquals("[2,1]", board.getHead().toString());
        assertEquals(4, board.size());
        assertEquals("[[1,2]]", board.getStones().toString());

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
        assertTrue(board.isAt(2, 1, Board.HEAD_DOWN));
        assertTrue(board.isAt(2, 2, Board.BODY));

        assertEquals(Direction.DOWN, board.getSnakeDirection());

        assertEquals("[[2,1], [2,2]]", board.getSnake().toString());

        assertEquals("[[2,1], [2,2], " +   // змейка
                "[1,2], " +         // камень
                "[0,3], [1,3], [2,3], [3,3], [0,2], [3,2], [0,1], [3,1], [0,0], [1,0], [2,0], [3,0]]",   // стены
                board.getBarriers().toString());

        assertEquals(
                "Board:\n" +
                "☼☼☼☼\n" +
                "☼☻○☼\n" +
                "☼☺▼☼\n" +
                "☼☼☼☼\n" +
                "\n" +
                "Apple at: [1,1]\n" +
                "Stones at: [[1,2]]\n" +
                "Head at: [2,1]\n" +
                "Snake at: [[2,1], [2,2]]\n" +
                "Current direction: DOWN",
                board.toString());
    }

    @Test
    public void shouldWithoutStone() {
        Board board = new Board(
                "☼☼☼☼" +
                "☼ ○☼" +
                "☼☺▼☼" +
                "☼☼☼☼");
        assertEquals("[]", board.getStones().toString());
    }

    @Test
    public void shouldWith2Stone() {
        Board board = new Board(
                "☼☼☼☼" +
                "☼☻○☼" +
                "☼☻▼☼" +
                "☼☼☼☼");
        assertEquals("[[1,2], [1,1]]", board.getStones().toString());
    }
}
