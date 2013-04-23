package com.apofig;

import java.util.Random;

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
        if (board.isNear(bomberman, Board.DESTROY_WALL) && !board.isAt(bomberman, Board.BOMB_BOMBERMAN)) {
            bomb = new Point(bomberman);
        }

        direction = tryToMove(bomberman, bomb);
        return "" + ((bomb!=null)? Direction.ACT+",":"") + ((direction!=null)?direction:"");
    }

    private Direction tryToMove(Point pt, Point bomb) {
        int count = 0;
        int x = pt.x;
        int y = pt.y;
        Direction result = null;
        do {
            int count1 = 0;
            do {
                result = Direction.valueOf(dice.nextInt(4));
            } while (count1++ < 10 && (result.inverted() == direction && board.countAt(pt, Board.SPACE) > 1));

            x = result.changeX(pt.x);
            y = result.changeY(pt.y);
        } while (count++ < 20 && ((bomb != null && bomb.equals(pt(x, y))) || board.isBarriersAt(x, y) || board.isNear(x, y, Board.MEAT_CHOPPER)));

        if (count < 20) {
            return result;
        }
        return Direction.ACT;
    }

    private Point pt(int x, int y) {
        return new Point(x, y);
    }
}
