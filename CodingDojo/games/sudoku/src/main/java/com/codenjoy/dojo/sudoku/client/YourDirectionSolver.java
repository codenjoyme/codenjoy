package com.codenjoy.dojo.sudoku.client;

import com.codenjoy.dojo.sudoku.client.utils.BoardImpl;
import com.codenjoy.dojo.sudoku.client.utils.Dice;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver {

    private Dice dice;
    private BoardImpl board;

    public YourDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(BoardImpl board) {
        this.board = board;

        return "ACT(1,2,3)";
    }

}
