package com.codenjoy.dojo.spacerace.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

import java.util.List;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.spacerace.client.Board;
import com.codenjoy.dojo.spacerace.model.Elements;
import com.codenjoy.dojo.spacerace.services.GameRunner;

public class AlAnSolver implements Solver<Board> {

    private int delay = 0;
    private boolean vpravo = true;

    public AlAnSolver(Dice dice) {
    }

    @Override
    public String get(final Board board) {
        String result = "";
        int x = board.getMe().getX();

		if (vpravo && (x < board.size() - 5)||(x < 5)){
            result = Direction.RIGHT.toString();
            vpravo = true;
        }else {
            vpravo = false;
            result = Direction.LEFT.toString();
        }

        delay++;
        if(delay >= 3){
            result += Direction.UP.ACT.toString();
            delay = 0;
        }

        return result;
    }

    public static void main(String[] args) {
//        LocalGameRunner.run(new GameRunner(),
//                new ApofigSolver(new RandomDice()),
//                new Board());
        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host host) {
        WebSocketRunner.run(host,
                name,
                new AlAnSolver(new RandomDice()),
                new Board());
    }

}
