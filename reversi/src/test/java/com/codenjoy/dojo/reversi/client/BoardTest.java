package com.codenjoy.dojo.reversi.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardTest {

    private Board board;

    @Test
    public void test_isMyTurn_myColor() {
        shouldB("    " +
                " xO " +
                " Ox " +
                "    ");

        assertEquals(true, board.isMyTurn());
        assertEquals(true, board.myColor());
        assertEquals(false, board.isWhite(1, 2));
        assertEquals(true, board.isBlack(1, 2));
        assertEquals(true, board.isWhite(2, 2));
        assertEquals(false, board.isBlack(2, 2));

        shouldB("    " +
                " -O " +
                " O- " +
                "    ");

        assertEquals(false, board.isMyTurn());
        assertEquals(false, board.myColor());
        assertEquals(false, board.isWhite(1, 2));
        assertEquals(true, board.isBlack(1, 2));
        assertEquals(true, board.isWhite(2, 2));
        assertEquals(false, board.isBlack(2, 2));

        shouldB("    " +
                " Xo " +
                " oX " +
                "    ");

        assertEquals(true, board.isMyTurn());
        assertEquals(false, board.myColor());
        assertEquals(false, board.isWhite(1, 2));
        assertEquals(true, board.isBlack(1, 2));
        assertEquals(true, board.isWhite(2, 2));
        assertEquals(false, board.isBlack(2, 2));

        shouldB("    " +
                " X- " +
                " -X " +
                "    ");

        assertEquals(false, board.isMyTurn());
        assertEquals(true, board.myColor());
        assertEquals(false, board.isWhite(1, 2));
        assertEquals(true, board.isBlack(1, 2));
        assertEquals(true, board.isWhite(2, 2));
        assertEquals(false, board.isBlack(2, 2));
    }

    private void shouldB(String string) {
        board = (Board)new Board().forString(string);
    }
}
