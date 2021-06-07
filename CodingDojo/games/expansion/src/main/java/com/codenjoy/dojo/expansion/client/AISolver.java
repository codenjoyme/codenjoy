package com.codenjoy.dojo.expansion.client;

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


import com.codenjoy.dojo.games.expansion.AbstractSolver;
import com.codenjoy.dojo.games.expansion.Board;
import com.codenjoy.dojo.games.expansion.Command;
import com.codenjoy.dojo.games.expansion.Forces;
import com.codenjoy.dojo.games.expansion.ForcesMoves;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class AISolver extends AbstractSolver {

    private int increase;
    private Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
        this.increase = 10;
    }

    public static int count = 0;
    public static long time = now();

    private static long now() {
        return Calendar.getInstance().getTimeInMillis();
    }

    private void calc() {
        count++;
        double t = (now() - time)/1000;
        double d = count/t;
        System.out.println("==> " + t);
        System.out.println("==> " + count);
        System.out.println("==> " + d);
        System.out.println("----------------");
    }

    public static DeikstraFindWay.Possible possible(Board board) {
        List<Point> barriers = board.barriers();

        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point point) {
                return !barriers.contains(point);
            }
        };
    }

    /**
     * @param to Destination point.
     * @return Shortest path (list of directions where to move) from any location to coordinates specified.
     */
    public static List<Direction> getShortestWay(Board board, Point from, List<Point> to) {
        DeikstraFindWay.Possible map = possible(board);
        DeikstraFindWay findWay = new DeikstraFindWay();
        List<Direction> shortestWay = findWay.getShortestWay(board.size(), from, to, map);
        return shortestWay;
    }

    /**
     * @param board use it for find elements on board
     * @return what hero should do in this tick (for this board)
     */
    @Override
    public Command whatToDo(Board board) {
//        calc();
        if (board.isGameOver()) return Command.doNothing();

        DeikstraFindWay.Possible map = possible(board);
        DeikstraFindWay findWay = new DeikstraFindWay(true);

        Point point = null;
        QDirection direction = null;
        List<Forces> forces = board.getEnemyForces();
        List<Point> destination = new LinkedList<>();
        for (Forces force : forces) {
            destination.add(force.getRegion());
        }
        if (!destination.isEmpty()) {
            int length = Integer.MAX_VALUE;
            List<Direction> shortestWay = null;
            for (Forces force : board.getMyForces()) {
                Point from = force.getRegion();
                List<Direction> way = findWay.getShortestWay(board.size(), from, destination, map);
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
            return Command.doNothing();
        }

        return Command
                .increase(new Forces(point, increase))
                .move(new ForcesMoves(point, board.forceAt(point).getCount(), direction))
                .build();
    }

    @Override
    protected String buildAnswer(Board board) {
        return String.format("message('%s')", super.buildAnswer(board));
    }
}