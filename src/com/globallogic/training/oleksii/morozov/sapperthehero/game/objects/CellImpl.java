package com.globallogic.training.oleksii.morozov.sapperthehero.game.objects;


public class CellImpl implements Cell {
    private int x;
    private int y;

    public CellImpl(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public CellImpl(Cell cell) {
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
	public void changeMyCoordinate(Cell cell) {
        x += cell.getX();
        y += cell.getY();
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
        return String.format("[%s : %s]", x, y);
    }
}
