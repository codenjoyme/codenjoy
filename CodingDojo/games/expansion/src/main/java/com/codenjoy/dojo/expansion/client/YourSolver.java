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


import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.expansion.model.Forces;
import com.codenjoy.dojo.expansion.model.ForcesMoves;
import com.codenjoy.dojo.services.*;

import java.util.List;

import static com.codenjoy.dojo.expansion.client.Command.*;

/**
 * Your AI
 */
public class YourSolver extends AbstractSolver {

    private Dice dice;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    /**
     * @param board use it for find elements on board
     * @return what hero should do in this tick (for this board)
     */
    @Override
    public Command whatToDo(Board board) {
        if (!board.isMeAlive()) return doNothing();

        List<Point> destination = board.getGold();
        if (destination.isEmpty()) {
            destination = board.getExits();
        }

        Point faster = null;
        int length = Integer.MAX_VALUE;
        List<Direction> shortestWay = null;
        for (Forces force : board.getMyForces()) {
            Point from = force.getRegion();
            List<Direction> way = board.getShortestWay(from, destination);
            if (way.size() < length) {
                length = way.size();
                shortestWay = way;
                faster = from;
            }
        }

        if (shortestWay.isEmpty()) {
            return doNothing();
        }
        QDirection nextStep = QDirection.get(shortestWay.get(0));

        return Command
                .increase(new Forces(faster, 10))
                .move(new ForcesMoves(faster, 5, nextStep))
                .build();
    }

    @Override
    protected String buildAnswer(Board board) {
        return String.format("message('%s')", super.buildAnswer(board));
    }

    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://codenjoy.com:80/codenjoy-contest/board/player/your@email.com?code=12345678901234567890",
                new YourSolver(new RandomDice()),
                new Board());
    }

}