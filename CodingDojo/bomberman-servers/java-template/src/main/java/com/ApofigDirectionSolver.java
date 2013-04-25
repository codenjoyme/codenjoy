package com;

import com.utils.Board;
import com.utils.Point;

import java.util.Random;
import static com.utils.Point.*;

public class ApofigDirectionSolver {

    private static Direction direction;
    private Random dice = new Random();

    public ApofigDirectionSolver() {
    }

    public String get(Board board) {
        Point bomberman = board.getBomberman();

        Point bomb = null;
        if (board.isNear(bomberman, Element.DESTROY_WALL) && !board.isAt(bomberman, Element.BOMB_BOMBERMAN)) {
            bomb = new Point(bomberman);
        }

        direction = tryToMove(board, bomberman, bomb);
        return "" + ((bomb!=null)? Direction.ACT+",":"") + ((direction!=null)?direction:"");
    }

    private Direction tryToMove(Board board, Point pt, Point bomb) {
        int count = 0;
        int x = pt.x;
        int y = pt.y;
        Direction result = null;
        do {
            int count1 = 0;
            do {
                result = Direction.valueOf(dice.nextInt(4));
            } while (count1++ < 10 && (result.inverted() == direction && board.countNear(pt, Element.SPACE) > 1));

            x = result.changeX(pt.x);
            y = result.changeY(pt.y);
        } while (count++ < 20 && ((bomb != null && bomb.equals(pt(x, y))) || board.isBarrierAt(x, y) || board.isNear(x, y, Element.MEAT_CHOPPER)));

        if (count < 20) {
            return result;
        }
        return Direction.ACT;
    }
}
