package com.globallogic.training.oleksii.morozov.sapperthehero.game.objects;


public class Cell {
    private int x;
    private int y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Cell(Cell cell) {
        this.x = cell.getX();
        this.y = cell.getY();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void changeMyCoordinate(Cell cell) {
        x += cell.getX();
        y += cell.getY();
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
        return x == cell.getX() && y == cell.getY();
    }

    @Override
    public Cell clone() {
        return new Cell(x, y);
    }

    @Override
    public String toString() {
        return String.format("[%s : %s]", x, y);
    }
}
