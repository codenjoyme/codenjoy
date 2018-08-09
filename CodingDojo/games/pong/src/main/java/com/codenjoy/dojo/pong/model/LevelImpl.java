package com.codenjoy.dojo.pong.model;

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

import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> getHero() {
        return getPointsOf(Elements.HERO).stream()
                .map(Hero::new)
                .collect(toList());
    }

    @Override
    public List<Wall> getWalls() {
        return new LinkedList<Wall>(){{
            addAll(getPointsOf(Elements.VERTICAL_WALL).stream()
                    .map(pt -> new Wall(pt, BarrierOrientation.VERTICAL))
                    .collect(toList()));

            addAll(getPointsOf(Elements.HORIZONTAL_WALL).stream()
                    .map(pt -> new Wall(pt, BarrierOrientation.HORIZONTAL))
                    .collect(toList()));

        }};
    }

    @Override
    public Ball getBall() {
        return new Ball(getPointsOf(Elements.BALL).get(0));
    }

    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }


}
