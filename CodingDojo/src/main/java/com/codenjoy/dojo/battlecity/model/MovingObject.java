package com.codenjoy.dojo.battlecity.model;

public abstract class MovingObject extends Point {
    protected Direction direction;
    protected int speed;
    private Point oldPosition;

    public MovingObject(int x, int y, Direction direction) {
        super(x, y);
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    protected void save() {
        oldPosition = new Point(x, y);
    }

    protected void goBack() {
        // if (oldPosition == null) {
            this.x = oldPosition.x;
            this.y = oldPosition.y;
        // }
//        oldPosition = null; // TODO test me
    }

}
