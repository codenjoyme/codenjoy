package com.codenjoy.dojo.bomberman.client;

import com.codenjoy.dojo.bomberman.client.utils.Board;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver {

    @Override
    public String get(Board board) {
        if (board.isGameOver()) return "";

        return Direction.ACT.toString();
    }
}
