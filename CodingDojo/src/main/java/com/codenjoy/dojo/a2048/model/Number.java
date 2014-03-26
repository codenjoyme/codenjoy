package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

/**
 * Created by Sanja on 26.03.14.
 */
public class Number extends PointImpl {

    private int number;

    public Number(int number, Point pt) {
        super(pt);
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
}
