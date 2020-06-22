package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.StateUtils.filterOne;

public class MeatChopperHunter extends MeatChopper {

    private Hero prey;
    private DeikstraFindWay way;
    private boolean alive = true;

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
            // митчопер умрет от праведного (ничейного) огня! мы увидим его трупик 1 тик
            alive = false;
            field.remove(this);
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

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (alive) {
            return super.state(player, alsoAtPoint);
        }

        // если поднизом бомба, видеть ее важнее, чем трупик митчопера TODO test me
        Bomb bomb = filterOne(alsoAtPoint, Bomb.class);
        if (bomb != null) {
            return bomb.state(player, alsoAtPoint);
        }

        return Elements.DEAD_MEAT_CHOPPER;
    }
}
