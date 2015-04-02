package com.codenjoy.dojo.sudoku.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Cell extends PointImpl implements State<Elements, Player> {
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

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (visible) {
            return Elements.valueOf(number);
        } else {
            if (alsoAtPoint[1] != null) {
                return Elements.valueOf(((Cell) alsoAtPoint[1]).getNumber());
            } else {
                return Elements.NONE;
            }
        }
    }
}
