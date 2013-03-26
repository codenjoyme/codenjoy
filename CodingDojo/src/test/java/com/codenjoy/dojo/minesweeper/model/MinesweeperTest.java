package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.Mine;
import com.codenjoy.dojo.minesweeper.model.objects.Sapper;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MinesweeperTest {

    private MockBoard board;
    private int size = 3;
    private List<Mine> mines;
    private int detectorCharge = 3;

    private void shouldSize(int size) {
        this.size = size;
    }

    private void shouldDetectorCharge(int charge) {
        this.detectorCharge = charge;
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardRight() {
        shouldBoardWith(new Sapper(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼* ☺☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*  ☼\n" +
                "☼**☺☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*  ☼\n" +
                "☼*☺ ☼\n" +
                "☼☼☼☼☼\n");
    }

    private void moveLeft() {
        board.getJoystick().left();
        board.tick();
    }

    private void moveDown() {
        board.getJoystick().down();
        board.tick();
    }

    private void moveRight() {
        board.getJoystick().right();
        board.tick();
    }

    @Test
    public void shouldLeaveEmptySpaceshouldWalkOnBoardDown() {
        shouldBoardWith(new Sapper(1, 1));

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼* *☼\n" +
                "☼*☺*☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardUp() {
        shouldBoardWith(new Sapper(1, 1));

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*☺*☼\n" +
                "☼* *☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardLeft() {
        shouldBoardWith(new Sapper(1, 1));

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺ *☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetRight() {
        shouldBoardWith(new Sapper(1, 1));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetUp() {
        shouldBoardWith(new Sapper(1, 1));

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetDown() {
        shouldBoardWith(new Sapper(1, 1));

        unbombDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetLeft() {
        shouldBoardWith(new Sapper(1, 1));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼‼☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldDie_whenSapperAtBombs() {
        shouldBoardWith(new Sapper(1, 1), new Mine(2, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼* Ѡ☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(board.isGameOver());
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

    @Test
    public void shouldSetFlagOnBomb_whenBombRight() {
        shouldBoardWith(new Sapper(1, 1), new Mine(2, 1));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombRight() {
        shouldBoardWith(new Sapper(1, 1), new Mine(0, 1));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombDown() {
        shouldBoardWith(new Sapper(1, 1), new Mine(1, 0));

        unbombDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombDown() {
        shouldBoardWith(new Sapper(1, 1), new Mine(1, 0));

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombUp() {
        shouldBoardWith(new Sapper(1, 1), new Mine(1, 2));

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombUp() {
        shouldBoardWith(new Sapper(1, 1), new Mine(1, 2));

        unbombDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombLeft() {
        shouldBoardWith(new Sapper(1, 1), new Mine(0, 1));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼‼☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    private void assertWin() {
        assertTrue(board.isWin());
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombLeft() {
        shouldBoardWith(new Sapper(1, 1), new Mine(2, 1));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼‼1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldWin_whenDestroyAllBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 2), new Mine(2, 1), new Mine(2, 0),
                new Mine(1, 0), new Mine(1, 2),
                new Mine(0, 2), new Mine(0, 1), new Mine(0, 0));

        unbombLeft();

        unbombDown();

        unbombRight();

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼‼4‼☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveUp();

        unbombLeft();

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼‼☺‼☼\n" +
                "☼‼ ‼☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    private void moveUp() {
        board.getJoystick().up();
        board.tick();
    }

    private void assertStillNotWin() {
        assertFalse(board.isWin());
    }

    private void unbombUp() {
        board.getJoystick().act();
        moveUp();
    }

    private void unbombRight() {
        board.getJoystick().act();
        moveRight();
    }

    private void unbombDown() {
        board.getJoystick().act();
        moveDown();
    }

    private void unbombLeft() {
        board.getJoystick().act();
        moveLeft();
    }

    private void assertBoard(String expected) {
        assertEquals(expected, new MinesweeperPrinter(board).print());
    }

    private void shouldBoardWith(Sapper sapper, Mine... mines) {
        board = new MockBoard(sapper, mines);
    }

    private class MockBoard extends BoardImpl {
        private Sapper sapper;

        public MockBoard(Sapper sapper, Mine...mines) {
            super(size, 0, detectorCharge, new MinesGenerator() {
                @Override
                public List<Mine> get(int count, Board board) {
                    return new ArrayList<Mine>();
                }
            }, null);
            this.sapper = sapper;
            MinesweeperTest.this.mines = new LinkedList<Mine>();
            MinesweeperTest.this.mines.addAll(Arrays.asList(mines));
            newGame();
        }

        @Override
        public List<Mine> getMines() {
            return MinesweeperTest.this.mines;
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
