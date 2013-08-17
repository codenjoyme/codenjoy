package com;

import com.utils.Board;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver {

    @Override
    public String get(Board board) {
        return Direction.ACT.toString();
    }
}
