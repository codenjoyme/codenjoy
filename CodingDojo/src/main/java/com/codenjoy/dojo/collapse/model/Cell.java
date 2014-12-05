package com.codenjoy.dojo.collapse.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Cell extends PointImpl implements State<Elements, Player> {
    private int number;

    public Cell(Point point, int number) {
        super(point);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("pt%s=%s%s", super.toString(), number);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.valueOf(number);
    }

    public void exchange(Cell cell) {
        int temp = cell.number;
        cell.number = this.number;
        this.number = temp;
    }
}
