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
import com.codenjoy.dojo.minesweeper.client.ai.logic.Cell;
import com.codenjoy.dojo.minesweeper.client.ai.logic.Field;
import com.codenjoy.dojo.minesweeper.model.Elements;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.codenjoy.dojo.minesweeper.client.ai.logic.Action.*;
import static com.codenjoy.dojo.minesweeper.model.Elements.*;
import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class AISolver implements Solver<Board> {

    private Point me;
    private Elements underMe;
    private Direction where;
    private Dice dice;

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    public String get(Board board) {
        if (board.isGameOver() || board.isWin()) {
            return STOP.toString();
        }

        me = board.getMe();

        if (isFirstTurn(board)) {
            return UP.toString();
        }

        Field field = new Field(board.size());
        field.scan(pt -> convert(board.getAt(pt)));
        Collection<Cell> actions = field.actions();
        if (actions.isEmpty()) {
            // не знаем куда походить - надо сделать шаг назад
            // если на прошлом тике в месте куда я пришел было '*'
            // это может подсказать что было подомной
            if (underMe == HIDDEN && where != null) {
                where = where.inverted();
            } else {
                // а тут уже надо рисковать )
                // TODO решить это
                return STOP.toString();
            }
        } else {
            Cell to = getClosest(actions);
            boolean oneStep = isNeighbours(to, me);
            if (oneStep) {
                where = me.direction(to);
            } else {
                where = safePathTo(board, me, to);
                if (where == null) {
                    return STOP.toString();
                }
            }
            if (oneStep && to.action() == MARK) {
                return ACT.toString() + ',' + where.toString();
            }
        }
        underMe = board.getAt(where.change(me));
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

    private Direction safePathTo(Board board, Point from, Cell to) {
        DeikstraFindWay way = new DeikstraFindWay();
        way.getPossibleWays(board.size(), possible(board, to));

        // для отладки возможных путей
        // System.out.println(TestUtils.drawPossibleWays(3,
        //        way.getBasic().toMap(),
        //        board.size(),
        //        pt -> board.getAt(pt).ch()));

        List<Direction> path = way.buildPath(from, Arrays.asList(to));
        if (path.isEmpty()) {
            // TODO решить это
            return null;
        }

        return path.get(0);
    }

    private DeikstraFindWay.Possible possible(Board board, Cell to) {
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
                                    || (point.equals(to) && to.action() == MARK)
                                    // так же мы помним с прошлого хода, что под нами было
                                    || (underMe != null
                                        && pt.equals(board.getMe())
                                        && underMe == NONE))) return false;
                }

                return true;
            }
        };
    }

    private Cell getClosest(Collection<Cell> actions) {
        double min = 1.7976931348623157E308D; // TODO magic was here )
        Cell result = null;

        for (Cell action : actions) {
            if (action.equals(me)) continue;

            double distance = me.distance(action);
            if (distance < min) {
                min = distance;
                result = action;
            }
        }

        return result;
    }

    private Elements convert(Elements element) {
        if (element == DETECTOR) {
            if (underMe == null || underMe == HIDDEN) {
                return element;
            } else {
                return convert(underMe);
            }
        }

        return element;
    }
}