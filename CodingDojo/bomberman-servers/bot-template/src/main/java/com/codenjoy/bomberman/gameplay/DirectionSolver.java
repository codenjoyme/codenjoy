package com.codenjoy.bomberman.gameplay;

import com.codenjoy.bomberman.domain.*;

public class DirectionSolver {

    private Board board;

    public DirectionSolver(Board board) {
        this.board = board;
    }
    /**
     * Returns next bot action
     * @return
     */
    public String get() {
        return Direction.ACT.toString();
    }

}
