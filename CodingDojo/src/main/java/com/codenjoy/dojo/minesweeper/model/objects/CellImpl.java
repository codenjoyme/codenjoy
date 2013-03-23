package com.codenjoy.dojo.minesweeper.model.objects;


public class CellImpl implements Cell {
    private int x;
    private int y;

    public CellImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    CellImpl(Cell cell) {
        this.x = cell.getX();
        this.y = cell.getY();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void changeTo(Cell cell) {
        x += cell.getX();
        y += cell.getY();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof CellImpl)) {
            return false;
        }
        Cell cell = (Cell) obj;
        return x == cell.getX() && y == cell.getY();
    }

    @Override
    public Cell clone() {
        return new CellImpl(x, y);
    }

    @Override
    public String toString() {
        return String.format("%s %s", x, y);
    }
}
