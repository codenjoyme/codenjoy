package com.codenjoy.dojo.battlecity.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.battlecity.model.*;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class LevelImpl implements Level {

    private final LengthToXY xy;
    private final Dice dice;
    private final String map;

    public LevelImpl(String map, Dice dice) {
        this.map = map;
        this.dice = dice;
        xy = new LengthToXY(size());
    }

    @Override
    public int size() {
        return (int) Math.sqrt(map.length());
    }

    private <T> List<T> getObjects(Elements element, Function<Point, T> objects) {
        List<T> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch) {
                Point pt = xy.getXY(index);
                result.add(objects.apply(pt));
            }
        }
        return result;
    }

    @Override
    public List<Wall> getWalls() {
        return getObjects(Elements.WALL,
                pt -> new Wall(pt));
    }

    @Override
    public List<River> getRivers() {
        return getObjects(Elements.RIVER,
                pt -> new River(pt));
    }

    @Override
    public List<Ice> getIce() {
        return getObjects(Elements.ICE,
                pt -> new Ice(pt));
    }

    @Override
    public List<Tree> getTrees() {
        return getObjects(Elements.TREE,
                pt -> new Tree(pt));
    }

    @Override
    public List<Tank> getAiTanks() {
        return getObjects(Elements.AI_TANK_DOWN,
                pt -> new AITank(pt, dice, Direction.DOWN));
    }

    @Override
    public List<Border> getBorders() {
        return getObjects(Elements.BATTLE_WALL,
                pt -> new Border(pt));
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return LevelImpl.this.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(LevelImpl.this.getBorders());
                    addAll(LevelImpl.this.getWalls());
                    addAll(LevelImpl.this.getAiTanks());
                    addAll(LevelImpl.this.getIce());
                    addAll(LevelImpl.this.getRivers());
                    addAll(LevelImpl.this.getTrees());
                }};
            }
        };
    }
}
