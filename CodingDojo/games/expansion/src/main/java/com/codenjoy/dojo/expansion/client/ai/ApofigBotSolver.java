package com.codenjoy.dojo.expansion.client.ai;

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


import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.expansion.client.AbstractSolver;
import com.codenjoy.dojo.expansion.client.Board;
import com.codenjoy.dojo.expansion.client.Command;
import com.codenjoy.dojo.expansion.client.YourSolver;
import com.codenjoy.dojo.expansion.model.Forces;
import com.codenjoy.dojo.expansion.model.ForcesMoves;
import com.codenjoy.dojo.services.*;

import java.util.*;

/**
 * Your AI
 */
public class ApofigBotSolver extends AbstractSolver {

    private int increase;
    private Dice dice;

    public ApofigBotSolver(Dice dice) {
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

    /**
     * @param board use it for find elements on board
     * @return what hero should d o in this tick (for this board)
     */
    @Override
    public Command whatToDo(Board board) {
//        calc();

        if (!board.isMeAlive()) return Command.doNothing();
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