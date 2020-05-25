package com.codenjoy.dojo.hex.client.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.hex.client.Board;
import com.codenjoy.dojo.hex.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.List;

public class AISolver implements Solver<Board> {

    public static final int MAX = 100;
    private Dice dice;
    private Board board;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";

        Point to;
        Direction direction;
        boolean jump;
        Point from;
        List<Point> points = board.get(Elements.MY_HERO);
        int count = 0;
        do {
            if (board.isGameOver()) return "";

            direction = Direction.random(dice);
            from = points.get(dice.next(points.size()));

            jump = dice.next(2) == 0;
            to = direction.change(from);
            if (jump) {
                to = direction.change(to);
            }
        } while (board.isBarrierAt(to.getX(), to.getY()) && count++ < MAX);
        if (count >= MAX) return "";

        return command(from.getX(), from.getY(),
                direction, jump);
    }

    private String command(int x, int y, Direction direction, boolean jump) {
        return Direction.ACT.toString() + "(" + x + "," + y + ((jump)?",1":"")  + ")," + direction.toString();
    }
}
