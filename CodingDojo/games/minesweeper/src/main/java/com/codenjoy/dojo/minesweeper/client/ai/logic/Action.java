package com.codenjoy.dojo.minesweeper.client.ai.logic;

import com.codenjoy.dojo.services.PointImpl;

public class Action extends PointImpl {

    private Cell cell;
    private boolean mark;

    public Action(Cell cell, boolean mark) {
        super(cell);
        this.cell = cell;
        this.mark = mark;
    }

    public boolean willMark() {
        return mark;
    }

    public Cell cell() {
        return cell;
    }

    @Override
    public String toString() {
        return "Action{" +
                "mark=" + mark +
                ", cell=" + cell +
                '}';
    }
}
