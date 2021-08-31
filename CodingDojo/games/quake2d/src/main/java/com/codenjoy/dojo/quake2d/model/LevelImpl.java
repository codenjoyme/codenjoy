package com.codenjoy.dojo.quake2d.model;

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

import com.codenjoy.dojo.games.quake2d.Element;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.LinkedList;
import java.util.List;

public class LevelImpl implements Level {

    private LengthToXY xy;
    private String map;

    public LevelImpl(String map) {
        this.map = LevelUtils.clear(map);
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> getHero() {
        List<Hero> result = new LinkedList<>();

        for (Point pt : getPointsOf(Element.HERO)) {
            result.add(new Hero(pt));
        }

        return result;
    }

    @Override
    public List<Robot> getRobots(Field field) {
        List<Robot> result = new LinkedList<>();

        for (Point pt : getPointsOf(Element.ROBOT)) {
            result.add(new Robot(field, pt.getX(), pt.getY()));
        }

        return result;
    }

    public List<Hero> getOtherHero() {
        List<Hero> result = new LinkedList<>();

        for (Point pt : getPointsOf(Element.OTHER_HERO)) {
            result.add(new Hero(pt));
        }

        return result;
    }

    @Override
    public List<Ability> getAbility() {
        List<Ability> result = new LinkedList<>();

        for (Point pt : getPointsOf(Element.SUPER_WEAPON)) {
            result.add(new Ability(pt, Ability.Type.WEAPON));
        }
        for (Point pt : getPointsOf(Element.SUPER_DEFENCE)) {
             result.add(new  Ability(pt, Ability.Type.DEFENCE));
        }
        return result;
    }

    @Override
    public List<Wall> getWalls() {
        List<Wall> result = new LinkedList<>();

        for (Point pt : getPointsOf(Element.WALL)) {
            result.add(new Wall(pt));
        }

        return result;
    }

    private List<Point> getPointsOf(Element element) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }

}
