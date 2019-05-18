package com.codenjoy.dojo.battlecity.client.ai;

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


import com.codenjoy.dojo.battlecity.client.Board;
import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Path;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.*;
import java.util.function.Consumer;

public class AISolver implements Solver<Board> {
    private Dice dice;
    private Board board;
    private List<Path> closedList = new ArrayList<>();


    public AISolver(Dice dice) {
        this.dice = dice;

    }


    @Override
    public String get(final Board board) {
        this.board = board;
        if (board.isGameOver()) return "";
        Direction direction = Direction.UP;
        List<Point> bullets = this.board.getBullets();
        final Point me = this.board.getMe();
        Collections.sort(
                bullets, new ShortestDistanceComparator(me));
        for (Point point : bullets) {
            List<Path> openList = new ArrayList<>();
            closedList = new ArrayList<>();
            addToOpenList(new Path(me, null, null), openList, closedList);
            closedList.add(new Path(me, null, null));
            if (traverse(openList, closedList, point)) {
                Path path = closedList.get(closedList.indexOf(new Path(point, null, null)));
                while (!path.getParent().getPoint().equals(me)) {
                    path = path.getParent();
                }
                direction = path.getDirection();
                break;
            }
        }
        return direction.toString();
    }


    private boolean traverse(List<Path> openList, List<Path> closedList, Point destination) {
        if (!openList.isEmpty()) {
            Collections.sort(openList, new ShortestPathComparator(destination));
            Path minDistancePoint = openList.get(0);
            closedList.add(minDistancePoint);
            if (minDistancePoint.getPoint().itsMe(destination)) {
                return true;
            }
            openList.remove(minDistancePoint);
            addToOpenList(minDistancePoint, openList, closedList);
            return traverse(openList, closedList, destination);
        }
        return false;
    }

    private void addToOpenList(Path me, List<Path> openList, List<Path> closedList) {
        directionTraversable(me.getPoint(), Direction.UP).ifPresent(getPointConsumer(openList, closedList, me, Direction.UP));
        directionTraversable(me.getPoint(), Direction.DOWN).ifPresent(getPointConsumer(openList, closedList, me, Direction.DOWN));
        directionTraversable(me.getPoint(), Direction.LEFT).ifPresent(getPointConsumer(openList, closedList, me, Direction.LEFT));
        directionTraversable(me.getPoint(), Direction.RIGHT).ifPresent(getPointConsumer(openList, closedList, me, Direction.RIGHT));
    }

    private Consumer<Point> getPointConsumer(List<Path> openList, List<Path> closedList, Path parent, Direction direction) {
        return point -> {
            if (!openList.contains(new Path(point, null, null)) && !closedList.contains(new Path(point, null, null)))
                openList.add(new Path(point, parent, direction));
        };
    }

    private Optional<Point> directionTraversable(Point from, Direction towards) {
        Point destination = from.copy();
        destination.change(towards);
        return !this.board.isBarrierAt(destination.getX(), destination.getY()) ? Optional.of(destination) : Optional.empty();
    }


    class ShortestDistanceComparator implements Comparator<Point> {
        private Point origin;
        public ShortestDistanceComparator(Point origin) {
            this.origin = origin;
        }
        public int compare(Point p1, Point p2) {
            return (p1 == null) ? 1 : ((p2 == null) ? -1 : origin.distance(p1) > origin.distance(p2) ? 1 : -1);
        }
    }

    class ShortestPathComparator implements Comparator<Path> {
        private Point origin;
        public ShortestPathComparator(Point origin) {
            this.origin = origin;
        }
        public int compare(Path p1, Path p2) {
            return (p1 == null) ? 1 : ((p2 == null) ? -1 : origin.distance(p1.getPoint()) > origin.distance(p2.getPoint()) ? 1 : -1);
        }
    }
}
