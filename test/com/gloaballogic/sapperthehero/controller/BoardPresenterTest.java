package com.gloaballogic.sapperthehero.controller;

import com.globallogic.sapperthehero.controller.BoardPresenter;
import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Mine;
import com.globallogic.sapperthehero.game.MinesGenerator;
import com.globallogic.sapperthehero.game.impl.RandomMinesGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class BoardPresenterTest {

    public static final boolean SHOW_MINES = true;
    private RandomMinesGenerator minesGenerator;

    @Test
    public void shouldPrintBoard() {
        Board board = new MockBoard();
        assertEquals("" +
                "# # # # # \n" +
                "# . . * # \n" +
                "# . @ * # \n" +
                "# . . * # \n" +
                "# # # # # \n" +
                "мин на поле: 3\n" +
                "мин рядом со мной: 3\n" +
                "заряов у детектора: 3",
                new BoardPresenter(SHOW_MINES, board).print());
    }

    private class MockBoard extends Board {
        public MockBoard() {
            super(3, 1, 3, new MinesGenerator() {

                @Override
                public List<Mine> get(int count, Board board) {
                    return new ArrayList<Mine>();
                }
            });
        }

        @Override
        public List<Mine> getMines() {
            return Arrays.asList(new Mine(2, 2), new Mine(2, 1), new Mine(2, 0));
        }

        @Override
        public int getMinesNearSapper() {
            return getMines().size();
        }

        @Override
        public int getMinesCount() {
            return getMines().size();
        }

    }
}
