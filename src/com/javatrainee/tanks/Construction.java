package com.javatrainee.tanks;

public class Construction {
    private String symbolOfConstruction;
    private int coordinateX = 0;
    private int coordinateY = 0;

    public Construction() {
        symbolOfConstruction = Symbols.CONSTRUCTION_SYMBOL;
     }

    public Construction(int X, int Y) {
        symbolOfConstruction = Symbols.CONSTRUCTION_SYMBOL;
        this.coordinateX = X;
        this.coordinateY = Y;
    }

    public String drawConstruction() {
        return symbolOfConstruction;
    }

    public int[]  getCoordinates() {
        return new int[] {this.coordinateX, this.coordinateY};
    }
}
