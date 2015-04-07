package com.codenjoy.dojo.snake.client.ai;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.algs.SnakeFindWay;
import com.codenjoy.dojo.snake.client.Board;
import com.codenjoy.dojo.snake.services.GameRunner;

import java.util.List;

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

        List<Point> apples = board.getApples();
        if (apples.isEmpty()) {
            return board.getSnakeDirection().toString();
        }
        SnakeFindWay logic = new SnakeFindWay(board.getHead(), apples.get(0), board.getSnakeDirection());
        return logic.get(board.getBarriers()).toString();
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
