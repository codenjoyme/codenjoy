package com.globallogic.training.oleksii.morozov.sapperthehero.game;

public enum Direction {

    UP(new Cell(0, -1)),
    DOWN(new Cell(0, 1)),
    LEFT(new Cell(-1, 0)),
    RIGHT(new Cell(1, 0));

    private Cell cell;

    Direction(Cell cell) {
        this.cell = cell;
    }

    public Cell getDeltaPosition() {
        return cell;
    }

}
