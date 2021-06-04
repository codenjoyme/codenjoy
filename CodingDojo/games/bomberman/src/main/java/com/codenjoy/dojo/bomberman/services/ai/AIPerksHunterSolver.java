package com.codenjoy.dojo.bomberman.services.ai;

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


import com.codenjoy.dojo.games.bomberman.Board;
import com.codenjoy.dojo.games.bomberman.Element;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

    public static DeikstraFindWay.Possible possible(Board board) {
        Collection<Point> futureBlasts = board.getFutureBlasts();
        Collection<Point> meatChoppers = board.getMeatChoppers();
        Collection<Point> barriers = board.getBarriers();

        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                Point to = where.change(from);
                if (futureBlasts.contains(to)) return false;
                if (meatChoppers.contains(to)) return false;
                return true;
            }

            @Override
            public boolean possible(Point point) {
                return !barriers.contains(point);
            }
        };
    }

    public List<Direction> getDirections(Board board) {
        int size = board.size();
        Point from = board.getBomberman();
        List<Point> to = board.get(
                Element.BOMB_BLAST_RADIUS_INCREASE,
                Element.BOMB_COUNT_INCREASE,
                Element.BOMB_IMMUNE,
                Element.BOMB_REMOTE_CONTROL);
        if (to.isEmpty()) {
            return Arrays.asList();
        }
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

    public static void main(String[] args) {
        WebSocketRunner.runClient(args,
                // paste here board page url from browser after registration
                // or put it as command line parameter
                "http://127.0.0.1:8080/codenjoy-contest/board/player/anyidyouwant?code=12345678901234567890",
                new AISolver(new RandomDice()),
                new Board());
    }
}
