package com.codenjoy.dojo.minesweeper.client.ai;

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
import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.client.ai.logic.Action;
import com.codenjoy.dojo.minesweeper.client.ai.logic.Field;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.minesweeper.model.Elements.*;
import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class AISolver implements Solver<Board> {

    public static final int HIDDEN_VALUE = 9;
    public static final int FLAG_VALUE = 11;
    public static final int NONE_VALUE = 0;
    public static final int BANG_VALUE = 12;
    public static final int DETECTOR_VALUE = 10;
    public static final int BORDER_VALUE = -1;

    private Point me;
    private Elements underMe;

    public AISolver(Dice dice) {
    }

    public String get(Board board) {
        System.out.println(board.toString());

        if (board.isGameOver() || board.isWin()) {
            return STOP.toString();
        }

        me = board.getMe();

        if (isFirstTurn(board)) {
            return UP.toString();
        }

        Field field = new Field(board.size());
        field.scan(pt -> convert(board.getAt(pt).ch()));
        field.play();
        List<Action> actions = field.actions();

        Action to = getClosest(actions);
        if (to == null) {
            throw new RuntimeException(); // TODO решить это
        }
        Direction where;
        boolean oneStep = isNeighbours(to, me);
        if (oneStep) {
            where = me.direction(to);
        } else {
            where = safePathTo(board, me, to);
        }
        underMe = board.getAt(where.change(me));
        if (oneStep && to.willMark()) {
            return ACT.toString() + ',' + where.toString();
        }
        return where.toString();
    }

    private boolean isNeighbours(Point pt1, Point pt2) {
        return pt1.distance(pt2) <= 1.0D;
    }

    private boolean isFirstTurn(Board board) {
        return pt(1, 1).itsMe(me)
                && board.getAt(RIGHT.change(me)) == HIDDEN
                && board.getAt(UP.change(me)) == HIDDEN;
    }

    private Direction safePathTo(Board board, Point from, Action to) {
        DeikstraFindWay way = new DeikstraFindWay();
        way.getPossibleWays(board.size(), possible(board, to));

//        System.out.println(TestUtils.drawPossibleWays(3,
//                way.getBasic().toMap(),
//                board.size(),
//                pt -> board.getAt(pt).ch()));

        List<Direction> path = way.buildPath(from, Arrays.asList(to));
        if (path.isEmpty()) {
            throw new RuntimeException(); // TODO решить это
        }

        return path.get(0);
    }

    private DeikstraFindWay.Possible possible(Board board, Action to) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point point) {
                if (board.isAt(point, BORDER)) return false;

                // если на пути неизвестность
                if (board.isAt(point, HIDDEN)) {
                    // мы смотрим соседей, если там нет ни одной пустой клетки - идти опасно
                    if (QDirection.getValues().stream()
                            .map(direction -> direction.change(point))
                            .filter(pt -> !pt.isOutOf(board.size()))
                            .noneMatch(pt -> board.isAt(pt, NONE)
                                    // а тут мы не собираемся идти, а просто там флажок поставим
                                    || (point.equals(to) && to.willMark())
                                    // так же мы помним с прошлого хода, что под нами было
                                    || (underMe != null
                                        && pt.equals(board.getMe())
                                        && underMe == NONE))) return false;
                }

                return true;
            }
        };
    }

    private Action getClosest(List<Action> actions) {
        double min = 1.7976931348623157E308D; // TODO magic was here )
        Action result = null;

        for (Action action : actions) {
            if (action.equals(me)) continue;

            double distance = me.distance(action);
            if (distance < min) {
                min = distance;
                result = action;
            }
        }

        return result;
    }

    private int convert(char element) {
        if (ONE_MINE.ch() <= element && element <= EIGHT_MINES.ch()) {
            return Character.getNumericValue(element);
        }

        if (element == HIDDEN.ch()) {
            return HIDDEN_VALUE;
        }

        if (element == BORDER.ch()) {
            return BORDER_VALUE;
        }

        if (element == FLAG.ch()) {
            return FLAG_VALUE;
        }

        if (element == NONE.ch()) {
            return NONE_VALUE;
        }

        if (element == BANG.ch()) {
            return BANG_VALUE;
        }

        if (element == DETECTOR.ch()) {
            if (underMe == null || underMe == HIDDEN) {
                return DETECTOR_VALUE;
            } else {
                return convert(underMe.ch());
            }
        }

        return element;
    }
}