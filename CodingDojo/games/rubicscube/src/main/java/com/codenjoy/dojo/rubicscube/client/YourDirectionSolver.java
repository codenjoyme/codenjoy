package com.codenjoy.dojo.rubicscube.client;

import com.codenjoy.dojo.rubicscube.client.utils.BoardImpl;
import com.codenjoy.dojo.rubicscube.client.utils.Dice;

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

        return result(Face.DOWN, Rotate.CLOCKWISE);
    }

    private String result(Face face, Rotate rotate) {
        return String.format("ACT(%s, %s)", face.number, rotate.rotate);
    }

}
