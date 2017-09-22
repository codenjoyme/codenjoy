package com.epam.dojo.icancode.client;

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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.epam.dojo.icancode.model.Elements;

import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.*;
import static com.epam.dojo.icancode.model.Elements.*;
import static com.epam.dojo.icancode.model.Elements.Layers.*;
import static com.epam.dojo.icancode.client.Command.*;

/**
 * Your AI
 */
public class YourSolver extends AbstractSolver {

    /**
     * @param board use it for find elements on board
     * @return what hero should do in this tick (for this board)
     */
    @Override
    public Command whatToDo(Board board) {
        if (!board.isMeAlive()) return doNothing();

        List<Point> goals = board.getGold();
        if (goals.isEmpty()) {
            goals = board.getExits();
        }
        List<Direction> shortestWay = board.getShortestWay(goals);
        if (shortestWay.isEmpty()) {
            return doNothing();
        }

        Direction direction = shortestWay.get(0);
        return go(direction);
    }

    /**
     * Run this method for connect to Server
     */
    public static void main(String[] args) {
        start("your@email.com", "dojo.lab.epam.com:80", new YourSolver());
    }

}