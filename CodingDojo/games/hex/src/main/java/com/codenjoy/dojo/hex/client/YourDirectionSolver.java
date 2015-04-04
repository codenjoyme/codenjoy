package com.codenjoy.dojo.hex.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.DirectionSolver;
import com.codenjoy.dojo.hex.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver<Board> {

    private Dice dice;
    private Board board;

    public YourDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

        Point point = board.get(Elements.MY_HERO).get(0);

        boolean jump = false;
        return command(point.getX(), point.getY(), Direction.UP, jump);
    }

    private String command(int x, int y, Direction direction, boolean jump) {
        return Direction.ACT.toString() + "(" + x + "," + y + ((jump)?",1":"")  + ")," + direction.toString();
    }

}
