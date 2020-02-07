package com.codenjoy.dojo.bomberman.client;

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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.RandomDice;

/**
 * User: your name
 */
public class YourSolverLite implements Solver<Board> {

    private Dice dice;
    private Board board;

    public YourSolverLite(Dice dice) {
        this.dice = dice;
    }

    // the method which should be implemented
    @Override
    public String get(Board board) {
        this.board = board;
        if (board.isMyBombermanDead()) return "";

        if (board.isNearMe(
                "???" +
                "☻☺?" +
                "???")) {
            return "RIGHT";
        } else if (board.isNearMe(
                "???" +
                "?☺☻" +
                "???")) {
            return "LEFT";
        } else if (board.isNearMe(
                "?☻?" +
                "?☺?" +
                "???")) {
            return "DOWN";
        } else if (board.isNearMe(
                "???" +
                "?☺?" +
                "?☻?")) {
            return "UP";
        }


        // put your logic here
        return Direction.DOWN.toString();
    }

    /**
     * To connect to the game server:
     * 1. Sign up on the game server. If you did everything right, you'll get to the main game board.
     * 2. Click on your name on the right hand side panel
     * 3. Copy the whole link from the browser, paste it inside below method, now you're good to go!
     */
    public static void main(String[] args) {
        WebSocketRunner.runClient(
                // paste here board page url from browser after registration
                "http://127.0.0.1:8080/codenjoy-contest/board/player/0?code=000000000000&gameName=bomberman",
                new YourSolverLite(new RandomDice()),
                new Board());
    }

}
