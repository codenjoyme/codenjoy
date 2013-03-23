package com.globallogic.training.oleksii.morozov.sapperthehero.game.objects;

public enum Direction {

    UP(new CellImpl(0, -1)),
    DOWN(new CellImpl(0, 1)),
    LEFT(new CellImpl(-1, 0)),
    RIGHT(new CellImpl(1, 0));

    private Cell cell;

    Direction(Cell cell) {
        this.cell = cell;
    }

    public Cell getDeltaPosition() {
        return cell;
    }

}
