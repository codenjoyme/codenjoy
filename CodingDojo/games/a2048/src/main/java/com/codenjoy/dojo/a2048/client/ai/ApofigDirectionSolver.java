package com.codenjoy.dojo.a2048.client.ai;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.client.DirectionSolver;
import com.codenjoy.dojo.services.Dice;

import java.util.Random;

/**
 * User: your name
 */
public class ApofigDirectionSolver implements DirectionSolver<Board> {

    private Dice dice;
    private Board board;

    public ApofigDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

        return Direction.values()[new Random().nextInt(4)].toString();
    }

}
