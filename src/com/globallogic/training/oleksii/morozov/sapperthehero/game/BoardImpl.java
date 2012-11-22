package com.globallogic.training.oleksii.morozov.sapperthehero.game;

import com.globallogic.training.oleksii.morozov.sapperthehero.game.minegenerator.MinesGenerator;
import com.globallogic.training.oleksii.morozov.sapperthehero.game.objects.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BoardImpl implements Board {

    private List<CellImpl> cells;
    private int size;
    private Sapper sapper;
    private List<Mine> mines;
    private int turnCount = 0;
    private MinesGenerator minesGenerator;

    public BoardImpl(int size, int minesCount, int detectorCharge,
                     MinesGenerator minesGenerator) {
        if (size < 2) {
            throw new IllegalArgumentException();
        }
        if (minesCount > size * size - 1) {
            throw new IllegalArgumentException();
        }
        if (detectorCharge < minesCount) {
            throw new IllegalArgumentException();
        }
        this.minesGenerator = minesGenerator;
        this.size = size;
        this.cells = initializeBoardCells(size);
        this.sapper = initializeSapper();
        this.sapper.iWantToHaveMineDetectorWithChargeNumber(detectorCharge);
        this.mines = this.minesGenerator.get(minesCount, this);
    }

    private Sapper initializeSapper() {
        return new Sapper(1, 1);
    }

    private List<CellImpl> initializeBoardCells(int size) {
        List<CellImpl> result = new ArrayList<CellImpl>();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                result.add(new CellImpl(x, y));
            }
        }
        return result;
    }

    @Override
    public List<CellImpl> getFreeCells() {
        List<CellImpl> result = new LinkedList<CellImpl>();
        for (CellImpl cell : getCells()) {
            boolean isSapper = cell.equals(getSapper());
            boolean isMine = isMine(cell);
            if (!isSapper && !isMine) {
                result.add(cell);
            }
        }
        return result;
    }

    private boolean isMine(Cell cell) {
        boolean isMine = false;
        if (getMines() != null) {
            for (Mine mine : getMines()) {
                isMine |= cell.equals(mine);
            }
        }
        return isMine;
    }

    @Override
    public List<CellImpl> getCells() {
        return cells;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public Sapper getSapper() {
        return sapper;
    }

    @Override
    public List<Mine> getMines() {
        return mines;
    }

    @Override
    public int getMinesCount() {
        return getMines().size();
    }

    @Override
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

    @Override
    public boolean isSapperOnMine() {
        return getMines().contains(sapper);
    }

    @Override
    public boolean isGameOver() {
        return sapper.isDead() || isEmptyDetectorButPresentMines();
    }

    @Override
    public Cell getCellPossiblePosition(Direction direction) {
        Cell result = sapper.clone();
        result.changeTo(direction.getDeltaPosition());
        return result;
    }

    @Override
    public Mine createMineOnPositionIfPossible(Cell cell) {
        Mine result = new Mine(cell);
        getMines().add(result);
        return result;
    }

    @Override
    public int getTurn() {
        return turnCount;
    }

    @Override
    public int getMinesNearSapper() {
        int result = 0;
        for (Direction direction : Direction.values()) {
            Cell sapperPossiblePosition = getCellPossiblePosition(direction);
            if (cells.contains(sapperPossiblePosition)
                    && getMines().contains(sapperPossiblePosition)) {
                result++;
            }
        }
        return result;
    }

    @Override
    public void useMineDetectorToGivenDirection(Direction direction) {
        Cell result = getCellPossiblePosition(direction);
        if (cells.contains(result)) {
            sapper.useMineDetector();
            if (getMines().contains(result)) {
                destroyMine(result);
            }
        }
    }

    @Override
    public boolean isEmptyDetectorButPresentMines() {
        return mines.size() != 0 && sapper.getMineDetectorCharge() == 0;
    }

    @Override
    public boolean isWin() {
        return mines.size() == 0 && !sapper.isDead();
    }

    private void destroyMine(Cell possibleMine) {
        getMines().remove(possibleMine);
    }

}
