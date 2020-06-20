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
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class AIPerksHunterSolver implements Solver<Board> {

    private DeikstraFindWay way;
    private Direction direction;
    private Point bomb;
    private Dice dice;
    private Board board;

    public AIPerksHunterSolver(Dice dice) {
        this.dice = dice;
        this.way = new DeikstraFindWay();
    }

    public DeikstraFindWay.Possible possible(final Board board) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                int x = from.getX();
                int y = from.getY();
                if (board.isBarrierAt(x, y)) return false;

                Point newPt = where.change(from);
                int nx = newPt.getX();
                int ny = newPt.getY();

                if (board.isOutOfField(nx, ny)) return false;

                if (board.isBarrierAt(nx, ny)) return false;
                if (board.getFutureBlasts().contains(newPt)) return false;
                if (board.getMeatChoppers().contains(newPt)) return false;

                return true;
            }

            @Override
            public boolean possible(Point atWay) {
                return true;
            }
        };
    }

    public List<Direction> getDirections(Board board) {
        int size = board.size();
        Point from = board.getBomberman();
        List<Point> to = board.get(
                Elements.BOMB_BLAST_RADIUS_INCREASE,
                Elements.BOMB_COUNT_INCREASE,
                Elements.BOMB_IMMUNE,
                Elements.BOMB_REMOTE_CONTROL);
        DeikstraFindWay.Possible map = possible(board);
        return way.getShortestWay(size, from, to, map);
    }

    @Override
    public String get(Board board) {
        if (board.isMyBombermanDead()) return Direction.STOP.toString();
        List<Direction> result = getDirections(board);
        if (result.isEmpty()) return Direction.ACT.toString();
        return result.get(0).toString();
    }
}
