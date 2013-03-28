package com.javatrainee.tanks;

public class Tank {
    private int size;
    private int coordinateX = 0;
    private int coordinateY = 0;

    public Tank(int coordinateX, int coordinateY) {
        this.size = 1;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public int getSize() {
        return size;
    }

    public int[] getCoordinates() {
        return new int[] { coordinateX, coordinateY};
    }

    public void moveUp() {
        coordinateX--;
    }
}
