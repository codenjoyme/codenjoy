package com.codenjoy.dojo.a2048.client.ai;


import com.codenjoy.dojo.a2048.services.GameRunner;
import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

import java.util.Random;

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

        return Direction.values()[new Random().nextInt(4)].toString();
    }

    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new ApofigSolver(new RandomDice()),
                new Board());
//        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
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
