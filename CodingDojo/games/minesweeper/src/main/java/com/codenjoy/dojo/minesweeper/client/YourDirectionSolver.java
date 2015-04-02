package com.codenjoy.dojo.minesweeper.client;

import com.codenjoy.dojo.minesweeper.client.utils.BoardImpl;
import com.codenjoy.dojo.minesweeper.client.utils.Dice;

import java.util.LinkedList;
import java.util.List;

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
