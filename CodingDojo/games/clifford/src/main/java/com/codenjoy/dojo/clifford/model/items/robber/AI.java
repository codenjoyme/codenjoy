package com.codenjoy.dojo.clifford.model.items.robber;

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


import com.codenjoy.dojo.clifford.model.Field;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.Direction.DOWN;

public class AI implements RobberAI {

    public static boolean POSSIBLE_IS_CONSTANT = true;

    private DeikstraFindWay way = new DeikstraFindWay(POSSIBLE_IS_CONSTANT);

    @Override
    public Direction getDirection(Field field, Point from, List<Point> to) {
        if (to == null) return null;

        Direction direction = null;
        List<Direction> path = getPath(field, from, to);
        if (!path.isEmpty()) {
            direction = path.get(0);
        }

        return direction;
    }

    private DeikstraFindWay.Possible possible(Field field) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                if (where == Direction.UP
                        && !field.isLadder(from)) return false;

                Point under = DOWN.change(from);
                if (where != DOWN
                        && !under.isOutOf(field.size())
                        && !field.isBrick(under)
                        && !field.isLadder(under)
                        && !field.isBorder(under)
                        && !field.isLadder(from)
                        && !field.isPipe(from)) return false;

                return true;
            }

            @Override
            public boolean possible(Point point) {
                if (field.isBrick(point)
                        || field.isBorder(point)) return false;
                return true;
            }
        };
    }

    public Map<Point, List<Direction>> ways(Field field) {
        return way.getPossibleWays(field.size(), possible(field)).toMap();
    }

    public List<Direction> getPath(Field field, Point from, List<Point> to) {
        return way.getShortestWay(field.size(), from, to, possible(field));
    }

    @Override
    public Point getReached() {
        return way.getReached();
    }
}
