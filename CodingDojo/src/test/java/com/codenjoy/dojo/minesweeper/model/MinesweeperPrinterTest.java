package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.Mine;
import com.codenjoy.dojo.minesweeper.model.objects.Sapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MinesweeperPrinterTest {

    private MockBoard board;

    @Test
    public void shouldPrintBoard_shouldWalkOnBoardRight() {
        shouldBoardWith(new Sapper(1, 1));

        board.getJoystick().right();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼* ☺☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        board.getJoystick().down();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*  ☼\n" +
                "☼**☺☼\n" +
                "☼☼☼☼☼\n");

        board.getJoystick().left();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*  ☼\n" +
                "☼*☺ ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_shouldWalkOnBoardDown() {
        shouldBoardWith(new Sapper(1, 1));

        board.getJoystick().down();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼* *☼\n" +
                "☼*☺*☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_shouldWalkOnBoardUp() {
        shouldBoardWith(new Sapper(1, 1));

        board.getJoystick().up();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*☺*☼\n" +
                "☼* *☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_shouldWalkOnBoardLeft() {
        shouldBoardWith(new Sapper(1, 1));

        board.getJoystick().left();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺ *☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_shouldSetFlagRight() {
        shouldBoardWith(new Sapper(1, 1));

        board.getJoystick().act();
        board.getJoystick().right();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_shouldSetFlagUp() {
        shouldBoardWith(new Sapper(1, 1));

        board.getJoystick().act();
        board.getJoystick().up();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_shouldSetFlagDown() {
        shouldBoardWith(new Sapper(1, 1));

        board.getJoystick().act();
        board.getJoystick().down();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_shouldSetFlagLeft() {
        shouldBoardWith(new Sapper(1, 1));

        board.getJoystick().act();
        board.getJoystick().left();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼‼☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenSapperAtBombs() {
        shouldBoardWith(new Sapper(1, 1), new Mine(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*Ѡ*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperNoBombs() {
        shouldBoardWith(new Sapper(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperOneBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperTwoBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2), new Mine(2, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*2*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperThreeBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2), new Mine(2, 1), new Mine(2, 0));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*3*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperFourBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2), new Mine(2, 1), new Mine(2, 0),
                new Mine(1, 0));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*4*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperFiveBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2), new Mine(2, 1), new Mine(2, 0),
                new Mine(1, 0), new Mine(1, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*5*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperSixBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2), new Mine(2, 1), new Mine(2, 0),
                new Mine(1, 0), new Mine(1, 2),
                new Mine(0, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*6*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperSevenBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2), new Mine(2, 1), new Mine(2, 0),
                new Mine(1, 0), new Mine(1, 2),
                new Mine(0, 2), new Mine(0, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*7*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperEightBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2), new Mine(2, 1), new Mine(2, 0),
                new Mine(1, 0), new Mine(1, 2),
                new Mine(0, 2), new Mine(0, 1), new Mine(0, 0));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*8*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    private void assertBoard(String expected) {
        assertEquals(expected, new MinesweeperPrinter(board).print());
    }

    private void shouldBoardWith(Sapper sapper, Mine... mines) {
        board = new MockBoard(sapper, mines);
    }

    private class MockBoard extends BoardImpl {
        private Sapper sapper;
        private Mine[] mines;

        public MockBoard(Sapper sapper, Mine...mines) {
            super(3, 1, 3, new MinesGenerator() {

                @Override
                public List<Mine> get(int count, Board board) {
                    return new ArrayList<Mine>();
                }
            }, null);
            this.sapper = sapper;
            this.mines = mines;
            newGame();
        }

        @Override
        public List<Mine> getMines() {
            return Arrays.asList(mines);
        }

        @Override
        public int getMinesNearSapper() {
            return getMines().size();
        }

        @Override
        protected Sapper initializeSapper() {
            return sapper;
        }

        @Override
        public int getMinesCount() {
            return getMines().size();
        }

    }

}
