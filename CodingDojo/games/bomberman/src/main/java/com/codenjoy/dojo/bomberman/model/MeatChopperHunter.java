package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.Arrays;
import java.util.List;

public class MeatChopperHunter extends MeatChopper {

    private Hero prey;
    private DeikstraFindWay way;

    public MeatChopperHunter(Point pt, Hero prey) {
        super(pt, prey.field(), prey.getDice());
        this.prey = prey;
        this.way = new DeikstraFindWay();
    }

    public DeikstraFindWay.Possible possible(Field field) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                if (isWall(from)) return false;
                Point to = where.change(from);
                if (to.isOutOf(field.size())) return false;
                if (isWall(to)) return false;
                return true;
            }

            @Override
            public boolean possible(Point atWay) {
                return true;
            }
        };
    }

    private boolean isWall(Point pt) {
        Wall wall = field.walls().get(pt);
        return wall != null
                && wall.getClass().equals(Wall.class);
    }

    public List<Direction> getDirections(Point from, List<Point> to) {
        DeikstraFindWay.Possible map = possible(field);
        return way.getShortestWay(field.size(), from, to, map);
    }

    @Override
    public void tick() {
        // если нарушитель уже того, выпиливаемся тоже
        if (!prey.isActiveAndAlive()) {
            field.walls().destroy(this);
            return;
        }

        List<Direction> directions = getDirections(this, Arrays.asList(prey));
        if (directions.isEmpty()) {
            // если не видим куда идти - выпиливаемся
            field.walls().destroy(this);
        } else {
            // если видим - идем
            direction = directions.get(0);
            Point from = this.copy();
            this.move(direction.change(from));

            // попутно сносим стенки на пути прожженные
            field.walls().destroy(from);
        }
    }
}
