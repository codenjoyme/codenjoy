package com.gloaballogic.sapperthehero.controller;

import com.globallogic.sapperthehero.controller.BoardPresenter;
import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Mine;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class BoardPresenterTest {

    public static final boolean SHOW_MINES = true;

    @Test
    public void shouldPrintBoard() {
        Board board = new MockBoard();
        assertEquals("" +
                "# # # # # # # \n" +
                "# . . . . . # \n" +
                "# . @ . . . # \n" +
                "# . . * . . # \n" +
                "# . . . . . # \n" +
                "# . . . . . # \n" +
                "# # # # # # # \n" +
                "мин на поле: 1\n" +
                "мин рядом со мной: 0\n" +
                "заряов у детектора: 3",
                new BoardPresenter(SHOW_MINES, board).print());
    }

    private class MockBoard extends Board {
        public MockBoard() {
            super(5, 1, 3);
        }

        @Override
        public List<Mine> getMines() {
            return Arrays.asList(new Mine(2, 2));
        }


    }
}
