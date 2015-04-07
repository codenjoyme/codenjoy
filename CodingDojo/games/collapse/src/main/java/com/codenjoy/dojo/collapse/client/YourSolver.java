package com.codenjoy.dojo.collapse.client;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;

/**
 * User: your name
 */
public class YourSolver implements Solver<Board> {

    private Dice dice;
    private Board board;

    public YourSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

        return "ACT(3,4),RIGHT";
    }

}
