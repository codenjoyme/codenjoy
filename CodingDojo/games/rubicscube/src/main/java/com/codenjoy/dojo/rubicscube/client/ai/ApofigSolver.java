package com.codenjoy.dojo.rubicscube.client.ai;

import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.rubicscube.client.Board;
import com.codenjoy.dojo.rubicscube.client.Face;
import com.codenjoy.dojo.rubicscube.client.Rotate;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

/**
 * User: your name
 */
public class ApofigSolver implements Solver<Board> {

    private Dice dice;
    private Board board;

    public ApofigSolver(Dice dice) {
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

    public static void main(String[] args) {
        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new ApofigSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
