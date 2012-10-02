package com;

import static com.Direction.*;
import com.Point;
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
        assertWay(from(6, y), to(4, y), LEFT, path(LEFT, LEFT));
        assertWay(from(4, y), to(6, y), RIGHT, path(RIGHT, RIGHT));
        assertWay(from(x, 4), to(x, 6), UP, path(UP, UP));
        assertWay(from(x, 6), to(x, 4), DOWN, path(DOWN, DOWN));
    }

    @Test
    public void shouldOneRotate() {
        assertWay(from(6, y), to(4, y), UP, path(LEFT, LEFT));
        assertWay(from(6, y), to(4, y), DOWN, path(LEFT, LEFT));

        assertWay(from(4, y), to(6, y), UP, path(RIGHT, RIGHT));
        assertWay(from(4, y), to(6, y), DOWN, path(RIGHT, RIGHT));

        assertWay(from(x, 4), to(x, 6), LEFT, path(UP, UP));
        assertWay(from(x, 4), to(x, 6), RIGHT, path(UP, UP));

        assertWay(from(x, 6), to(x, 4), LEFT, path(DOWN, DOWN));
        assertWay(from(x, 6), to(x, 4), RIGHT, path(DOWN, DOWN));
    }

    @Test
    public void shouldTwoRotate() {
        assertWay(from(4, 4), to(2, 6), DOWN, path(LEFT, "", UP, UP));
        assertWay(from(x, 4), to(x, 6), DOWN, path(RIGHT, UP, "", LEFT));
        assertWay(from(4, 4), to(6, 6), DOWN, path(RIGHT, "", UP, UP));

        assertWay(from(4, 4), to(2, 2), UP, path(LEFT, "", DOWN, DOWN));
        assertWay(from(x, 4), to(x, 2), UP, path(RIGHT, DOWN, "", LEFT));
        assertWay(from(4, 4), to(6, 2), UP, path(RIGHT, "", DOWN, DOWN));

        assertWay(from(4, 4), to(6, 2), LEFT, path(DOWN, "", RIGHT, RIGHT));
        assertWay(from(4, y), to(6, y), LEFT, path(UP, RIGHT, "", DOWN));
        assertWay(from(4, 4), to(6, 6), LEFT, path(UP, "", RIGHT, RIGHT));

        assertWay(from(4, 4), to(2, 2), RIGHT, path(DOWN, "", LEFT, LEFT));
        assertWay(from(4, y), to(2, y), RIGHT, path(UP, LEFT, "", DOWN));
        assertWay(from(4, 4), to(2, 6), RIGHT, path(UP, "", LEFT, LEFT));
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

    private void assertWay(Point from, Point to, String direction, Path expectedPath) {
        List<String> actualPath = new LinkedList<String>();
        do {
            String actual = new Direction(from, to, direction).get();
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
        return from(x, y);
    }

    private Point from(int x, int y) {
        return new Point(x, y);
    }
}
