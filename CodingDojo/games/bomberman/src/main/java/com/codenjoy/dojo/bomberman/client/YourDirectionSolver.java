package com.codenjoy.dojo.bomberman.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.DirectionSolver;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver<Board> {

    @Override
    public String get(Board board) {
        if (board.isGameOver()) return "";

        return Direction.ACT.toString();
    }
}
