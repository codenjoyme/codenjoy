package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public class Cell extends PointImpl {
    private int number;
    private boolean visible;

    public Cell(Point point, int number, boolean visible) {
        super(point);
        this.number = number;
        this.visible = visible;
    }

    public boolean isHidden() {
        return !visible;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("pt%s=%s%s", super.toString(), (isHidden())?"-":"+", number);
    }
}
