package com.codenjoy.dojo.icancode.client.ai;

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


import com.codenjoy.dojo.icancode.client.Board;
import com.codenjoy.dojo.icancode.client.ai.finder.CrudePathFinder;
import com.codenjoy.dojo.icancode.client.ai.finder.PathFinder;
import com.codenjoy.dojo.icancode.client.ai.finder.PathGrid;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Mikhail_Udalyi on 07.10.2016.
 */
public class BotBoard extends Board {

    private PathFinder finder;

    public List<Direction> findPath(Point start, Point finish) {
        finder = new CrudePathFinder(1000);

        int[] path = finder.findPath(new PathGrid(this), start.getX(), start.getY(), finish.getX(), finish.getY());
        if (path == null) {
            return null;
        }

        List<Direction> result = new LinkedList<>();

        //0 = up, 1 = right, 2 = down, 3 = left.
        for (int i = 0; i < path.length; ++i) {
            switch (path[i]) {
                case 0:
                    result.add(Direction.UP);
                    break;
                case 1:
                    result.add(Direction.RIGHT);
                    break;
                case 2:
                    result.add(Direction.DOWN);
                    break;
                case 3:
                    result.add(Direction.LEFT);
                    break;
            }
        }

        return result;
    }

    public Point getNearInList(Point start, List<Point> list) {
        if (list.size() == 0) {
            return null;
        }

        int distance = 1000;
        Point result = list.get(0);
        for (int i = 0; i < list.size(); ++i) {
            int curDistance = heuristic(start, list.get(i));
            if (curDistance < distance) {
                distance = curDistance;
                result = list.get(i);
            }
        }

        return result;
    }

    public int heuristic(Point pos0, Point pos1) {
        int d1 = Math.abs(pos1.getX() - pos0.getX());
        int d2 = Math.abs(pos1.getY() - pos0.getY());
        return d1 + d2;
    }
}
