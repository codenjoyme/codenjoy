package com;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import  static com.Direction.*;
import  static com.Board.*;

public class DirectionSolver {

    private Board board;
    private static Direction direction;
    private Random dice = new Random();

    public DirectionSolver(Board board) {
        this.board = board;
    }

    public String get() {
        Point bomberman = board.getBomberman();

        if (board.isNear(bomberman, DESTROY_WALL) && !board.isAt(bomberman, BOMB_BOMBERMAN)) {
            return "" + ACT + ((direction != null)?","+direction.inverted():RIGHT);
        }

        direction = tryToMove(bomberman);
        return "" + ((direction!=null)?direction:"");
    }

    private Direction tryToMove(Point pt) {
        int count = 0;
        int x = pt.x;
        int y = pt.y;
        Direction result = null;
        do {
            do {
                result = Direction.valueOf(dice.nextInt(4));
            } while (result.inverted() == direction && board.countAt(pt, SPACE) > 1);

            x = result.changeX(pt.x);
            y = result.changeY(pt.y);
        } while (board.isBarriersAt(pt(x, y)) || board.isNear(pt(x, y), MEAT_CHOPPER) && count++ < 20);

        if (count < 20) {
            return result;
        }
        return null;
    }

    private Point pt(int x, int y) {
        return new Point(x, y);
    }
}
