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
    private int minesCount;
    private int boardSize;
    private Sapper sapper;
    private List<Mine> mines;

    public Board(int boardSize, int minesCount) {
        this.minesCount = minesCount;
        this.boardSize = boardSize;
        this.freeCells = initializeBoardCells(boardSize);
        this.boardCells = initializeBoardCells(boardSize);
        this.sapper = initializeSapper();
        this.mines = generateRandomPlacedMines(boardSize);
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
        return minesCount;
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

//    private void addFreeCell(Cell cell) {
//        freeCells.add(cell);
//    }

    public void sapperMoveTo(Direction direction) {
        if (boardCells.contains(getSapperPossiblePosition(direction)))
            sapper.displaceMeByDelta(direction.getDeltaPosition());
    }

    public Cell getSapperPossiblePosition(Direction direction) {
        Cell temporarySupperPosition = sapper.clone();
        temporarySupperPosition.changeMyCoordinate(direction.getDeltaPosition());
        return temporarySupperPosition;
    }
}
