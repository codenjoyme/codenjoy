package com.javatrainee.tanks;

public class Tank {
    private int size;
    private int coordinateX = 0;
    private int coordinateY = 0;
    private Direction direction;

    public Tank(int coordinateX, int coordinateY) {
        this.size = 1;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
        direction = Direction.UP;
    }

    public int getSize() {
        return size;
    }

    public int[] getCoordinates() {
        return new int[] { coordinateX, coordinateY};
    }

    public void moveUp() {
        coordinateY--;
        direction = Direction.UP;
    }

    public void moveDown() {
        coordinateY++;
        direction = Direction.DOWN;
    }

    public void moveRight() {
        coordinateX++;
        direction = Direction.RIGHT;
    }

    public void moveLeft() {
        coordinateX--;
        direction =Direction.LEFT;
    }

    public Direction getDirection() {
        return direction;
    }
}
