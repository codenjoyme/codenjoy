package com;

import com.utils.Board;
import com.utils.Dice;
import com.utils.Point;

import java.util.*;

/**
 * User: your name
 */
public class ApofigDirectionSolver implements DirectionSolver {

    private Dice dice;
    private DeikstraFindWay way;

    public ApofigDirectionSolver(Dice dice) {
        this.dice = dice;
        this.way = new DeikstraFindWay();
    }

    @Override
    public String get(Board board) {
        List<Direction> way = getShortestWay(board);
        if (way.isEmpty()) return "";
        return way.get(0).toString();
    }

    public List<Direction> getShortestWay(Board board) {
        way.setupPossibleWays(board);
        if (board.isGameOver()) return Arrays.asList();

        Point me = board.getMe();
        List<List<Direction>> paths = new LinkedList<List<Direction>>();
        for (Point pt : board.get(Element.GOLD)) {
            List<Direction> path = way.getPath(board, me, pt);
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

    class DeikstraFindWay {

        Map<Point, List<Direction>> possibleWays = new TreeMap<Point, List<Direction>>();

        List<Direction> getPath(Board board, Point from, Point to) {
            return getPath(board, from).get(to);
        }

        private Map<Point, List<Direction>> getPath(Board board, Point from) {
            Map<Point, List<Direction>> path = new HashMap<Point, List<Direction>>();
            for (Point point : possibleWays.keySet()) {
                path.put(point, new LinkedList<Direction>());
            }

            boolean[][] processed = new boolean[board.size()][board.size()];
            LinkedList<Point> toProcess = new LinkedList<Point>();

            Point current = from;
            do {
                if (current == null) {
                    current = toProcess.remove();
                }
                List<Direction> before = path.get(current);
                for (Direction direction : possibleWays.get(current)) {
                    Point to = direction.change(current);
                    if (board.isEnemyAt(to.getX(), to.getY())) continue;
                    if (board.isOtherHeroAt(to.getX(), to.getY())) continue;
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

        void setupPossibleWays(Board field) {
            possibleWays.clear();

            for (int x = 0; x < field.size(); x++) {
                for (int y = 0; y < field.size(); y++) {
                    Point pt = new Point(x, y);
                    List<Direction> directions = new LinkedList<Direction>();
                    for (Direction direction : Direction.values()) {
                        if (isPossible(field, pt, direction)) {
                            directions.add(direction);
                        }
                    }
                    possibleWays.put(pt, directions);
                }
            }
        }

        private boolean isPossible(Board field, Point pt, Direction direction) {
            if (field.isAt(pt.getX(), pt.getY(), Element.BRICK) || field.isAt(pt.getX(), pt.getY(), Element.UNDESTROYABLE_WALL)) return false;

            Point newPt = direction.change(pt);
            int x = newPt.getX();
            int y = newPt.getY();

            if (isOutOfField(field.size(), x, y)) return false;

            if (field.isAt(x, y, Element.BRICK) || field.isAt(x, y, Element.UNDESTROYABLE_WALL)) return false;

            if (direction == Direction.UP && !field.isAt(pt.getX(), pt.getY(), Element.LADDER)) return false;

            if (!isOutOfField(field.size(), pt.getX(), pt.getY() - 1) &&
                    !field.isAt(pt.getX(), pt.getY() - 1, Element.BRICK) &&
                    !field.isAt(pt.getX(), pt.getY() - 1, Element.LADDER) &&
                    !field.isAt(pt.getX(), pt.getY() - 1, Element.UNDESTROYABLE_WALL) &&
                    !field.isAt(pt.getX(), pt.getY(), Element.LADDER) &&
                    !field.isAt(pt.getX(), pt.getY(), Element.PIPE) &&
                    direction != Direction.DOWN) return false;

            return true;
        }

        private boolean isOutOfField(int size, int x, int y) {
            return x < 0 || y < 0 || x > size - 1 || y > size - 1;
        }
    }

}
