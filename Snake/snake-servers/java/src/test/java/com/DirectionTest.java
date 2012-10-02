package com;

import static com.Direction.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 10/2/12
 * Time: 12:10 AM
 */
public class DirectionTest {

    int x = 4;
    int y = 4;

    @Test
    public void shouldGoByInertion() {
        assertWay(path(LEFT, LEFT), from(6, y), to(4, y), LEFT);
        assertWay(path(RIGHT, RIGHT), from(4, y), to(6, y), RIGHT);
        assertWay(path(UP, UP), from(x, 4), to(x, 6), UP);
        assertWay(path(DOWN, DOWN), from(x, 6), to(x, 4), DOWN);
    }

    @Test
    public void shouldOneRotate() {
        assertWay(path(LEFT, LEFT), from(6, y), to(4, y), UP);
        assertWay(path(LEFT, LEFT), from(6, y), to(4, y), DOWN);

        assertWay(path(RIGHT, RIGHT), from(4, y), to(6, y), UP);
        assertWay(path(RIGHT, RIGHT), from(4, y), to(6, y), DOWN);

        assertWay(path(UP, UP), from(x, 4), to(x, 6), LEFT);
        assertWay(path(UP, UP), from(x, 4), to(x, 6), RIGHT);

        assertWay(path(DOWN, DOWN), from(x, 6), to(x, 4), LEFT);
        assertWay(path(DOWN, DOWN), from(x, 6), to(x, 4), RIGHT);
    }

    @Test
    public void shouldTwoRotate() {
        assertWay(path(LEFT, LEFT, UP, UP), from(4, 4), to(2, 6), DOWN);
        assertWay(path(RIGHT, UP, UP, LEFT), from(x, 4), to(x, 6), DOWN);
        assertWay(path(RIGHT, RIGHT, UP, UP), from(4, 4), to(6, 6), DOWN);

        assertWay(path(LEFT, LEFT, DOWN, DOWN), from(4, 4), to(2, 2), UP);
        assertWay(path(RIGHT, DOWN, DOWN, LEFT), from(x, 4), to(x, 2), UP);
        assertWay(path(RIGHT, RIGHT, DOWN, DOWN), from(4, 4), to(6, 2), UP);

        assertWay(path(DOWN, DOWN, RIGHT, RIGHT), from(4, 4), to(6, 2), LEFT);
        assertWay(path(UP, RIGHT, RIGHT, DOWN), from(4, y), to(6, y), LEFT);
        assertWay(path(UP, UP, RIGHT, RIGHT), from(4, 4), to(6, 6), LEFT);

        assertWay(path(DOWN, DOWN, LEFT, LEFT), from(4, 4), to(2, 2), RIGHT);
        assertWay(path(UP, LEFT, LEFT, DOWN), from(4, y), to(2, y), RIGHT);
        assertWay(path(UP, UP, LEFT, LEFT), from(4, 4), to(2, 6), RIGHT);
    }

    @Test
    public void shouldGetRoundIfBarrier() {
        assertWay(path(UP, LEFT, LEFT, DOWN), from(6, y), to(4, y), LEFT, barrier(5, y));
        assertWay(path(DOWN, RIGHT, RIGHT, UP), from(4, y), to(6, y), RIGHT, barrier(5, y));
        assertWay(path(RIGHT, UP, UP, LEFT), from(x, 4), to(x, 6), UP, barrier(x, 5));
        assertWay(path(LEFT, DOWN, DOWN, RIGHT), from(x, 6), to(x, 4), DOWN, barrier(x, 5));
    }

    @Test
    public void shouldGetRoundIfTwoBarriers() {
        assertWay(path(DOWN, LEFT, LEFT, UP), from(6, y), to(4, y), LEFT, barrier(5, y), barrier(6, y+1));
        assertWay(path(UP, RIGHT, RIGHT, DOWN), from(4, y), to(6, y), RIGHT, barrier(5, y), barrier(4, y-1));
        assertWay(path(RIGHT, UP, UP, LEFT), from(x, 4), to(x, 6), UP, barrier(x, 5), barrier(x-1, 4));
        assertWay(path(LEFT, DOWN, DOWN, RIGHT), from(x, 6), to(x, 4), DOWN, barrier(x, 5), barrier(x+1, 6));
    }

    @Test
    public void test() {
        assertWay(path(DOWN, DOWN, DOWN, DOWN, DOWN, DOWN),
                from(12,9), to(12,3), DOWN,
                barrier(2,4), barrier(12,9), barrier(12,10), barrier(11,10), barrier(10,10));
    }

    private Point barrier(int x, int y) {
        return to(x, y);
    }

    Path path(String... directions) {
        return new Path(directions);
    }

    class Path implements Iterable<String> {
        private List<String> directions;

        public Path(String ... directions) {
            this.directions = Arrays.asList(directions);
        }

        @Override
        public Iterator<String> iterator() {
            return directions.iterator();
        }

        @Override
        public String toString() {
            return directions.toString();
        }
    }

    private void assertWay(Path expectedPath, Point from, Point to, String direction, Point... barriers) {
        List<String> actualPath = new LinkedList<String>();
        do {
            String actual = new Direction(from, to, direction).get(Arrays.asList(barriers));
            actualPath.add(actual);

            if (actual.equals("")) {
                actual = direction;
            }
            from = update(from, actual);
            direction = actual;
        } while (!from.equals(to) && !from.isBad(20));

        assertEquals(expectedPath.toString(), actualPath.toString());
    }

    private Point update(Point from, String actual) {
        if (RIGHT.equals(actual)) {
            return new Point(from.x + 1, from.y);
        } else if (LEFT.equals(actual)) {
            return new Point(from.x - 1, from.y);
        } else if (UP.equals(actual)) {
            return new Point(from.x, from.y + 1);
        } else {
            return new Point(from.x, from.y - 1);
        }
    }

    private Point to(int x, int y) {
        return new Point(x, y);
    }

    private Point from(int x, int y) {
        return to(x, y);
    }
}
