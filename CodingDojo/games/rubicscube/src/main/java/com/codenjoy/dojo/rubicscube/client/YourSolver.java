package com.codenjoy.dojo.rubicscube.client;

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

        return result(Face.DOWN, Rotate.CLOCKWISE);
    }

    private String result(Face face, Rotate rotate) {
        return String.format("ACT(%s, %s)", face.number(), rotate.rotate());
    }

}
