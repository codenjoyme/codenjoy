package com.codenjoy.dojo.battlecity.model;

public abstract class MovingObject {
    protected int coordinateX;
    protected int coordinateY;
    protected Direction direction;
    protected int movingSpeed;

    public MovingObject(int coordinateX, int coordinateY, Direction direction) {
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        this.direction = direction;
    }

    public int[] getCoordinates() {
        return new int[] {coordinateX, coordinateY};
    }

    public abstract void moveUp();
    public abstract void moveDown();
    public abstract void moveRight();
    public abstract void moveLeft();

    public Direction getDirection() {
        return direction;
    }
}
