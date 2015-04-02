package com.codenjoy.dojo.collapse.client;


import com.codenjoy.dojo.collapse.client.utils.BoardImpl;
import com.codenjoy.dojo.collapse.client.utils.Dice;

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

        return "ACT(3,4),RIGHT";
    }

}
