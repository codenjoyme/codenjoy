package com.codenjoy.dojo.bomberman.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    @Override
    public String get(Board board) {
        if (board.isGameOver()) return "";

        return Direction.ACT.toString();
    }
}
