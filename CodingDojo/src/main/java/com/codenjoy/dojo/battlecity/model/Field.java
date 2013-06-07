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
        this.construction.setOnDestroy(new OnDestroy() {
            @Override
            public void destroy() {
                Field.this.construction = null;
            }
        });
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

    public void affect(Bullet bullet) {
        if (construction == null) {
            return;
        }

        if (construction.equals(bullet)) {
            construction.destroyFrom(bullet.getDirection());
            bullet.onDestroy();
        }
    }
}
