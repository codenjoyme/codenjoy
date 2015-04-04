package com.codenjoy.dojo.snake.client.ai;

import com.codenjoy.dojo.client.DirectionSolver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.SnakeFindWay;
import com.codenjoy.dojo.snake.client.Board;

import java.util.List;

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

        List<Point> apples = board.getApples();
        if (apples.isEmpty()) {
            return board.getSnakeDirection().toString();
        }
        SnakeFindWay logic = new SnakeFindWay(board.getHead(), apples.get(0), board.getSnakeDirection());
        return logic.get(board.getBarriers()).toString();
    }


}
