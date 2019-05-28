package com.codenjoy.dojo.snakebattle.client.ai;

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


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.snakebattle.client.Board;

import java.util.List;

import static com.codenjoy.dojo.snakebattle.model.Elements.*;

public class AISolver implements Solver<Board> {

    private DeikstraFindWay way;
    private Point myNeck;
    Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
        this.way = new DeikstraFindWay();
    }

    public DeikstraFindWay.Possible possible(final Board board, final Point... excludePoints) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                int x = from.getX();
                int y = from.getY();
                if (board.isBarrierAt(x, y)) return false;

                Point newPt = where.change(from);
                for (Point p : excludePoints)
                    if (p != null && p.equals(newPt))
                        return false;
                int nx = newPt.getX();
                int ny = newPt.getY();

                if (board.isOutOfField(nx, ny)) return false;

                if (board.isBarrierAt(nx, ny)) return false;
                // вероятность не есть камень 3/4
                if (board.isStoneAt(nx, ny))
                    if (dice.next(4) != 0)
                        return false;
                // вероятность не врезаться в противника 9/10
                if (board.isAt(nx, ny, ENEMY_HEAD_DOWN,
                        ENEMY_HEAD_LEFT,
                        ENEMY_HEAD_RIGHT,
                        ENEMY_HEAD_UP,
                        ENEMY_HEAD_EVIL,
                        ENEMY_HEAD_FLY,
                        ENEMY_TAIL_END_DOWN,
                        ENEMY_TAIL_END_LEFT,
                        ENEMY_TAIL_END_UP,
                        ENEMY_TAIL_END_RIGHT,
                        ENEMY_BODY_HORIZONTAL,
                        ENEMY_BODY_VERTICAL,
                        ENEMY_BODY_LEFT_DOWN,
                        ENEMY_BODY_LEFT_UP,
                        ENEMY_BODY_RIGHT_DOWN,
                        ENEMY_BODY_RIGHT_UP))
                    if (dice.next(10) != 0)
                        return false;
                //вероятность не есть себя 3/4
                if (board.isAt(nx, ny, BODY_HORIZONTAL,
                        BODY_VERTICAL,
                        BODY_LEFT_DOWN,
                        BODY_LEFT_UP,
                        BODY_RIGHT_DOWN,
                        BODY_RIGHT_UP))
                    if (dice.next(3) != 0)
                        return false;

                return true;
            }

            @Override
            public boolean possible(Point atWay) {
                int x = atWay.getX();
                int y = atWay.getY();
                if (board.isBarrierAt(x, y)) return false;
                if (board.isOutOfField(x, y)) return false;
                return true;
            }
        };
    }

    @Override
    public String get(final Board board) {
        if (board.isGameOver()) return "";
        List<Direction> result = getDirections(board, myNeck);
        myNeck = board.getMe();
        if (result.isEmpty()) return "";
        return result.get(0).toString() + getBombIfNeeded(board);
    }

    private String getBombIfNeeded(Board board) {
        Point me = board.getMe();
        if (me.getX() % 2 == 0 && me.getY() % 2 == 0) {
            return ", ACT";
        } else {
            return "";
        }
    }

    public List<Direction> getDirections(Board board, Point... excludePoints) {
        int size = board.size();

        Point from = board.getMe();
        List<Point> to = board.get(APPLE, GOLD, FURY_PILL, FLYING_PILL); //, FLYING_PILL, FURY_PILL
        DeikstraFindWay.Possible map = possible(board, excludePoints);
        return way.getShortestWay(size, from, to, map);
    }
}
