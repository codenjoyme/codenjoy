package com.codenjoy.dojo.lunolet.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.lunolet.client.Board;
import com.codenjoy.dojo.lunolet.model.VesselState;
import com.codenjoy.dojo.services.Direction;

import java.awt.geom.Point2D;

public class DumbSolver implements Solver<Board> {

    @Override
    public String get(Board board) {
        Point2D.Double point = board.getPoint();
        Point2D.Double target = board.getTarget();

        if (board.getState() == VesselState.START) { // take-off
            return "message('go 0, 0.8, 3')";
        }

        if (point.getY() < 8.0 || board.getVSpeed() < -1.5) {
            return Direction.UP.toString();
        }
        if (point.getX() < target.getX() && board.getHSpeed() < 3.0) {
            return Direction.RIGHT.toString();
        } else if (point.getX() > target.getX() && board.getHSpeed() > -3.0) {
            return Direction.LEFT.toString();
        }

        return Direction.DOWN.toString();
    }

    public static void main(String[] args) {
        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host host) {
        WebSocketRunner.run(host,
                name,
                null,
                new DumbSolver(),
                new Board());
    }
}
