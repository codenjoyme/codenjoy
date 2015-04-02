package com.codenjoy.dojo.sample.client;

import com.codenjoy.dojo.sample.client.utils.Dice;
import com.codenjoy.dojo.sample.client.utils.BoardImpl;

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
        if (board.isGameOver()) return "";

        return Direction.UP.toString();
    }

}
