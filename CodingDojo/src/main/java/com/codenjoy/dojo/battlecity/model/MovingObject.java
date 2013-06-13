package com.codenjoy.dojo.battlecity.model;

public abstract class MovingObject extends Point {
    protected Direction direction;
    protected int speed;
    protected Point newPosition;

    public MovingObject(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    protected void tryToveTo(int x, int y) {
        newPosition = new Point(x, y);
    }

    public void move() {
        if (newPosition != null) {
            x = newPosition.x;
            y = newPosition.y;
            newPosition = null;
        }
    }

}
