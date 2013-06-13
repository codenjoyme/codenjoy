package com.codenjoy.dojo.battlecity.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Field {
    private int size;
    private List<Construction> constructions;
    private Tank tank = null;

    public Field(int size) {
        this.size = size;
        constructions = new LinkedList<Construction>();
    }

    public int getSize() {
        return size;
    }

    public List<Construction> getConstructions() {
        return constructions;
    }

    public void addConstructions(Construction... constructions) {
        addConstructions(Arrays.asList(constructions));
    }

    public void addConstructions(List<Construction> constructions) {
        this.constructions.addAll(constructions);
        for (final Construction construction : constructions) {
            construction.setOnDestroy(new OnDestroy() {
                @Override
                public void destroy() {
                    Field.this.constructions.remove(construction);
                }
            });
        }
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
        if (constructions.contains(bullet)) {
            int index = constructions.indexOf(bullet);
            constructions.get(index).destroyFrom(bullet.getDirection());
            bullet.onDestroy();
        }
    }

    public boolean isBreakAt(Point point) {
        for (Construction construction : constructions) {
            if (construction.equals(point)) {
                return true;
            }
        }
        return false;
    }
}
