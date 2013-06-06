package com.codenjoy.dojo.battlecity.model;

public class Field {
    private int size;
    private Construction construction = null;
    private Tank tank = null;

    public Field(int size) {
        this.size = size;
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

    public void setTank(Tank tank) {
        if (tank != null) {
            tank.setField(this);
        }
        this.tank = tank;
    }
}
