package com.codenjoy.dojo.clifford.services.ai;

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
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.games.clifford.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.RandomDice;

import java.util.Calendar;
import java.util.Date;

public class YourSolverChecker implements Solver<Board> {

    private Dice dice;
    private Board board;
    private long time;

    public YourSolverChecker(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isGameOver()) return "";

        long delta = now() - time;
        long delta2 =delta - 1000;
        Date time = Calendar.getInstance().getTime();
        System.out.print(time.toString() + " > " + (delta2 < 0 ? delta2 : "+" + delta2));
        delta2 =  Math.abs(delta2);
        if (delta2 > 300) {
            System.out.println(" !!!!!!!!");
        } else if (delta2 > 200) {
            System.out.println(" !!!!");
        } else if (delta2 > 100) {
            System.out.println(" !!");
        } else if (delta2 > 50) {
            System.out.println(" !");
        } else {
            System.out.println(" ");
        }
        this.time = now();

        return Direction.RIGHT.toString();
    }

    public long now() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static void main(String[] args) {
        WebSocketRunner.PRINT_TO_CONSOLE = false;
        WebSocketRunner.runClient(args,
                // paste here board page url from browser after registration
                // or put it as command line parameter
                "https://codenjoy.com/codenjoy-contest/board/player/dojorena5?code=953820862434373766",
                new YourSolverChecker(new RandomDice()),
                new Board());
    }

}
