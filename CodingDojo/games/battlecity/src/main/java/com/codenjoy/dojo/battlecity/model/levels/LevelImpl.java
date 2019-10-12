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
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class LevelImpl implements Level {

    private LengthToXY xy;
    private final Dice dice;
    private String map;
    private UUID mapUuid;

    public LevelImpl(String map, Dice dice) {
        this.map = map;
        mapUuid = UUID.randomUUID();
        this.dice = dice;
        xy = new LengthToXY(getSize());
    }

    @Override
    public List<Construction> getConstructions() {
        List<Construction> result = new LinkedList<Construction>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.CONSTRUCTION.ch) {
                result.add(new Construction(xy.getXY(index)));
            }
        }
        return result;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return LevelImpl.this.getSize();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>() {{
                    addAll(LevelImpl.this.getBorders());
                    addAll(LevelImpl.this.getConstructions());
                    addAll(LevelImpl.this.getTanks());
                }};
            }
        };
    }

    @Override
    public List<Tank> getTanks() {
        List<Tank> result = new LinkedList<Tank>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.AI_TANK_DOWN.ch) {
                Point pt = xy.getXY(index);
                result.add(new AITank(pt.getX(), pt.getY(), dice, Direction.DOWN));
            }
        }
        return result;
    }

    @Override
    public List<Border> getBorders() {
        List<Border> result = new LinkedList<Border>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.BATTLE_WALL.ch) {
                result.add(new Border(xy.getXY(index)));
            }
        }
        return result;
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public void refresh(String map) {
        this.map = map;
        mapUuid = UUID.randomUUID();
        this.xy = new LengthToXY(getSize());
    }

    @Override
    public UUID getMapUUID() {
        return mapUuid;
    }

}
