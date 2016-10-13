package com.epam.dojo.icancode.client.ai;

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


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.client.AbstractSolver;
import com.epam.dojo.icancode.client.Board;

import java.util.List;

import static com.epam.dojo.icancode.model.Elements.*;

/**
 * Your AI
 */
public class ApofigBotSolver extends AbstractSolver {

    /**
     * @param board use it for find elements on board
     * @return what hero should d o in this tick (for this board)
     */
    @Override
    public String whatToDo(Board board) {
        if (!board.isMeAlive()) return doNothing();

        List<Point> destination = board.getGold();
        if (destination.isEmpty()) {
            destination = board.getExit();
        }
        List<Direction> shortestWay = board.getShortestWay(destination);
        if (shortestWay.isEmpty()) {
            return doNothing();
        }
        Direction nextStep = shortestWay.get(0);
        Point me = board.getMe();
        Point whereToGo = nextStep.change(me);
        if (board.isAt(whereToGo.getX(), whereToGo.getY(), HOLE, BOX, LASER_RIGHT, LASER_LEFT, LASER_UP, LASER_DOWN)) {
            return jumpTo(nextStep);
        }

        return go(nextStep);
    }

    /**
     * Run this method for connect to Server
     */
    public static void main(String[] args) {
        start("user@gmail.com", "127.0.0.1:8080", new ApofigBotSolver());
    }

}