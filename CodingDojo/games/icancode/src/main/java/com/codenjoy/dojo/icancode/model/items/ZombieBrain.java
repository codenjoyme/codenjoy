package com.codenjoy.dojo.icancode.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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

import com.codenjoy.dojo.icancode.model.Field;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ZombieBrain {

    public Direction whereToGo(Point zombie, Field field) {
        List<Point> heroes = field.getLevel().getItems(HeroItem.class).stream()
                .map(item -> item.getCell()).collect(toList());
        if (heroes.isEmpty()) {
            return null;
        }
        List<Direction> shortestWay = getShortestWay(field, zombie, heroes);
        if (shortestWay.isEmpty()) {
            return null;
        }
        Direction result = shortestWay.get(0);
        return result;
    }

    List<Direction> getShortestWay(Field field, Point from, List<Point> to) {
        DeikstraFindWay.Possible map = possible(field);
        DeikstraFindWay findWay = new DeikstraFindWay();
        List<Direction> result = findWay.getShortestWay(field.size(), from, to, map);
        return result;
    }

    private DeikstraFindWay.Possible possible(Field field) {
        return new DeikstraFindWay.Possible() { // TODO test me
            @Override
            public boolean possible(Point point) {
                int x = point.getX();
                int y = point.getY();

                if (field.isBarrier(x, y)) return false;
                if (field.isAt(x, y, Hole.class)) return false;
                if (field.isAt(x, y, Box.class)) return false;

                return true;
            }
        };
    }

}
