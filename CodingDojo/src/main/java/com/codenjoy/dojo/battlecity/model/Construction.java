package com.codenjoy.dojo.battlecity.model;

public class Construction {
    private int coordinateX = 0;
    private int coordinateY = 0;

    public Construction(int X, int Y) {
        this.coordinateX = X;
        this.coordinateY = Y;
    }

    public int[] getCoordinates() {
        return new int[] {this.coordinateX, this.coordinateY};
    }
}
