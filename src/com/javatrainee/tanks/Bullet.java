package com.javatrainee.tanks;

public class Bullet {
    private Direction direction;
    private int coordinateX;
    private int coordinateY;

    public Bullet(Direction tankDirection, int[] tankCoordinates) {
        this.direction = tankDirection;
        this.coordinateX = tankCoordinates[0];
        this.coordinateY = tankCoordinates[1];
    }

    public Direction getDirection() {
        return direction;
    }
}
