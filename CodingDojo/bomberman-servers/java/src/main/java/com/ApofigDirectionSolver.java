package com;

import com.utils.Board;
import com.utils.Point;

import java.util.Random;
import static com.utils.Point.*;

public class ApofigDirectionSolver implements DirectionSolver {

    private Direction direction;
    private Point bomb;
    private Random dice = new Random();

    public ApofigDirectionSolver() {
    }

    @Override
    public String get(String boardString) {
        Board board = new Board(boardString);
        Point bomberman = board.getBomberman();

        boolean nearDestroyWall = board.isNear(bomberman.getX(), bomberman.getY(), Element.DESTROY_WALL);
        boolean nearBomberman = board.isNear(bomberman.getX(), bomberman.getY(), Element.OTHER_BOMBERMAN);
        boolean nearMeatchopper = board.isNear(bomberman.getX(), bomberman.getY(), Element.MEAT_CHOPPER);
        boolean bombNotDropped = !board.isAt(bomberman.getX(), bomberman.getY(), Element.BOMB_BOMBERMAN);

        bomb = null;
        if ((nearDestroyWall || nearBomberman || nearMeatchopper) && bombNotDropped) {
            bomb = new Point(bomberman);
        }

        direction = tryToMove(board, bomberman);

        return mergeCommands(bomb, direction);
    }

    private String mergeCommands(Point bomb, Direction direction) {
        return "" + ((bomb!=null)? Direction.ACT+",":"") + ((direction!=null)?direction:"");
    }

    private Direction tryToMove(Board board, Point pt) {
        int count = 0;
        int newX = pt.getX();
        int newY = pt.getY();
        Direction result = null;
        boolean again = false;
        do {
            int count1 = 0;
            do {
                result = Direction.valueOf(dice.nextInt(4));
            } while (count1++ < 10 && (result.inverted() == direction && board.countNear(pt.getX(), pt.getY(), Element.SPACE) > 1));

            newX = result.changeX(pt.getX());
            newY = result.changeY(pt.getY());

            boolean bombAtWay = bomb != null && bomb.equals(pt(newX, newY));
            boolean barrierAtWay = board.isBarrierAt(newX, newY);
            boolean meatChopperNearWay = board.isNear(newX, newY, Element.MEAT_CHOPPER);

            again = bombAtWay || barrierAtWay || meatChopperNearWay;

            boolean deadEndAtWay = board.countNear(newX, newY, Element.SPACE) == 0 && bomb != null;  // TODO продолжить но с тестами
            if (deadEndAtWay) {
                bomb = null;
            }
        } while (count++ < 20 && again);

        if (count < 20) {
            return result;
        }
        return Direction.ACT;
    }
}
