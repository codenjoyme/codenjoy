package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;

public abstract class MovingObject extends Point {
    protected Direction direction;
    protected int speed;

    public MovingObject(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

}
