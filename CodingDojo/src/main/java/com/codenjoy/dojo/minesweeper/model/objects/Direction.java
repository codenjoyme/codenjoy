package com.codenjoy.dojo.minesweeper.model.objects;

public enum Direction {

    UP(new CellImpl(0, 1)),
    DOWN(new CellImpl(0, -1)),
    LEFT(new CellImpl(-1, 0)),
    RIGHT(new CellImpl(1, 0)),
    UP_LEFT(new CellImpl(-1, 1)),
    UP_RIGHT(new CellImpl(1, 1)),
    DOWN_LEFT(new CellImpl(-1, -1)),
    DOWN_RIGHT(new CellImpl(1, -1));

    private Cell cell;

    Direction(Cell cell) {
        this.cell = cell;
    }

    public Cell getDeltaPosition() {
        return cell;
    }

}
