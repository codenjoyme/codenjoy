package com;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class DirectionSolver {

    private Board board;
    private static Direction direction;
    private static Queue<Direction> futureDirections = new LinkedList<Direction>();
    private Random dice = new Random();

    public DirectionSolver(Board board) {
        this.board = board;
    }

    public String get() {
        while (futureDirections.size() != 0) {
            Direction first = futureDirections.remove();
            if (futureDirections.size() != 0) {
                if (first == Direction.ACT || futureDirections.peek() == Direction.ACT) {
                    return first.name() + "," + futureDirections.remove().name();
                }
            }
            return first.name();
        }

        Point bomberman = board.getBomberman();

        if (board.isNear(bomberman, Board.DESTROY_WALL) && !board.isAt(bomberman, Board.BOMB_BOMBERMAN)) {
            next(Direction.ACT);
            if (direction != null) {
                next(direction.inverted());
            }
            return get();
        }

//        if (direction != null && dice.nextInt(5) > 0) {
//            int x = direction.changeX(bomberman.x);
//            int y = direction.changeY(bomberman.y);
//            if (!board.isBarriersAt(pt(x, y))) {
//                return direction.name();
//            } else {
//                // do nothig
//            }
//        }
        direction = tryToMove(bomberman);
        next(direction);
        return get();
    }

    private void next(Direction direction) {
        if (direction == null) {
            direction = Direction.STOP;
        }
        futureDirections.add(direction);
    }

    private Direction tryToMove(Point pt) {
        int count = 0;
        int x = pt.x;
        int y = pt.y;
        Direction result = null;
        do {
            do {
                result = Direction.valueOf(dice.nextInt(4));
            } while (result.inverted() == direction && board.countAt(pt, Board.SPACE) > 1);

            x = result.changeX(pt.x);
            y = result.changeY(pt.y);
        } while (board.isBarriersAt(pt(x, y)) || board.isNear(pt(x, y), Board.MEAT_CHOPPER) && count++ < 10);

        if (count < 10) {
            return result;
        }
        return null;
    }

    private Point pt(int x, int y) {
        return new Point(x, y);
    }
}
