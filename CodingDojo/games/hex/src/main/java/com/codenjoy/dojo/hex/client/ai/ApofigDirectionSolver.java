package com.codenjoy.dojo.hex.client.ai;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.DirectionSolver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.hex.client.Board;
import com.codenjoy.dojo.hex.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

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
        if (board.isGameOver()) return "";

        Point to;
        Direction direction;
        boolean jump;
        Point from;
        List<Point> points = board.get(Elements.MY_HERO);
        int count = 0;
        do {
            if (board.isGameOver()) return "";

            direction = Direction.random(dice);
            from = points.get(dice.next(points.size()));

            jump = dice.next(2) == 0;
            to = direction.change(from);
            if (jump) {
                to = direction.change(to);
            }
        } while (board.isBarrierAt(to.getX(), to.getY()) && count++ < 110);
        if (count == 100) return "";

        return command(from.getX(), from.getY(),
                direction, jump);
    }

    private String command(int x, int y, Direction direction, boolean jump) {
        return Direction.ACT.toString() + "(" + x + "," + y + ((jump)?",1":"")  + ")," + direction.toString();
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
