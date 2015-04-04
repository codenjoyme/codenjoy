package com.codenjoy.dojo.services.algs;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.*;

public class DeikstraFindWay {

    private static final List<Direction> DIRECTIONS = Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
    private Map<Point, List<Direction>> possibleWays;
    private int size;
    private Possible possible;

    public static interface Possible {
        boolean possible(Point from, Direction direction);
        boolean possible(Point atWay);
    }

    public DeikstraFindWay() {
    }

    public List<Direction> getShortestWay(int size, Point from, List<Point> goals, Possible possible) {
        this.size = size;
        this.possible = possible;
        setupPossibleWays();

        List<List<Direction>> paths = new LinkedList<List<Direction>>();
        for (Point to : goals) {
            List<Direction> path = getPath(from).get(to);
            if (path.isEmpty()) continue;
            paths.add(path);
        }

        int minDistance = Integer.MAX_VALUE;
        int indexMin = 0;
        for (int index = 0; index < paths.size(); index++) {
            List<Direction> path = paths.get(index);
            if (minDistance > path.size()) {
                minDistance = path.size();
                indexMin = index;
            }
        }

        if (paths.isEmpty()) return Arrays.asList();
        List<Direction> shortest = paths.get(indexMin);
        if (shortest.size() == 0) return Arrays.asList();

        return shortest;
    }

    private Map<Point, List<Direction>> getPath(Point from) {
        Map<Point, List<Direction>> path = new HashMap<Point, List<Direction>>();
        for (Point point : possibleWays.keySet()) {
            path.put(point, new LinkedList<Direction>());
        }

        boolean[][] processed = new boolean[size][size];
        LinkedList<Point> toProcess = new LinkedList<Point>();

        Point current = from;
        do {
            if (current == null) {
                current = toProcess.remove();
            }
            List<Direction> before = path.get(current);
            for (Direction direction : possibleWays.get(current)) {
                Point to = direction.change(current);
                if (possible != null) {
                    if (!possible.possible(to)) continue;
                }
                if (processed[to.getX()][to.getY()]) continue;

                List<Direction> directions = path.get(to);
                if (directions.isEmpty() || directions.size() > before.size() + 1) {
                    directions.addAll(before);
                    directions.add(direction);

                    if (!processed[to.getX()][to.getY()]) {
                        toProcess.add(to);
                    }
                }
            }
            processed[current.getX()][current.getY()] = true;
            current = null;
        } while (!toProcess.isEmpty());

        return path;
    }

    private void setupPossibleWays() {
        possibleWays = new TreeMap<Point, List<Direction>>();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point from = new PointImpl(x, y);
                List<Direction> directions = new LinkedList<Direction>();
                for (Direction direction : DIRECTIONS) {
                    if (possible.possible(from, direction)) {
                        directions.add(direction);
                    }
                }
                possibleWays.put(from, directions);
            }
        }
    }

    public Map<Point, List<Direction>> getPossibleWays() {
        return possibleWays;
    }
}