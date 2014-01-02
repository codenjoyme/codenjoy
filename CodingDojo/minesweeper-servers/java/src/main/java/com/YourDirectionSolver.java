package com;

import com.utils.BoardImpl;
import com.utils.Dice;

import java.util.LinkedList;
import java.util.List;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver {

    private Dice dice;
    static List<Direction> path = new LinkedList<Direction>();
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
