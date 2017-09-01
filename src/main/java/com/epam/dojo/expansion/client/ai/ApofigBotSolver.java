package com.epam.dojo.expansion.client.ai;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.expansion.client.*;
import com.epam.dojo.expansion.model.Elements;
import com.epam.dojo.expansion.model.Forces;
import com.epam.dojo.expansion.model.ForcesMoves;

import java.util.List;

import static com.epam.dojo.expansion.client.Command.*;

/**
 * Your AI
 */
public class ApofigBotSolver extends AbstractSolver {

    /**
     * @param board use it for find elements on board
     * @return what hero should d o in this tick (for this board)
     */
    @Override
    public Command whatToDo(Board board) {
        if (!board.isMeAlive()) return doNothing();
        Point point = null;
        QDirection direction = null;
        List<Point> destination = board.getGold();
        if (!destination.isEmpty()) {
            int length = Integer.MAX_VALUE;
            List<Direction> shortestWay = null;
            for (Forces force : board.getMyForces()) {
                Point from = force.getRegion();
                List<Direction> way = board.getShortestWay(from, destination);
                if (way.size() < length) {
                    length = way.size();
                    shortestWay = way;
                    point = from;
                }
            }
            if (!shortestWay.isEmpty()) {
                direction = QDirection.get(shortestWay.get(0));
            }
        }
        if (direction == null) {
            List<Point> free = board.getFreeSpaces();
            List<Forces> forces = board.getMyForces();
            for (Forces force : forces) {
                Point pt1 = force.getRegion();
                for (QDirection d : QDirection.values()) {
                    Point pt2 = d.change(pt1);
                    if (free.contains(pt2)) {
                        point = pt1;
                        direction = d;
                        break;
                    }
                }
            }
        }
        if (direction == null) {
            return doNothing();
        }

        return Command
                .increase(new Forces(point, 10))
                .move(new ForcesMoves(point, 5, direction))
                .build();
    }

    @Override
    protected String buildAnswer(Board board) {
        return String.format("message('%s')", super.buildAnswer(board));
    }

    /**
     * Run this method for connect to Server
     */
    public static void main(String[] args) {
        start("apofig@gmail.com", "127.0.0.1:8080", new ApofigBotSolver());
    }

}