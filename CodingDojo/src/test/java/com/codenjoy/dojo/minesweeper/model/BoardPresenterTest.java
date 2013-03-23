package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.Mine;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BoardPresenterTest {

    public static final boolean SHOW_MINES = true;

    @Test
    public void shouldPrintBoard() {
        Board board = new MockBoard();
        assertEquals("" +
                "# # # # # \n" +
                "# . . * # \n" +
                "# . @ * # \n" +
                "# . . * # \n" +
                "# # # # # \n" +
                "mines on board: 3\n" +
                "mines near supper: 3\n" +
                "mine detector charge: 3",
                new BoardPresenter(SHOW_MINES, board).print());
    }

    private class MockBoard extends BoardImpl {
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
            return Arrays
                    .asList(new Mine(2, 2), new Mine(2, 1), new Mine(2, 0));
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
