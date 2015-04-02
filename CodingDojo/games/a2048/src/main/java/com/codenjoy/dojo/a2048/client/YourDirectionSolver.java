package com.codenjoy.dojo.a2048.client;


import com.codenjoy.dojo.a2048.client.utils.BoardImpl;
import com.codenjoy.dojo.a2048.client.utils.Dice;

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

        return Direction.UP.toString();
    }

}
