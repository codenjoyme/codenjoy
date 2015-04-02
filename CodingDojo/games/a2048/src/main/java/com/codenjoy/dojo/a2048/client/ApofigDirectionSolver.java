package com.codenjoy.dojo.a2048.client;


import com.codenjoy.dojo.a2048.client.utils.BoardImpl;
import com.codenjoy.dojo.a2048.client.utils.Dice;

import java.util.Random;

/**
 * User: your name
 */
public class ApofigDirectionSolver implements DirectionSolver {

    private Dice dice;
    private BoardImpl board;

    public ApofigDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(BoardImpl board) {
        this.board = board;

        return Direction.values()[new Random().nextInt(4)].toString();
    }

}
