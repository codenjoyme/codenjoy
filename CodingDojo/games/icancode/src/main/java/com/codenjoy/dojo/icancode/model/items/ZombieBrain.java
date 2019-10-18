package com.codenjoy.dojo.icancode.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 EPAM
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

import com.codenjoy.dojo.icancode.model.interfaces.IField;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ZombieBrain {

    public Direction whereToGo(Point zombie, IField field) {
        List<Point> heroes = field.getLevel().getItems(HeroItem.class).stream()
                .map(item -> item.getCell()).collect(toList());
        if (heroes.isEmpty()) {
            return null;
        }
        List<Direction> shortestWay = getShortestWay(field, zombie, heroes);
        if (shortestWay.isEmpty()) {
            return null;
        }
        Direction nextStep = shortestWay.get(0);
        return nextStep;
    }

    List<Direction> getShortestWay(IField field, Point from, List<Point> to) {
        DeikstraFindWay.Possible map = possible(field);
        DeikstraFindWay findWay = new DeikstraFindWay();
        List<Direction> shortestWay = findWay.getShortestWay(field.size(), from, to, map);
        return shortestWay;
    }

    private DeikstraFindWay.Possible possible(IField field) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                int x = from.getX();
                int y = from.getY();

                if (isNotFree(x, y, field)) return false;

                Point to = where.change(from);
                int nx = to.getX();
                int ny = to.getY();

                if (to.isOutOf(field.size())) return false;

                if (isNotFree(nx, ny, field)) return false;

                return true;
            }

            @Override
            public boolean possible(Point atWay) {
                return true;
            }
        };
    }

    boolean isNotFree(int x, int y, IField field) {
        return field.isBarrier(x, y)
                || field.isAt(x, y, Hole.class)
                || field.isAt(x, y, Box.class);
    }
}
