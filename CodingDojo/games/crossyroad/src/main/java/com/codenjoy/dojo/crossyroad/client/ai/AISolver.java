package com.codenjoy.dojo.crossyroad.client.ai;

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
import com.codenjoy.dojo.crossyroad.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.crossyroad.client.Board;
import com.codenjoy.dojo.services.Direction;

public class AISolver implements Solver<Board> {

    private Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    private Direction dir = Direction.UP;

    @Override
    public String get(final Board board) {
        if ((board.getNear(board.getMe()).get(0) == Elements.CARLEFTTORIGHT)
                || (board.getNear(board.getMe()).get(3) == Elements.STONE)
                || (board.getNear(board.getMe()).get(5) == Elements.CARRIGHTTOLEFT)) {
            if ((board.getNear(board.getMe()).get(1) == Elements.STONE)
                    & (board.getNear(board.getMe()).get(0) == Elements.CARLEFTTORIGHT)) {
                dir = Direction.STOP;
            } else if ((board.getNear(board.getMe()).get(6) == Elements.STONE)
                    & (board.getNear(board.getMe()).get(5) == Elements.CARRIGHTTOLEFT)) {
                dir = Direction.STOP;
            } else if (board.getNear(board.getMe()).get(1) == Elements.STONE) {
                dir = Direction.RIGHT;
            } else if (board.getNear(board.getMe()).get(5) == Elements.STONE) {
                dir = Direction.LEFT;
            } else dir = Direction.STOP;
        } else dir = Direction.UP;
        return dir.toString();
    }
}
