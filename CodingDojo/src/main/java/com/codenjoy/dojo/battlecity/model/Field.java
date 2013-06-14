package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.PointImpl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Field {
    private int size;
    private List<Construction> constructions;
    private List<Tank> tanks;

    public Field(int size) {
        this.size = size;
        tanks = new LinkedList<Tank>();
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
        for (Construction construction : constructions) {
            construction.setOnDestroy(new OnDestroy() {
                @Override
                public void destroy(Object construction) {
                    Field.this.constructions.remove(construction);
                }
            });
        }
    }

    public void addTank(Tank tank) {
        if (tank != null) {
            tank.setField(this);
        }
        tanks.add(tank);
    }

    public void affect(Bullet bullet) {
        if (constructions.contains(bullet)) {
            int index = constructions.indexOf(bullet);
            constructions.get(index).destroyFrom(bullet.getDirection());
            bullet.onDestroy();
        } else if (tanks.contains(bullet)) {
            int index = tanks.indexOf(bullet);
            tanks.get(index).kill(bullet);
            bullet.onDestroy();
        }
    }

    public boolean isBarrier(int x, int y) {
        for (Construction construction : constructions) {
            if (construction.equals(new PointImpl(x, y))) {
                return true;
            }
        }
        for (Tank tank : tanks) {
            if (tank.equals(new PointImpl(x, y))) {
                return true;
            }
        }
        return isBorder(x, y);
    }

    public boolean isBorder(int x, int y) {
        return x <= 0 || y <= 0 || y >= size - 1 || x >= size - 1;
    }

    public Iterable<Bullet> getBullets() {
        List<Bullet> result = new LinkedList<Bullet>();
        for (Tank tank : tanks) {
            for (Bullet bullet : tank.getBullets()) {
                result.add(bullet);
            }
        }
        return result;
    }

    public Iterable<Tank> getTanks() {
        return new LinkedList<Tank>(tanks);
    }

    public void remove(Tank tank) {
        tanks.remove(tank);
    }
}
