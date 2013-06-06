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

    public Direction getDirection() {
        return direction;
    }
}
