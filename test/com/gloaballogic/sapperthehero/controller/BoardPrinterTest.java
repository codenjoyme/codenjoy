package com.gloaballogic.sapperthehero.controller;

import com.globallogic.sapperthehero.controller.BoardPresenter;
import com.globallogic.sapperthehero.game.Board;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;

public class BoardPrinterTest {

    @Test
    public void shouldPrintBoard() {
        Board board = new MockBoard();
        assertNotNull(new BoardPresenter(true, board));
    }

    private class MockBoard extends Board {
        public MockBoard() {
            super(16, 1, 3);
        }
    }
}
