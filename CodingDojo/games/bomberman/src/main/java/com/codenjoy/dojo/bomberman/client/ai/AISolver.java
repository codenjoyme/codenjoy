package com.codenjoy.dojo.bomberman.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import org.apache.commons.lang3.StringUtils;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class AISolver implements Solver<Board> {

    private Direction direction;
    private Point bomb;
    private Dice dice;
    private Board board;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;
        Point bomberman = board.getBomberman();

        boolean nearDestroyableWall = board.isNear(bomberman.getX(), bomberman.getY(), Elements.DESTROYABLE_WALL);
        boolean nearBomberman = board.isNear(bomberman.getX(), bomberman.getY(), Elements.OTHER_BOMBERMAN);
        boolean nearMeatchopper = board.isNear(bomberman.getX(), bomberman.getY(), Elements.MEAT_CHOPPER);
        boolean bombNotDropped = !board.isAt(bomberman.getX(), bomberman.getY(), Elements.BOMB_BOMBERMAN);

        bomb = null;
        if ((nearDestroyableWall || nearBomberman || nearMeatchopper) && bombNotDropped) {
            bomb = new PointImpl(bomberman);
        }

        direction = tryToMove(bomberman);

        String result = mergeCommands(bomb, direction);
        return StringUtils.isEmpty(result) ? Direction.STOP.toString() : result;
    }

    private String mergeCommands(Point bomb, Direction direction) {
        if (Direction.STOP.equals(direction)) {
            bomb = null;
        }
        return "" + ((bomb!=null)? Direction.ACT+((direction!=null)?",":""):"") +
                ((direction!=null)?direction:"");
    }

    private Direction tryToMove(Point pt) {
        int count = 0;
        int newX = pt.getX();
        int newY = pt.getY();
        Direction result = null;
        boolean again = false;
        do {
            result = whereICAnGoFrom(pt);
            if (result == null) {
                return null;
            }

            newX = result.changeX(pt.getX());
            newY = result.changeY(pt.getY());

            boolean bombAtWay = bomb != null && bomb.equals(pt(newX, newY));
            boolean barrierAtWay = board.isBarrierAt(newX, newY);
            boolean blastAtWay = board.getFutureBlasts().contains(pt(newX, newY));
            boolean meatChopperNearWay = board.isNear(newX, newY, Elements.MEAT_CHOPPER);

            if (blastAtWay && board.countNear(pt.getX(), pt.getY(), Elements.NONE) == 1 &&
                    !board.isAt(pt.getX(), pt.getY(), Elements.BOMB_BOMBERMAN)) {
                return Direction.STOP;
            }

            again = bombAtWay || barrierAtWay || meatChopperNearWay;

            // TODO продолжить но с тестами
            boolean deadEndAtWay = board.countNear(newX, newY, Elements.NONE) == 0 && bomb != null;
            if (deadEndAtWay) {
                bomb = null;
            }
        } while (count++ < 20 && again);

        if (count < 20) {
            return result;
        }
        return Direction.ACT;
    }

    private Direction whereICAnGoFrom(Point pt) {
        Direction result;
        int count = 0;
        do {
            result = Direction.valueOf(dice.next(4));
        } while (count++ < 10 &&
                ((result.inverted() == direction && bomb == null) ||
                        !board.isAt(result.changeX(pt.getX()), result.changeY(pt.getY()), Elements.NONE)));
        if (count > 10) {
            return null;
        }
        return result;
    }
}
