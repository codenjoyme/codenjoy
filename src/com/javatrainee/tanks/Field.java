package com.javatrainee.tanks;

public class Field {
    private int size;
    private Construction construction;
    private Tank tank;

    public Field(int size) {
        this.size = size;
        this.tank = new Tank(1, 1);
    }

    public int getSize() {
        return size;
    }

    public Construction getConstruction() {
        return construction;
    }

    public void setConstruction(Construction construction) {
        this.construction = construction;
    }

    public Tank getTank() {
        return tank;
    }
}
