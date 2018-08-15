package com.codenjoy.dojo.fifteen.client.ai;

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
import com.codenjoy.dojo.fifteen.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

public class AISolver implements Solver<Board> {

    private final Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(final Board board) {
        String[] directions = {
                Direction.DOWN.toString(),
                Direction.UP.toString(),
                Direction.RIGHT.toString(),
                Direction.LEFT.toString()};

        int random = 0;
        do {
            random = dice.next(directions.length);
        } while (!checkDirection(directions[random], board));

        return directions[random];
    }

    private boolean checkDirection(String direction, Board board) {
        Point me = board.getMe();

        int newX = me.getX();
        int newY = me.getY();

        if(direction.equals("UP")){
            newY--;
        } else if(direction.equals("DOWN")){
            newY++;
        } else if(direction.equals("LEFT")){
            newX--;
        } else if(direction.equals("RIGHT")) {
            newX++;
        }

        return !board.isBarrierAt(newX, newY);
    }
}
