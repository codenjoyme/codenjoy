package com.codenjoy.dojo.collapse.client.ai;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.collapse.client.Board;
import com.codenjoy.dojo.collapse.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

public class ApofigSolver implements Solver<Board> {

    private Board board;
    private Dice dice;

    public ApofigSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

        int x = dice.next(board.size());
        int y = dice.next(board.size());
        Direction direction = Direction.random(dice);

        return String.format("ACT(%s,%s),%s", x, y, direction);
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
