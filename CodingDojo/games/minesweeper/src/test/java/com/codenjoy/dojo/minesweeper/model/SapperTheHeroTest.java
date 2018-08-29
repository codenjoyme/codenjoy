package com.codenjoy.dojo.minesweeper.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.settings.Parameter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * User: oleksii.morozov Date: 10/14/12 Time: 11:04 AM
 */

public class SapperTheHeroTest {
    private static final int MINES_COUNT = 4;
    private static final int BOARD_SIZE = 5;
    private static final int CHARGE_COUNT = 8;
    private Field board;
    private Sapper sapper;
    private List<Mine> mines;
    private final MinesGenerator NO_MINES = new MockGenerator();
    private EventListener listener;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void gameStart() {
        board = new Minesweeper(v(BOARD_SIZE), v(MINES_COUNT), v(CHARGE_COUNT), NO_MINES);
        board.newGame(new Player(listener));
        sapper = board.sapper();
        mines = board.getMines();
        listener = mock(EventListener.class);
    }

    class MockGenerator implements MinesGenerator {

        @Override
        public List<Mine> get(int count, Field board) {
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

    @Test
    public void shouldBoardSizeMoreThanOne_whenGameStart() {
        Parameter<Integer> boardSize = v(0);
        new Minesweeper(boardSize, v(MINES_COUNT), v(CHARGE_COUNT), NO_MINES).newGame(new Player(listener));
        assertEquals(5, boardSize.getValue().intValue());
    }

    @Test
    public void shouldMinesCountLessThenAllCells_whenGameStart() {
        Parameter<Integer> minesCount = v(100);
        new Minesweeper(v(2), minesCount, v(CHARGE_COUNT), NO_MINES).newGame(new Player(listener));
        assertEquals(12, minesCount.getValue().intValue());
    }

    @Test
    public void shouldMineDetectorChargeMoreThanMines_whenGameStart() {
        Parameter<Integer> chargeCount = v(CHARGE_COUNT);
        new Minesweeper(v(BOARD_SIZE), v(10), chargeCount, NO_MINES).newGame(new Player(listener));
        assertEquals(10, chargeCount.getValue().intValue());
    }

    @Test
    public void shouldBoardSizeSpecify_whenGameStart() {
        board = new Minesweeper(v(10), v(MINES_COUNT), v(CHARGE_COUNT), NO_MINES);
        assertEquals(10, board.size());
    }

    @Test
    public void shouldBoardBeSquare() {
        assertEquals(board.getCells().size() % (board.size() - 2), 0);
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
        assertEquals(sapper, pt(1, 1));
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
        int borders = 0; // (board.getSize() - 1) * 4;
        int freeCells = board.getFreeCells().size();
        int sapper = 1;
        int mines = this.mines.size();

        assertEquals(board.getCells().size(),
                freeCells + mines + sapper + borders);
    }

    @Test
    public void shouldSapperMoveToUp() {
        int oldYPosition = sapper.getY();

        board.sapperMoveTo(Direction.UP);

        assertEquals(sapper.getY(), oldYPosition + 1);
    }

    @Test
    public void shouldSapperMoveToDown() {
        board.sapperMoveTo(Direction.UP);

        int oldYPosition = sapper.getY();

        board.sapperMoveTo(Direction.DOWN);

        assertEquals(sapper.getY(), oldYPosition - 1);
    }

    @Test
    public void shouldSapperMoveToLeft() {
        board.sapperMoveTo(Direction.RIGHT);

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
        placeMineUpFromSapper();
        board.sapperMoveTo(Direction.UP);
    }

    private void placeMineUpFromSapper() {
        Point result = pt(sapper.getX(), sapper.getY() + 1);
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

        board.sapperMoveTo(Direction.UP);
        int turnAfterSapperMotion = board.getTurn();

        assertEquals(turnBeforeSapperMotion, turnAfterSapperMotion - 1);
    }

    @Test
    public void shouldSapperKnowsHowMuchMinesNearHim_whenAtLeastOneIsDownFromSapper() {
        placeMineUpFromSapper();

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

        board.useMineDetectorToGivenDirection(Direction.UP);
        int mineDetectorChargeWhenUse = sapper.getMineDetector().getCharge();

        assertEquals(mineDetectorCharge, mineDetectorChargeWhenUse + 1);
    }

    @Test
    public void shouldMineCountDecreaseByOne_whenMineIsDestroyed() {
        placeMineUpFromSapper();
        int minesCount = board.getMinesCount();

        board.useMineDetectorToGivenDirection(Direction.UP);
        int minesCountWhenMineDestroyed = board.getMinesCount();

        assertEquals(minesCount, minesCountWhenMineDestroyed + 1);
    }

    @Test
    public void shouldWin_whenNoMoreMines() {
        placeMineUpFromSapper();

        board.useMineDetectorToGivenDirection(Direction.UP);

        assertTrue(board.isWin());
    }

    @Test
    public void shouldGameOver_whenNoMoreCharge() {
        board.sapperMoveTo(Direction.UP);
        placeMineUpFromSapper();
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺**☼\n" +
                "☼ **☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(board));

        board.useMineDetectorToGivenDirection(Direction.DOWN);
//        board.useMineDetectorToGivenDirection(Direction.UP);  // there is bomb
        board.useMineDetectorToGivenDirection(Direction.LEFT);
        board.useMineDetectorToGivenDirection(Direction.RIGHT);
        board.sapperMoveTo(Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼1☺*☼\n" +
                "☼‼**☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(board));

        board.useMineDetectorToGivenDirection(Direction.DOWN);
        board.useMineDetectorToGivenDirection(Direction.UP);
        board.useMineDetectorToGivenDirection(Direction.LEFT);
        board.useMineDetectorToGivenDirection(Direction.RIGHT);
        board.sapperMoveTo(Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼‼‼☺☼\n" +
                "☼‼‼*☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(board));

        board.useMineDetectorToGivenDirection(Direction.DOWN);
        board.useMineDetectorToGivenDirection(Direction.UP);
        board.useMineDetectorToGivenDirection(Direction.LEFT);
        board.useMineDetectorToGivenDirection(Direction.RIGHT);
        assertEquals(
                "☼☼☼☼☼\n" +
                "☼☻‼‼☼\n" +
                "☼‼‼☺☼\n" +
                "☼‼‼‼☼\n" +
                "☼☼☼☼☼\n", getBoardAsString(board));

        assertFalse(sapper.isDead());
        assertTrue(board.isGameOver());
    }

    private String getBoardAsString(Field board) {
        return (String) new PrinterFactoryImpl<Elements, Player>()
                .getPrinter(board.reader(),
                        new Player(listener)).print();
    }

}
