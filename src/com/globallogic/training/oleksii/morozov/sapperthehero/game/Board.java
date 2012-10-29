package com.globallogic.training.oleksii.morozov.sapperthehero.game;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.MinesGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private static final String BAD_BOARD_SIZE_MESSAGE = "Введите размеры поля больше 1";
    private static final String BAD_MINES_COUNT = "Количество мин должно быть меньше всех клеток на поле, то есть ";
    private static final String BAD_DETECTOR_CHARGE = "Количество зарядов детектора должно быть больше количества мин на поле";

    private List<Cell> cells;
    private int size;
    private Sapper sapper;
    private List<Mine> mines;
    private int turnCount = 0;
    private MinesGenerator minesGenerator;

    public Board(int size, int minesCount, int detectorCharge, MinesGenerator minesGenerator) {
        if (size < 2) {
            throw new IllegalArgumentException(BAD_BOARD_SIZE_MESSAGE);
        }
        if (minesCount > size * size - 1) {
            throw new IllegalArgumentException(BAD_MINES_COUNT + size * size);
        }
        if (detectorCharge < minesCount) {
            throw new IllegalArgumentException(BAD_DETECTOR_CHARGE);
        }
        this.minesGenerator = minesGenerator;
        this.size = size;
        this.cells = initializeBoardCells(size);
        this.sapper = initializeSapper();
        sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge);
        this.mines = this.minesGenerator.get(minesCount, this);
    }


    private Sapper initializeSapper() {
        return new Sapper(1, 1);
    }

    private List<Cell> initializeBoardCells(int size) {
        List<Cell> result = new ArrayList<Cell>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                result.add(new Cell(x, y));
            }
        }
        return result;
    }

    public List<Cell> getFreeCells() {
        List<Cell> result = new LinkedList<Cell>();
        for (Cell cell : getCells()) {
            boolean isSapper = cell.equals(getSapper());
            boolean isMine = false;
            if (getMines() != null) {
                for (Mine mine : getMines()) {
                    isMine |= cell.equals(mine);
                }
            }
            if (!isSapper && !isMine) {
                result.add(cell);
            }
        }
        return result;
    }

    public List<Cell> getCells() {
        return cells;
    }

    public int getSize() {
        return size;
    }

    public Sapper getSapper() {
        return sapper;
    }

    public List<Mine> getMines() {
        return mines;
    }

    public int getMinesCount() {
        return getMines().size();
    }


    public void sapperMoveTo(Direction direction) {
        if (isSapperCanMoveToDirection(direction)) {
            moveSapperAndFillFreeCell(direction);
            if (isSapperOnMine()) {
                sapper.die(true);

            }
            nextTurn();
        }
    }

    private void moveSapperAndFillFreeCell(Direction direction) {
        sapper.displaceMeByDelta(direction.getDeltaPosition());
    }

    private boolean isSapperCanMoveToDirection(Direction direction) {
        Cell result = getCellPossiblePosition(direction);
        return cells.contains(result);
    }

    private void nextTurn() {
        turnCount++;
    }

    public boolean isSapperOnMine() {
        return getMines().contains(sapper);
    }

    public boolean isGameOver() {
        return sapper.isDead() || isEmptyDetectorButPresentMines();
    }


    public Cell getCellPossiblePosition(Direction direction) {
        Cell result = sapper.clone();
        result.changeMyCoordinate(direction.getDeltaPosition());
        return result;
    }

    public Mine createMineOnPositionIfPossible(Cell cell) {
        Mine result = new Mine(cell);
        getMines().add(result);
        return result;
    }

    public int getTurn() {
        return turnCount;
    }

    public int getMinesNearSapper() {
        int result = 0;
        for (Direction direction : Direction.values()) {
            Cell sapperPossiblePosition = getCellPossiblePosition(direction);
            if (cells.contains(sapperPossiblePosition) && getMines().contains(sapperPossiblePosition)) {
                result++;
            }
        }
        return result;
    }

    public void useMineDetectorToGivenDirection(Direction direction) {
        Cell result = getCellPossiblePosition(direction);
        if (cells.contains(result)) {
            sapper.useMineDetector();
            if (getMines().contains(result)) {
                destroyMine(result);
            }
        }
    }

    public boolean isEmptyDetectorButPresentMines() {
        return mines.size() != 0 && sapper.getMineDetectorCharge() == 0;
    }

    public boolean isWin() {
        return mines.size() == 0 && !sapper.isDead();
    }

    private void destroyMine(Cell possibleMine) {
        getMines().remove(possibleMine);
    }

}
