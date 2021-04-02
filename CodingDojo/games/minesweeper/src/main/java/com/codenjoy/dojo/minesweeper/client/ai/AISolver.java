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
import com.codenjoy.dojo.minesweeper.client.ai.logic.Field;
import com.codenjoy.dojo.minesweeper.client.ai.logic.PlayField;
import com.codenjoy.dojo.minesweeper.client.ai.logic.WaveField;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.*;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class AISolver implements Solver<Board> {

    private Dice dice;
    private Board board;
    private Point me;
    private int[][] field;
    private char movedTo;
    private List<Direction> safePath = new ArrayList();

    public AISolver(Dice dice) {
        this.dice = dice;
    }

    public String get(Board board) {
        String result;
        if (board.isGameOver()) {
            field = null;
            return STOP.toString();
        }
        this.board = board;

        if (isFirstTurn()) {
            return UP.toString();
        }

        if (field == null) {
            createField();
        }

        field = fillFieldWithBoard();
        Field field = new Field(new PlayField(this.field, 0));
        field.setMyCoord(me);

        try {
            field.play();
            Point[] pts = field.getToMark();
            Point[] toOpen = field.getToOpen();
            if (isOnJustMarked(pts) || movedTo == 42 && pts.length == 0 && field.getMinPossibility() > 0.0D) {
                result = getEscapeTo();
            } else {
                Map.Entry<Point, Boolean> closest = getClosest(toMap(pts, toOpen));
                if (closest != null) {
                    if (isNeighbours(closest.getKey(), me)) {
                        result = getAction(closest);
                    } else {
                        setSafePathTo(closest.getKey());
                        result = whereToGo();
                    }
                } else {
                    result = getEscapeTo();
                }
            }

            if (result.startsWith("ACT,") && movedTo == 45) {
                result = getEscapeTo();
            }

            if (result.startsWith("ACT,")) {
                movedTo = 45;
            } else {
                Point pt2 = Direction.valueOf(result).change(board.getMe());
                movedTo = board.getAt(pt2).ch();
            }
        } catch (Exception e) {
            result = getEscapeTo();
        }

        return result;
    }

    private String whereToGo() {
        Collections.sort(safePath);
        return safePath.remove(0).toString();
    }

    private boolean isNeighbours(Point pt1, Point pt2) {
        return pt1.distance(pt2) <= 1.0D;
    }

    private boolean isFirstTurn() {
        return board.getAt(1, board.size() - 3).ch() == 42
                && board.getAt(2, board.size() - 2).ch() == 42;
    }

    private String getEscapeTo() {
        int width = field.length;
        int height = field[0].length;
        if (me.getX() > 0
                && field[me.getX() - 1][me.getY()] != 9)
        {
            return LEFT.toString();
        }

        if (me.getX() < width - 1
                && field[me.getX() + 1][me.getY()] != 9)
        {
            return RIGHT.toString();
        }

        if (me.getY() > 0
                && field[me.getX()][me.getY() - 1] != 9)
        {
            return UP.toString();
        }

        if (me.getY() < height - 1
                && field[me.getX()][me.getY() + 1] != 9)
        {
            return DOWN.toString();
        }

        return null;
    }

    private void createField() {
        field = new int[board.size() - 2][board.size() - 2];
        for (int i = 0; i < field.length; ++i) {
            for (int j = 0; j < field[i].length; ++j) {
                field[i][j] = 9;
            }
        }
    }

    private boolean isOnJustMarked(Point[] toMark) {
        for (int i = 0; i < toMark.length; ++i) {
            Point point = toMark[i];
            if (point.equals(me)) {
                return true;
            }
        }
        return false;
    }

    private String getAction(Map.Entry<Point, Boolean> dest) {
        int dx = (dest.getKey()).getX() - me.getX();
        int dy = (dest.getKey()).getY() - me.getY();
        String result;
        Point neighbour;
        if (Math.abs(dx) > Math.abs(dy)) {
            int signumX = (int) Math.signum(dx);
            neighbour = pt(me.getX() + signumX, me.getY());
            if (isFree(neighbour) && !neighbour.equals(dest.getKey())) {
                result = getDirectionBydY(dy);
            } else {
                result = getDirectionBydX(dx);
            }
        } else {
            int signumY = (int) Math.signum(dy);
            neighbour = pt(me.getX(), me.getY() + signumY);
            if (isFree(neighbour) && !neighbour.equals(dest.getKey())) {
                result = getDirectionBydX(dx);
            } else {
                result = getDirectionBydY(dy);
            }
        }

        if (neighbour.equals(dest.getKey()) && !dest.getValue()) {
            result = ACT.toString() + ',' + result;
        }

        return result;
    }

    private boolean isFree(Point pt) {
        return field[pt.getX()][pt.getY()] == 9;
    }

    private void setSafePathTo(Point target) {
        WaveField waveField = new WaveField(board);
        target = pt(target.getX() + 1, target.getY() + 1);
        safePath = waveField.findWay(target);
    }

    private String getDirectionBydX(int dx) {
        if (dx > 0) {
            return RIGHT.toString();
        } else if (dx < 0) {
            return LEFT.toString();
        } else {
            return Direction.valueOf(dice.next(2)).toString();
        }
    }

    private String getDirectionBydY(int dy) {
        if (dy < 0) {
            return UP.toString();
        } else if (dy > 0) {
            return DOWN.toString();
        } else {
            return Direction.valueOf(dice.next(2) + 2).toString();
        }
    }

    private Map<Point, Boolean> toMap(Point[] toMark, Point[] toOpen) {
        HashMap result = new HashMap();

        for (int i = 0; i < toMark.length; ++i) {
            result.put(toMark[i], false);
        }

        for (int i = 0; i < toOpen.length; ++i) {
            result.put(toOpen[i], true);
        }

        return result;
    }

    private Map.Entry<Point, Boolean> getClosest(Map<Point, Boolean> points) {
        double min = 1.7976931348623157E308D;
        Map.Entry result = null;

        for (Map.Entry<Point, Boolean> entry : points.entrySet()) {
            if (entry.getKey().equals(me)) continue;
            double distance = me.distance(entry.getKey());
            if (distance < min) {
                min = distance;
                result = entry;
            }
        }

        return result;
    }

    private int[][] fillFieldWithBoard() {
        int[][] result = new int[board.size() - 2][board.size() - 2];

        for (int i = 0; i < result.length; ++i) {
            for (int j = 0; j < result[i].length; ++j) {
                char element = board.getAt(i + 1, j + 1).ch();
                if (element > 48 && element < 57) {
                    result[i][j] = Character.getNumericValue(element);
                } else if (element == 42) {
                    result[i][j] = 9;
                } else if (element == 8252) {
                    result[i][j] = 11;
                } else if (element == 32) {
                    result[i][j] = 0;
                } else if (element == 1120) {
                    result[i][j] = 12;
                } else if (element == 9786) {
                    me = pt(i, j);
                    result[i][j] = field[i][j];
                }
            }
        }

        return result;
    }
}