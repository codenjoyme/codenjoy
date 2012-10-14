package com.globallogic.sapperthehero.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class Board {
    private List<Cell> freeCells;
    private List<Cell> boardCells;
    private int boardSize;
    private Sapper sapper;
    private List<Mine> mines;
    private int turnCount = 0;

    public Board(int boardSize, int minesCount) {
        this.boardSize = boardSize;
        this.freeCells = initializeBoardCells(boardSize);
        this.boardCells = initializeBoardCells(boardSize);
        this.sapper = initializeSapper();
        this.mines = generateRandomPlacedMines(minesCount);
    }


    private Sapper initializeSapper() {
        Sapper sapperTemporary = new Sapper(1, 1);
        removeFreeCell(sapperTemporary);
        return sapperTemporary;
    }

    private List<Cell> initializeBoardCells(int boardSize) {
        List<Cell> cells = new ArrayList<Cell>();
        for (int xPosition = 0; xPosition < boardSize; xPosition++) {
            for (int yPosition = 0; yPosition < boardSize; yPosition++) {
                cells.add(new Cell(xPosition, yPosition));
            }
        }
        return cells;
    }

    public List<Cell> getFreeCells() {
        return freeCells;
    }

    public List<Cell> getBoardCells() {
        return boardCells;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public Sapper getSapper() {
        return sapper;
    }

    public List<Mine> getMines() {
        return mines;
    }

    public int getMinesCount() {
        return mines.size();
    }

    private List<Mine> generateRandomPlacedMines(int minesCount) {
        List<Mine> minesTemporary = new ArrayList<Mine>();
        for (int index = 0; index < minesCount; index++) {
            minesTemporary.add(new Mine(getRandomFreeCellOnBoard()));
        }
        return minesTemporary;
    }

    private Cell getRandomFreeCellOnBoard() {
        if (!freeCells.isEmpty()) {
            int indexRandomFreePositionAtTile = new Random().nextInt(freeCells.size());
            Cell randomFreeCellOnBoard = freeCells.get(indexRandomFreePositionAtTile);
            removeFreeCell(randomFreeCellOnBoard);
            return randomFreeCellOnBoard;
        }
        return null;
    }

    private void removeFreeCell(Cell cell) {
        freeCells.remove(cell);
    }

    private void addFreeCell(Cell cell) {
        freeCells.add(new Cell(cell));
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
        addFreeCell(sapper);
        sapper.displaceMeByDelta(direction.getDeltaPosition());
        removeFreeCell(sapper);
    }


    private boolean isSapperCanMoveToDirection(Direction direction) {
        Cell sapperPossiblePosition = getSapperPossiblePosition(direction);
        return boardCells.contains(sapperPossiblePosition);
    }

    private void nextTurn() {
        turnCount++;
    }

    public boolean isSapperOnMine() {
        return mines.contains(sapper);
    }

    public boolean isGameOver() {
        return sapper.isDead();
    }

    public Cell getSapperPossiblePosition(Direction direction) {
        Cell temporarySupperPosition = sapper.clone();
        temporarySupperPosition.changeMyCoordinate(direction.getDeltaPosition());
        return temporarySupperPosition;
    }

    public Mine createMineOnPositionIfPossible(Cell cell) {
        Mine mine = new Mine(cell);
        removeFreeCell(mine);
        mines.add(mine);
        return mine;
    }


    public int getTurn() {
        return turnCount;
    }

    public int minesNearSapper() {
        int minesNearSapper = 0;
        for (Direction direction : Direction.values()) {
            Cell sapperPossiblePosition = getSapperPossiblePosition(direction);
            if (boardCells.contains(sapperPossiblePosition) || mines.contains(sapperPossiblePosition)) {
                minesNearSapper++;
            }
        }
        return minesNearSapper;
    }
}
