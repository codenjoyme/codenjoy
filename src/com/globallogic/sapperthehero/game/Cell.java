package com.globallogic.sapperthehero.game;


public class Cell {
    private int xPosition;
    private int yPosition;

    public Cell(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
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
}
