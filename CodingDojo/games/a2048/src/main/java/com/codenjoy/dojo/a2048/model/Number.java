package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Number extends PointImpl implements State<Elements, Player> {

    private int number;

    public Number(int number, Point pt) {
        super(pt);
        this.number = number;
    }

    public Number(int number, int x, int y) {
        super(x, y);
        this.number = number;
    }

    public int get() {
        return number;
    }

    public int next() {
        return number*2;
    }

    @Override
    public String toString() {
        return String.format("{%s=%s}", super.toString(), number);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (number == Numbers.BREAK) {
            return Elements._x;
        }

        if (number == Numbers.NONE) {
            return Elements.NONE;
        }
        return Elements.valueOf(number);
    }
}
