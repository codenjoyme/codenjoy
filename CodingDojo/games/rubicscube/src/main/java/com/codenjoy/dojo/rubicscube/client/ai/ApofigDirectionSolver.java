package com.codenjoy.dojo.rubicscube.client.ai;

import com.codenjoy.dojo.client.DirectionSolver;
import com.codenjoy.dojo.rubicscube.client.Board;
import com.codenjoy.dojo.rubicscube.client.Face;
import com.codenjoy.dojo.rubicscube.client.Rotate;
import com.codenjoy.dojo.services.Dice;

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

        return result(Face.random(dice), Rotate.random(dice));
    }

    private String result(Face face, Rotate rotate) {
        return String.format("ACT(%s, %s)", face.number(), rotate.rotate());
    }

}
