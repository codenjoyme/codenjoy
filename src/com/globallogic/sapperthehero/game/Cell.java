package com.globallogic.sapperthehero.game;


public class Cell {
    private int xPosition;
    private int yPosition;

    public Cell(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public Cell(Cell cell) {
        this.xPosition = cell.getXPosition();
        this.yPosition = cell.getYPosition();
    }


    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof Cell)) {
            return false;
        }

        Cell cell = (Cell) obj;
        return xPosition == cell.getXPosition() && yPosition == cell.getYPosition();
    }

    @Override
    public Cell clone() {
        return new Cell(xPosition, yPosition);
    }


    public void changeMyCoordinate(Cell deltaCell) {
        xPosition += deltaCell.getXPosition();
        yPosition += deltaCell.getYPosition();
    }

    @Override
    public String toString() {
        return String.format("[%s : %s]", xPosition, yPosition);
    }
}
