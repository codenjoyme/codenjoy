package com.gloaballogic.sapperthehero;

import com.globallogic.sapperthehero.game.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.*;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 11:04 AM
 */

public class SapperTheHeroTest {
    private static final int MINES_COUNT = 4;
    private static final int BOARD_SIZE = 4;
    private static final int CHARGE_COUNT = 8;
    private Board board;
    private Sapper sapper;
    private List<Mine> mines;
    private final MinesGenerator NO_MINES = new MockGenerator();


    @Before
    public void gameStart() {
        board = newBoard();
        sapper = board.getSapper();
        mines = board.getMines();
    }

    class MockGenerator implements MinesGenerator {

        @Override
        public List<Mine> get(int count, Board board) {
            return new ArrayList<Mine>();
        }
    }

    private Board newBoard() {
        return new Board(BOARD_SIZE, MINES_COUNT, CHARGE_COUNT, new MockGenerator());
    }

    @Test
    public void shouldBoardOnStartGame() {
        assertNotNull(board);
    }

    @Test
    public void shouldBoardConsistOfCells() {
        assertNotNull(board.getCells());
    }

    @Test
    public void shouldFreeCellsNumberBeMoreThanZero() {
        assertTrue(board.getFreeCells().size() > 0);
    }

    @Test
    public void shouldBoardSizeSpecifyAtGameStart() {
        board = new Board(10, MINES_COUNT, CHARGE_COUNT, NO_MINES);
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
        assertEquals(sapper, new Cell(1, 1));
    }

    @Test
    public void shouldMinesOnBoard() {
        assertNotNull(mines);
    }

    @Test
    public void shouldMinesCountSpecifyAtGameStart() {
        assertNotNull(board.getMinesCount());
    }

    //    @Test
    public void shouldFreeCellsDecreaseOnCreatingSapperAndMines() {
        assertEquals(board.getCells().size(), board.getFreeCells().size() + mines.size() + 1);
    }

    @Test
    public void shouldSapperMoveToUp() {
        int oldXPosition = sapper.getX();
        int oldYPosition = sapper.getY();

        board.sapperMoveTo(Direction.UP);
        int newXPosition = sapper.getX();
        int newYPosition = sapper.getY();

        assertTrue(oldXPosition == newXPosition);
        assertTrue(oldYPosition == newYPosition + 1);
    }

    @Test
    public void shouldSapperMoveToDown() {
        int oldXPosition = sapper.getX();
        int oldYPosition = sapper.getY();

        board.sapperMoveTo(Direction.DOWN);
        int newXPosition = sapper.getX();
        int newYPosition = sapper.getY();

        assertTrue(oldXPosition == newXPosition);
        assertTrue(oldYPosition == newYPosition - 1);
    }

    @Test
    public void shouldSapperMoveToLeft() {
        int oldXPosition = sapper.getX();
        int oldYPosition = sapper.getY();

        board.sapperMoveTo(Direction.LEFT);
        int newXPosition = sapper.getX();
        int newYPosition = sapper.getY();

        assertTrue(oldXPosition == newXPosition + 1);
        assertTrue(oldYPosition == newYPosition);
    }

    @Test
    public void shouldSapperMoveToRight() {
        int oldXPosition = sapper.getX();
        int oldYPosition = sapper.getY();

        board.sapperMoveTo(Direction.RIGHT);
        int newXPosition = sapper.getX();
        int newYPosition = sapper.getY();

        assertTrue(oldXPosition == newXPosition - 1);
        assertTrue(oldYPosition == newYPosition);
    }

    @Test
    public void shouldSapperMoveToMine() {
        givenSapperMovedToMine();


    }

    private void givenSapperMovedToMine() {
        placeMineNearSapper();
        board.sapperMoveTo(Direction.DOWN);
    }

    private void placeMineNearSapper() {
        Cell result = new Cell(sapper.getX(), sapper.getY() + 1);
        if (!mines.contains(result)) {
            board.createMineOnPositionIfPossible(result);
        }
    }

    @Test
    public void shouldGameIsOverIfSapperIsDead() {
        givenSapperMovedToMine();
        assertTrue(sapper.isDead());
        assertEquals(board.isGameOver(), sapper.isDead());
    }

    @Test
    public void shouldNextTurnAfterSapperMove() {
        int turnBeforeSapperMotion = board.getTurn();

        board.sapperMoveTo(Direction.DOWN);
        int turnAfterSapperMotion = board.getTurn();

        assertEquals(turnBeforeSapperMotion, turnAfterSapperMotion - 1);
    }


    @Test
    public void shouldSapperKnowsHowMuchMinesNearHim() {
        placeMineNearSapper();

        assertTrue(board.getMinesNearSapper() > 0);
    }

    @Test
    public void shouldSapperHaveMineDetector() {
        assertNotNull(sapper.getMineDetector());
    }

    @Test
    public void shouldMineDetectorHaveCharge() {
        assertNotNull(sapper.getMineDetectorCharge());
    }

    @Test
    public void shouldMineDetectorChargeMoreThanMinesOnBoard() {
        assertTrue(sapper.getMineDetectorCharge() > board.getMinesCount());
    }

    @Test
    public void shouldSapperDestroyMine_IfMineExistInGivenDirection() {
        for (Direction direction : Direction.values()) {
            board.useMineDetectorToGivenDirection(direction);
        }
    }

    //Если сапер использует детектор мин, то заряд батареи уменьшается.
    @Test
    public void shouldMineDetectorChargeDecreaseByOneAfterUse() {
        int chargeBeforeUse = sapper.getMineDetectorCharge();

        board.useMineDetectorToGivenDirection(Direction.DOWN);
        int chargeAfterUse = sapper.getMineDetectorCharge();

        assertEquals(chargeBeforeUse, chargeAfterUse + 1);
    }


    //Если минер разминирует мину, то значение количества мин вокруг него уменьшится на один.
    @Test
    public void shouldMineCountDecreaseByOneIfMineIsDestroyed() {
        placeMineNearSapper();
        int mineCountBeforeDestroying = board.getMinesCount();

        board.useMineDetectorToGivenDirection(Direction.DOWN);
        int mineCountAfterDestroying = board.getMinesCount();

        assertEquals(mineCountBeforeDestroying, mineCountAfterDestroying + 1);
    }

    //Если на поле остались мины и заряд батареи исчерпан, то сапер умирает.
    //Сапер знает о количестве мин на поле.
    //После движения сапера начинается новый ход
    //Появляется сообщение о причине смерти.

//    private class MockGenerator implements MinesGenerator {
//    }
}