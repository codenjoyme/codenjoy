package com.codenjoy.dojo.snake.services.ai;

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
import com.codenjoy.dojo.games.snake.Board;
import com.codenjoy.dojo.games.snake.Element;

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
            @Override // TODO test me
            public boolean possible(Point from, Direction where) {
                Point to = where.change(from);

                if (board.isAt(to.getX(), to.getY(),
                        Element.HEAD_DOWN, Element.HEAD_LEFT,
                        Element.HEAD_UP, Element.HEAD_RIGHT)) return false;

                return true;
            }

            @Override
            public boolean possible(Point point) {
                return !isBarrierAt(point.getX(), point.getY());
            }
        };
    }

    private boolean isBarrierAt(int x, int y) {
        return board.isAt(x, y, Element.BREAK, Element.BAD_APPLE,
//                Element.HEAD_DOWN, Element.HEAD_LEFT,
//                Element.HEAD_UP, Element.HEAD_RIGHT,
                Element.TAIL_END_DOWN, Element.TAIL_END_LEFT,
                Element.TAIL_END_UP, Element.TAIL_END_RIGHT,
                Element.TAIL_HORIZONTAL, Element.TAIL_VERTICAL,
                Element.TAIL_LEFT_DOWN, Element.TAIL_LEFT_UP,
                Element.TAIL_RIGHT_DOWN, Element.TAIL_RIGHT_UP);
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
        List<Point> to = board.get(Element.GOOD_APPLE);
        List<Direction> way = getWay(from, to);

        if (way.isEmpty()) {
            int distance = 0;
            Point longest = null;
            for (int x = 0; x < board.size(); x++) {
                for (int y = 0; y < board.size(); y++) {
                    if (isBarrierAt(x, y)) continue;
                    Point pt = pt(x, y);
                    way = this.way.getShortestWay(board.size(), from, Arrays.asList(pt), possible);
                    if (distance <= way.size()) {
                        distance = way.size();
                        longest = pt;
                    }
                }
            }
            if (longest != null) {
                way = getWay(from, Arrays.asList(longest));
            }
        }

        return way;
    }

    private List<Direction> getWay(Point from, List<Point> to) {
        return this.way.getShortestWay(board.size(), from, to, possible);
    }
}

