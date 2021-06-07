package com.codenjoy.dojo.battlecity.services.ai;

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


import com.codenjoy.dojo.games.battlecity.Board;
import com.codenjoy.dojo.games.battlecity.Element;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.List;

public class AISolver implements Solver<Board> {

    private DeikstraFindWay way;

    public AISolver(Dice dice) {
        this.way = new DeikstraFindWay();
        // this.way = new DeikstraFindWay(true); // TODO #768 этот подход должен быть идентичным
    }

    public DeikstraFindWay.Possible withBarriers(Board board) {
        List<Point> barriers = board.getBarriers();

        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point point) {
                return !barriers.contains(point);
            }
        };
    }

    public DeikstraFindWay.Possible withBullets(Board board) {
        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point from, Direction where) {
                Point to = where.change(from);
                if (board.isBulletAt(to.getX(), to.getY())) return false;

                return true;
            }
        };
    }

    public DeikstraFindWay.Possible withBarriersAndBullets(Board board) {
        List<Point> barriers = board.getBarriers();
        List<Point> bullets = board.getBullets();
        List<Point> river = board.getRiver();

        return new DeikstraFindWay.Possible() {
            @Override
            public boolean possible(Point point) {
                if (barriers.contains(point)) return false;
                if (river.contains(point)) return false;
                return true;
            }

            @Override
            public boolean possible(Point from, Direction where) {
                Point to = where.change(from);
                if (bullets.contains(to)) return false;

                return true;
            }
        };
    }

    @Override
    public String get(Board board) {
        if (board.isGameOver()) return act("");
        List<Direction> result = getDirections(board);
        if (result.isEmpty()) return act("");
        return act(result.get(0).toString());
    }

    private String act(String command) {
        return ((command.equals("") ? "" : command + ", ") + "ACT");
    }

    public List<Direction> getDirections(Board board) {
        int size = board.size();
        Point from = board.getMe();
        List<Point> to = board.get(
                Element.AI_TANK_DOWN,
                Element.AI_TANK_LEFT,
                Element.AI_TANK_RIGHT,
                Element.AI_TANK_UP,
                Element.OTHER_TANK_DOWN,
                Element.OTHER_TANK_LEFT,
                Element.OTHER_TANK_RIGHT,
                Element.OTHER_TANK_UP);

        // TODO #768 этот подход должен быть идентичным
        // way.getPossibleWays(size, withBarriers(board));
        // way.updateWays(withBullets(board));
        way.getPossibleWays(size, withBarriersAndBullets(board));

        return way.buildPath(from, to);
    }

}
