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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.icancode.client.AbstractSolver;
import com.codenjoy.dojo.icancode.client.Board;
import com.codenjoy.dojo.icancode.client.Command;

import java.util.List;

import static com.codenjoy.dojo.icancode.client.Command.*;
import static com.codenjoy.dojo.icancode.model.Elements.*;

public class AISolver extends AbstractSolver {

    public AISolver(Dice dice) {
        super(dice);
    }

    @Override
    public Command whatToDo(Board board) {
        if (!board.isMeAlive()) return doNothing();

        List<Point> destination = board.getGold();
        if (destination.isEmpty()) {
            destination = board.getExits();
        }
        List<Direction> shortestWay = getShortestWay(board, destination);
        if (shortestWay.isEmpty()) {
            return doNothing();
        }
        Direction nextStep = shortestWay.get(0);
        Point me = board.getMe();
        Point whereToGo = nextStep.change(me);
        if (board.isAt(whereToGo.getX(), whereToGo.getY(), HOLE, BOX, LASER_RIGHT, LASER_LEFT, LASER_UP, LASER_DOWN)) {
            return Command.jump(nextStep);
        }

        if (shortestWay.size() != 1 && board.isAt(whereToGo.getX(), whereToGo.getY(), EXIT)) {
            return Command.jump(nextStep);
        }

        return go(nextStep);
    }

    private DeikstraFindWay.Possible possible(final Board board) {
        return new DeikstraFindWay.Possible() {
            @Override // TODO test me
            public boolean possible(Point point) {
                return !board.isBarrierAt(point.getX(), point.getY());
            }
        };
    }

    /**
     * @param to Destination point.
     * @return Shortest path (list of directions where to move) from your robot location to coordinates specified.
     */
    List<Direction> getShortestWay(Board board, List<Point> to) {
        DeikstraFindWay.Possible map = possible(board);
        DeikstraFindWay findWay = new DeikstraFindWay();
        List<Direction> shortestWay = findWay.getShortestWay(board.size(), board.getMe(), to, map);
        return shortestWay;
    }
}