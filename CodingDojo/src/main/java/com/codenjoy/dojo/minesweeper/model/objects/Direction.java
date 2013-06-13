package com.codenjoy.dojo.minesweeper.model.objects;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

public enum Direction {

    UP(new PointImpl(0, 1)),
    DOWN(new PointImpl(0, -1)),
    LEFT(new PointImpl(-1, 0)),
    RIGHT(new PointImpl(1, 0)),
    UP_LEFT(new PointImpl(-1, 1)),
    UP_RIGHT(new PointImpl(1, 1)),
    DOWN_LEFT(new PointImpl(-1, -1)),
    DOWN_RIGHT(new PointImpl(1, -1));

    private PointImpl delta;

    Direction(PointImpl delta) {
        this.delta = delta;
    }

    public PointImpl change(PointImpl point) {
        point.change(delta);
        return point;
    }

}
