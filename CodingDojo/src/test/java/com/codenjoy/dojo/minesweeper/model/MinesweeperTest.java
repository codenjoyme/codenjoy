package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.Mine;
import com.codenjoy.dojo.minesweeper.model.objects.Sapper;
import com.codenjoy.dojo.minesweeper.services.MinesweeperEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Printer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MinesweeperTest {

    private MockBoard board;
    private int size = 3;
    private List<Mine> mines;
    private int detectorCharge = 3;
    private EventListener listener;

    private void shouldSize(int size) {
        this.size = size;
    }

    private void shouldDetectorCharge(int charge) {
        this.detectorCharge = charge;
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardRight() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1☺☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼**☺☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
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
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*☺*☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*1☺☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1☺☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼**☺☼\n" +
                "☼*1 ☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardUp() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*☺*☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼☺ *☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  *☼\n" +
                "☼☺1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetRight() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

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
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

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
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

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
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

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
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1Ѡ☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(board.isGameOver());
    }

    @Test
    public void shouldSaveCommandAndActAfterTick() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        board.getJoystick().right();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        board.tick();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1Ѡ☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(board.isGameOver());
    }

    @Test
    public void shouldPrintAllBombs_whenSapperAtBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3));

        unbombUp();
        unbombDown();
        unbombLeft();
        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ x☻☼\n" +
                "☼‼3Ѡ☼\n" +
                "☼ x☻☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(board.isGameOver());
    }

    @Test
    public void shouldPrintBoard_whenNearSapperNoBombs() {
        size = 7;
        shouldBoardWith(new Sapper(3, 3), new Mine(1, 1));

        assertBoard(
                "☼☼☼☼☼☼☼\n" +
                "☼*****☼\n" +
                "☼*****☼\n" +
                "☼**☺**☼\n" +
                "☼*****☼\n" +
                "☼*****☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperOneBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperTwoBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺2*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperThreeBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺3*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperFourBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺4*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperFiveBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺5*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

    }

    @Test
    public void shouldPrintBoard_whenNearSapperSixBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3),
                new Mine(1, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺6*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperSevenBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3),
                new Mine(1, 3), new Mine(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺7*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperEightBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3),
                new Mine(1, 3), new Mine(1, 2), new Mine(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼☻☻☻☼\n" +
                "☼☻8☻☼\n" +
                "☼☻Ѡ☻☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombRight() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombRightAndLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2), new Mine(1, 2));

        unbombRight();
        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x☺x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombRight() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombDown() {
        shouldBoardWith(new Sapper(2, 2), new Mine(2, 1));

        unbombDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼ x ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombDown() {
        shouldBoardWith(new Sapper(2, 2), new Mine(2, 1));

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombUp() {
        shouldBoardWith(new Sapper(2, 2), new Mine(2, 3));

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ x ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombUp() {
        shouldBoardWith(new Sapper(2, 2), new Mine(2, 3));

        unbombDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 2));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    private void assertWin() {
        assertTrue(board.isWin());
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼‼☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldWin_whenDestroyAllBombs() {
        detectorCharge = 8;
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3),
                new Mine(1, 3), new Mine(1, 2), new Mine(1, 1));

        unbombLeft();
        unbombDown();
        unbombRight();
        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼‼☺‼☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveUp();
        unbombLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼‼☺‼☼\n" +
                "☼‼2‼☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveDown();
        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼‼‼‼☼\n" +
                "☼‼2‼☼\n" +
                "☼*☺*☼\n" +
                "☼☼☼☼☼\n");

        unbombLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼xxx☼\n" +
                "☼x x☼\n" +
                "☼x☺x☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldLeaveBombMap_whenWalkBetweenBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 3), new Mine(2, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☺**☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼1☺*☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼11☺☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼**☺☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼**☺☼\n" +
                "☼**2☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");
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
        assertEquals(expected, new Printer(board.getSize(), new MinesweeperPrinter(board)).toString());
    }

    private void shouldBoardWith(Sapper sapper, Mine... mines) {
        listener = mock(EventListener.class);
        board = new MockBoard(sapper, mines);
        board.newGame();
    }

    private class MockBoard extends Minesweeper {
        private Sapper sapper;

        public MockBoard(Sapper sapper, Mine...mines) {
            super(v(size), v(0), v(detectorCharge), new MinesGenerator() {
                @Override
                public List<Mine> get(int count, Field board) {
                    return new ArrayList<Mine>();
                }
            }, listener);
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

    @Test
    public void shouldFireEvent_whenDie() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1Ѡ☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(MinesweeperEvents.KILL_ON_MINE);
    }

    private void verifyEvents(MinesweeperEvents... events) {
        for (MinesweeperEvents event : events) {
            verify(listener).event(event);
        }
    }

    @Test
    public void shouldFireEvent_whenOpenSpace() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1☺☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(MinesweeperEvents.CLEAN_BOARD);
    }

    @Test
    public void shouldNotFireEvent_whenReturnsHome() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1☺☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(MinesweeperEvents.CLEAN_BOARD);

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺ ☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldFireEvent_whenNoMoreCharge() {
        detectorCharge = 3;
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        unbombDown();
        unbombLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼‼☺‼☼\n" +
                "☼☻‼ ☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(3, MinesweeperEvents.FORGET_CHARGE);
        verifyEvents(MinesweeperEvents.NO_MORE_CHARGE);

        unbombUp();
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldPrintAllBoardBombs_whenNoMoreCharge() {
        detectorCharge = 3;
        size = 7;
        shouldBoardWith(new Sapper(3, 3), new Mine(1, 1), new Mine(5, 5), new Mine(4, 2), new Mine(2, 4), new Mine(2, 1));

        unbombDown();
        unbombLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼☼☼\n" +
                "☼1111☻☼\n" +
                "☼1☻111☼\n" +
                "☼1‼☺‼1☼\n" +
                "☼22‼☻1☼\n" +
                "☼☻☻211☼\n" +
                "☼☼☼☼☼☼☼\n");
    }

    private void verifyEvents(int count, MinesweeperEvents event) {
        verify(listener, times(count)).event(event);
    }

    @Test
    public void shouldFireEvent_whenCleanMine() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2), new Mine(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(MinesweeperEvents.DESTROY_MINE);
    }

    @Test
    public void shouldFireEvent_whenCleanAllMines() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 2));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(
                MinesweeperEvents.DESTROY_MINE,
                MinesweeperEvents.WIN);
    }

    @Test
    public void shouldOnlyOneFlagPerSpace() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(MinesweeperEvents.FORGET_CHARGE);

        unbombRight();

        verifyNoMoreInteractions(listener);
    }


    @Test
    public void shouldCantGoOnBoard() {
        shouldBoardWith(new Sapper(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        moveRight();
        moveRight();
        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n");

        moveUp();
        moveUp();
        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCantUnbombOnBoard() {
        shouldBoardWith(new Sapper(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        unbombLeft();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        unbombDown();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        moveRight();
        moveRight();
        reset(listener);

        unbombRight();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n");

        moveUp();
        moveUp();
        reset(listener);

        unbombUp();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }
}
