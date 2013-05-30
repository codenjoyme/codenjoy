package com.codenjoy.bomberman.gameplay;

import com.codenjoy.bomberman.domain.*;

public class DirectionSolver {

    private Board board;

    public DirectionSolver(Board board) {
        this.board = board;
    }

    /**
     * @return next bot action
     */
    public String get() {
        return Direction.ACT.toString();
    }

}
