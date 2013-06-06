package com.codenjoy.dojo.minesweeper.model;

import com.codenjoy.dojo.minesweeper.model.objects.*;
import com.codenjoy.dojo.services.EventListener;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * User: oleksii.morozov Date: 10/14/12 Time: 11:04 AM
 */

public class SapperTheHeroTest {
    private static final int MINES_COUNT = 4;
    private static final int BOARD_SIZE = 4;
    private static final int CHARGE_COUNT = 8;
    private Board board;
    private Sapper sapper;
    private List<Mine> mines;
    private final MinesGenerator NO_MINES = new MockGenerator();
    private EventListener listener;

    @Before
    public void gameStart() {
        board = new BoardImpl(BOARD_SIZE, MINES_COUNT, CHARGE_COUNT, NO_MINES, listener);
        board.newGame();
        sapper = board.getSapper();
        mines = board.getMines();
        listener = mock(EventListener.class);
    }

    class MockGenerator implements MinesGenerator {

        @Override
        public List<Mine> get(int count, Board board) {
            return new ArrayList<Mine>();
        }
    }

    @Test
    public void shouldBoardConsistOfCells() {
        assertNotNull(board.getCells());
    }

    @Test
    public void shouldFreeCellsNumberBeMoreThanZero() {
        assertTrue(board.getFreeCells().size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldBoardSizeMoreThanOne_whenGameStart() {
        new BoardImpl(0, MINES_COUNT, CHARGE_COUNT, NO_MINES, listener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldMinesCountLessThenAllCells_whenGameStart() {
        new BoardImpl(2, 10, CHARGE_COUNT, NO_MINES, listener);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldMineDetectorChargeMoreThanMines_whenGameStart() {
        new BoardImpl(BOARD_SIZE, 10, CHARGE_COUNT, NO_MINES, listener);
    }

    @Test
    public void shouldBoardSizeSpecify_whenGameStart() {
        board = new BoardImpl(10, MINES_COUNT, CHARGE_COUNT, NO_MINES, listener);
        assertEquals(10, board.getSize());
    }

    @Test
    public void shouldBoardBeSquare() {
        assertEquals(board.getCells().size() % board.getSize(), 0);
    }

    @Test
    public void shouldBoardCellsNumberBeMoreThanOne() {
        assertTrue(board.getCells().size() > 1);
    }

    @Test
    public void shouldSapperOnBoard() {
        assertNotNull(sapper);
    }

    @Test
    public void shouldSapperBeAtBoardDefaultPosition() {
        assertEquals(sapper, new CellImpl(1, 1));
    }

    @Test
    public void shouldMinesOnBoard() {
        assertNotNull(mines);
    }

    @Test
    public void shouldMinesCountSpecify_whenGameStart() {
        assertNotNull(board.getMinesCount());
    }

    @Test
    public void shouldFreeCellsDecrease_whenCreatesSapperAndMines() {
        assertEquals(board.getCells().size(), board.getFreeCells().size()
                + mines.size() + 1);
    }

    @Test
    public void shouldSapperMoveToUp() {
        int oldYPosition = sapper.getY();

        board.sapperMoveTo(Direction.UP);

        assertEquals(sapper.getY(), oldYPosition + 1);
    }

    @Test
    public void shouldSapperMoveToDown() {
        int oldYPosition = sapper.getY();

        board.sapperMoveTo(Direction.DOWN);

        assertEquals(sapper.getY(), oldYPosition - 1);
    }

    @Test
    public void shouldSapperMoveToLeft() {
        int oldXPosition = sapper.getX();

        board.sapperMoveTo(Direction.LEFT);

        assertEquals(sapper.getX(), oldXPosition - 1);
    }

    @Test
    public void shouldSapperMoveToRight() {
        int oldXPosition = sapper.getX();

        board.sapperMoveTo(Direction.RIGHT);

        assertEquals(sapper.getX(), oldXPosition + 1);
    }

    private void givenSapperMovedToMine() {
        placeMineDownFromSapper();
        board.sapperMoveTo(Direction.DOWN);
    }

    private void placeMineDownFromSapper() {
        Cell result = new CellImpl(sapper.getX(), sapper.getY() - 1);
        if (!mines.contains(result)) {
            board.createMineOnPositionIfPossible(result);
        }
    }

    @Test
    public void shouldGameIsOver_whenSapperIsDead() {
        givenSapperMovedToMine();

        assertEquals(board.isGameOver(), sapper.isDead());
    }

    @Test
    public void shouldNextTurn_whenSapperMove() {
        int turnBeforeSapperMotion = board.getTurn();

        board.sapperMoveTo(Direction.DOWN);
        int turnAfterSapperMotion = board.getTurn();

        assertEquals(turnBeforeSapperMotion, turnAfterSapperMotion - 1);
    }

    @Test
    public void shouldSapperKnowsHowMuchMinesNearHim_whenAtLeastOneIsDownFromSapper() {
        placeMineDownFromSapper();

        assertTrue(board.getMinesNearSapper() > 0);
    }

    @Test
    public void shouldMineDetectorHaveCharge() {
        assertNotNull(sapper.getMineDetector().getCharge());
    }

    @Test
    public void shouldMineDetectorChargeMoreThanMinesOnBoard() {
        assertTrue(sapper.getMineDetector().getCharge() > board.getMinesCount());
    }

    @Test
    public void shouldSapperDestroyMine_whenMineExistInGivenDirection() {
        for (Direction direction : Direction.values()) {

            board.useMineDetectorToGivenDirection(direction);
            boolean isMineInDirection = board.getMines().contains(
                    board.getCellPossiblePosition(direction));

            assertTrue(!isMineInDirection);
        }
    }

    @Test
    public void shouldMineDetectorChargeDecreaseByOne_whenUse() {
        int mineDetectorCharge = sapper.getMineDetector().getCharge();

        board.useMineDetectorToGivenDirection(Direction.DOWN);
        int mineDetectorChargeWhenUse = sapper.getMineDetector().getCharge();

        assertEquals(mineDetectorCharge, mineDetectorChargeWhenUse + 1);
    }

    @Test
    public void shouldMineCountDecreaseByOne_whenMineIsDestroyed() {
        placeMineDownFromSapper();
        int minesCount = board.getMinesCount();

        board.useMineDetectorToGivenDirection(Direction.DOWN);
        int minesCountWhenMineDestroyed = board.getMinesCount();

        assertEquals(minesCount, minesCountWhenMineDestroyed + 1);
    }

    @Test
    public void shouldWin_whenNoMoreMines() {
        placeMineDownFromSapper();

        board.useMineDetectorToGivenDirection(Direction.DOWN);

        assertTrue(board.isWin());
    }

    @Test
    public void shouldGameOver_whenNoMoreCharge() {
        placeMineDownFromSapper();
        for (int count = 1; count <= CHARGE_COUNT; count++) {
            board.useMineDetectorToGivenDirection(Direction.UP);
        }

        assertTrue(board.isGameOver());
    }

}