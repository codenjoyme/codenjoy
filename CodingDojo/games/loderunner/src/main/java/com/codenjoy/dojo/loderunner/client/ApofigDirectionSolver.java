package com.codenjoy.dojo.loderunner.client;

import com.codenjoy.dojo.loderunner.client.utils.Board;
import com.codenjoy.dojo.loderunner.client.utils.Dice;
import com.codenjoy.dojo.loderunner.client.utils.Point;

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

    public static class DeikstraFindWay {

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

        public void setupPossibleWays(Board field) {
            possibleWays.clear();

            for (int x = 0; x < field.size(); x++) {
                for (int y = 0; y < field.size(); y++) {
                    Point pt = new Point(x, y);
                    List<Direction> directions = new LinkedList<Direction>();
                    for (Direction direction : Arrays.asList(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)) {
                        if (isPossible(field, pt, direction)) {
                            directions.add(direction);
                        }
                    }
                    possibleWays.put(pt, directions);
                }
            }
        }

        public boolean isPossible(Board field, Point pt, Direction direction) {
            int x = pt.getX();
            int y = pt.getY();
            if (aWall(field, x, y)) return false;

            Point newPt = direction.change(pt);
            int nx = newPt.getX();
            int ny = newPt.getY();

            if (isOutOfField(field.size(), nx, ny)) return false;

            if (aWall(field, nx, ny)) return false;

            if (direction == Direction.UP && !aLadder(field, x, y)) return false;

            int yd = Direction.DOWN.changeY(y);
            if (direction != Direction.DOWN &&
                !isOutOfField(field.size(), x, yd) &&
                !aWall(field, x, yd) &&
                !aLadder(field, x, yd) &&
                !aLadder(field, x, y) &&
                !aPipe(field, x, y)) return false;

            return true;
        }

        private boolean aWall(Board field, int x, int y) {
            return field.isAt(x, y, Element.BRICK) || field.isAt(x, y, Element.UNDESTROYABLE_WALL);
        }

        private boolean aLadder(Board field, int x, int y) {
            return field.isAt(x, y, Element.LADDER) ||
                    field.isAt(x, y, Element.HERO_LADDER);
        }

        private boolean aPipe(Board field, int x, int y) {
            return field.isAt(x, y, Element.PIPE) ||
                    field.isAt(x, y, Element.HERO_PIPE_LEFT) ||
                    field.isAt(x, y, Element.HERO_PIPE_RIGHT);
        }

        private boolean isOutOfField(int size, int x, int y) {
            return x < 0 || y < 0 || x > size - 1 || y > size - 1;
        }
    }

}
