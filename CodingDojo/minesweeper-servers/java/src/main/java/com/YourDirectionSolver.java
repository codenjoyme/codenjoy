package com;

import com.utils.BoardImpl;
import com.utils.Dice;
import com.utils.Point;

import java.util.*;

import static com.Element.*;
import static com.Direction.*;

/**
 * User: your name
 */
public class YourDirectionSolver implements DirectionSolver {

    private Dice dice;
    static List<Direction> path = new LinkedList<Direction>();
    private BoardImpl board;

    public YourDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(BoardImpl board) {
        this.board = board;
        if (board.isGameOver()) return "";

        if (path.isEmpty()) {

            if (nearFog()) {
                next(UP);
            } else {
                List<Point> free = findFree();
                if (free.isEmpty()) {
                    next(UP);
                } else {
                    Point point = findNear(board.getMe(), free);
                    next(buildPath(board.getMe(), point));
                }
            }
        }

        Direction direction = path.remove(0);
//        if (direction == UP && atTop() == BORDER) direction = LEFT;
//        if (direction == LEFT && atLeft() == BORDER) direction = DOWN;
//        if (direction == DOWN && atBottom() == BORDER) direction = RIGHT;
//        if (direction == RIGHT && atRight() == BORDER) direction = UP;
        return direction.toString();
    }

    private Direction[] buildPath(Point from, Point to) {
        int dx = from.getX() - to.getX();
        int dy = from.getY() - to.getY();

        List<Direction> result = new LinkedList<Direction>();

        while (dx != 0) {
            if (dx < 0) {
                result.add(RIGHT);
                dx++;
            } else if (dx > 0) {
                result.add(LEFT);
                dx--;
            }
        }
        while (dy != 0) {
            if (dy < 0) {
                result.add(DOWN);
                dy++;
            } else if (dy > 0) {
                result.add(UP);
                dy--;
            }
        }
        return result.toArray(new Direction[0]);
    }

    private Point findNear(final Point me, List<Point> free) {
        Collections.sort(free, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                return getPath(o1, me).compareTo(getPath(o2, me));
            }
        });
        return free.get(0);
    }

    private Double getPath(Point o1, Point me) {
        return Math.sqrt(Math.pow(Math.abs(o1.getX() - me.getX()), 2) + Math.pow(Math.abs(o1.getY() - me.getY()), 2));
    }

    private List<Point> findFree() {
        List<Point> result = new LinkedList<Point>();
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                if (board.isAt(x, y, NO_MINE)) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            if (board.isAt(x + dx, y + dy, HIDDEN)) {
                                if (x + dx < 0 || x + dy >= board.size() || y + dy < 0 || y + dy >= board.size()) continue;
                                Point fog = new Point(x + dx, y + dy);
                                if (!result.contains(fog)) {
                                    result.add(fog);
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    private boolean nearFog() {
        Point me = board.getMe();
        boolean result = true;
        for (int dx = -1; dx <= 1; dx ++) {
            for (int dy = -1; dy <= 1; dy ++) {
                if (dx == 0 && dy == 0) continue;
                result &= board.isAt(me.getX() + dx, me.getY() + dy, HIDDEN);
            }
        }
        return result;
    }

    private Element atTop() {
        Point me = board.getMe();
        return board.getAt(me.getX(), me.getY() - 1);
    }

    private Element atBottom() {
        Point me = board.getMe();
        return board.getAt(me.getX(), me.getY() + 1);
    }

    private Element atLeft() {
        Point me = board.getMe();
        return board.getAt(me.getX() - 1, me.getY());
    }

    private Element atRight() {
        Point me = board.getMe();
        return board.getAt(me.getX() + 1, me.getY());
    }

    private void next(Direction... direstions) {
        path.addAll(Arrays.asList(direstions));
    }

    private Direction randomDirection() {
        return Direction.valueOf(dice.next(4));
    }

}
