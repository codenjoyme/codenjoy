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

import com.codenjoy.dojo.bomberman.model.perks.PerkOnBoard;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.services.StateUtils.filterOne;

public class MeatChopperHunter extends MeatChopper {

    private Hero prey;
    private DeikstraFindWay way;
    private PerkOnBoard perk;
    private boolean alive = true;

    public MeatChopperHunter(PerkOnBoard perk, Hero prey) {
        super(perk.copy(), prey.field(), prey.getDice());
        this.prey = prey;
        this.perk = perk;
        this.way = new DeikstraFindWay();
    }

    public DeikstraFindWay.Possible possible(Field field) {
        List<Wall> walls = field.walls().listEquals(Wall.class);

        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point point) {
                return !walls.contains(point);
            }
        };
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
            die();
            return;
        }

        List<Direction> directions = getDirections(this, Arrays.asList(prey));
        if (directions.isEmpty()) {
            // если не видим куда идти - выпиливаемся
            die();
        } else {
            // если видим - идем
            direction = directions.get(0);
            Point from = this.copy();
            this.move(direction.change(from));

            // попутно сносим стенки на пути прожженные (если есть)
            field.walls().destroyExact(new DestroyWall(from));
        }
    }

    public void die() {
        alive = false;
        field.remove(this);
        // ларчик просто открывался, перки надо не убивать
        // а собирать, иначе они за тобой будут гнаться
        perk.move(this);
        field.perks().add(perk);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (alive) {
            DestroyWall wall = filterOne(alsoAtPoint, DestroyWall.class);
            if (wall != null) {
                return DESTROYED_WALL;
            }

            Blast blast = filterOne(alsoAtPoint, Blast.class);
            if (blast != null) {
                return MEAT_CHOPPER;
            }

            return DEAD_MEAT_CHOPPER;
        }

        // если поднизом бомба, видеть ее важнее, чем трупик митчопера TODO test me
        Bomb bomb = filterOne(alsoAtPoint, Bomb.class);
        if (bomb != null) {
            return bomb.state(player, alsoAtPoint);
        }

        return Elements.MEAT_CHOPPER;
    }
}
