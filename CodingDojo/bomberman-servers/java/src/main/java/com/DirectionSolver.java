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

        Point bomb = null;
        if (board.isNear(bomberman, DESTROY_WALL) && !board.isAt(bomberman, BOMB_BOMBERMAN)) {
            bomb = new Point(bomberman);
        }

        direction = tryToMove(bomberman, bomb);
        return "" + ((bomb!=null)?ACT+",":"") + ((direction!=null)?direction:"");
    }

    private Direction tryToMove(Point pt, Point bomb) {
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
        } while ((bomb != null && bomb.equals(pt(x, y))) || board.isBarriersAt(x, y) || board.isNear(x, y, MEAT_CHOPPER) && count++ < 20);

        if (count < 20) {
            return result;
        }
        return null;
    }

    private Point pt(int x, int y) {
        return new Point(x, y);
    }
}
