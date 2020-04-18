package com.codenjoy.dojo.snake.client.ai;

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
import com.codenjoy.dojo.snake.client.Board;
import com.codenjoy.dojo.snake.model.Elements;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class AISolver implements Solver<Board> {

    private Dice dice;
    private DeikstraFindWay way;
    private DeikstraFindWay.Possible possible;
    private Board board;

    public AISolver(Dice dice) {
        this.dice = dice;
        this.way = new DeikstraFindWay();
    }

    public DeikstraFindWay.Possible possible(final Board board) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                int x = from.getX();
                int y = from.getY();
                if (isBarrierAt(x, y)) return false;

                Point newPt = where.change(from);
                int nx = newPt.getX();
                int ny = newPt.getY();

                if (board.isOutOfField(nx, ny)) return false;

                if (isBarrierAt(nx, ny)) return false;
                if (board.isAt(nx, ny,
                        Elements.HEAD_DOWN, Elements.HEAD_LEFT,
                        Elements.HEAD_UP, Elements.HEAD_RIGHT)) return false;

                return true;
            }

            @Override
            public boolean possible(Point atWay) {
                return true;
            }
        };
    }

    private boolean isBarrierAt(int x, int y) {
        return board.isAt(x, y, Elements.BREAK, Elements.BAD_APPLE,
//                Elements.HEAD_DOWN, Elements.HEAD_LEFT,
//                Elements.HEAD_UP, Elements.HEAD_RIGHT,
                Elements.TAIL_END_DOWN, Elements.TAIL_END_LEFT,
                Elements.TAIL_END_UP, Elements.TAIL_END_RIGHT,
                Elements.TAIL_HORIZONTAL, Elements.TAIL_VERTICAL,
                Elements.TAIL_LEFT_DOWN, Elements.TAIL_LEFT_UP,
                Elements.TAIL_RIGHT_DOWN, Elements.TAIL_RIGHT_UP);
    }

    @Override
    public String get(final Board board) {
        this.board = board;
        if (board.isGameOver()) return "";
        List<Direction> result = getWay();
        if (result.isEmpty()) return "";
        return result.get(0).toString();
    }


    public List<Direction> getWay() {
        possible = possible(board);

        Point from = board.getHead();
        List<Point> to = board.get(Elements.GOOD_APPLE);
        List<Direction> way = getWay(from, to);

        if (way.isEmpty()) {
            int distance = 0;
            Point longest = null;
            for (int x = 0; x < board.boardSize(); x++) {
                for (int y = 0; y < board.boardSize(); y++) {
                    if (isBarrierAt(x, y)) continue;
                    Point pt = pt(x, y);
                    way = this.way.getShortestWay(board.boardSize(), from, Arrays.asList(pt), possible);
                    if (distance < way.size()) {
                        distance = way.size();
                        longest = pt;
                    }
                }
            }
            way = getWay(from, Arrays.asList(longest));
        }

        return way;
    }

    private List<Direction> getWay(Point from, List<Point> to) {
        return this.way.getShortestWay(board.boardSize(), from, to, possible);
    }
}

