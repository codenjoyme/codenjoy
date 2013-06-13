package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;

public abstract class MovingObject extends Point {
    protected Direction direction;
    protected int speed;
    protected boolean moving;

    public MovingObject(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
        moving = false;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move() {
        for (int i = 0; i < speed; i++) {
            if (!moving) {
                return;
            }

            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y); // TODO fixme
            moving(newX, newY);
        }
    }

    protected abstract void moving(int newX, int newY);

}
