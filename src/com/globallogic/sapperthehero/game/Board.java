package com.globallogic.sapperthehero.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 11:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class Board {
    private List<Cell> freeCells;
    private int boardSize;

    public Board(int boardSize, int minesCount) {
        this.boardSize = boardSize;
        initializeFreeCells();

    }

    private void initializeFreeCells() {
        freeCells = new ArrayList<Cell>();
        for (int xPosition = 0; xPosition < boardSize; xPosition++) {
            for (int yPosition = 0; yPosition < boardSize; yPosition++) {
                freeCells.add(new Cell(xPosition, yPosition));
            }
        }
    }

    public List<Cell> getFreeCells() {


        return freeCells;
    }
}
