package com.codenjoy.dojo.collapse.client.ai;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.DirectionSolver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.collapse.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

public class ApofigDirectionSolver implements DirectionSolver<Board> {

    private Board board;
    private Dice dice;

    public ApofigDirectionSolver(Dice dice) {
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
        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new ApofigDirectionSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
