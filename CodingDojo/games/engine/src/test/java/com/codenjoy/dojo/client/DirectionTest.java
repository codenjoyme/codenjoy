package com.codenjoy.dojo.client;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 21:29
 */
public class DirectionTest {

    @Test
    public void test_valueOfIndex() {
        assertEquals("LEFT", Direction.valueOf(0).toString());
        assertEquals("RIGHT", Direction.valueOf(1).toString());
        assertEquals("UP", Direction.valueOf(2).toString());
        assertEquals("DOWN", Direction.valueOf(3).toString());
    }

    @Test
    public void test_valueOfName() {
        assertEquals(0, Direction.valueOf("LEFT").value());
        assertEquals(1, Direction.valueOf("RIGHT").value());
        assertEquals(2, Direction.valueOf("UP").value());
        assertEquals(3, Direction.valueOf("DOWN").value());
    }

    @Test
    public void test_inverted() {
        assertEquals(Direction.RIGHT, Direction.LEFT.inverted());
        assertEquals(Direction.LEFT, Direction.RIGHT.inverted());
        assertEquals(Direction.DOWN, Direction.UP.inverted());
        assertEquals(Direction.UP, Direction.DOWN.inverted());
    }

    @Test
    public void test_random() {
        int[] c = new int[4];
        for (int count = 0; count < 1200; count ++) {
            c[Direction.random().value()]++;
        }
        String message = Arrays.toString(c);
        assertTrue(message, c[0] > 250);
        assertTrue(message, c[1] > 250);
        assertTrue(message, c[2] > 250);
        assertTrue(message, c[3] > 250);
    }

    @Test
    public void test_change() {
        check(new PointImpl(0, 1), Direction.DOWN, new PointImpl(0, 0));
        check(new PointImpl(-1, 0), Direction.LEFT, new PointImpl(0, 0));
        check(new PointImpl(0, -1), Direction.UP,   new PointImpl(0, 0));
        check(new PointImpl(1, 0), Direction.RIGHT, new PointImpl(0, 0));
    }

    @Test
    public void test_changePoint() {
        assertEquals("[1,0]", Direction.RIGHT.change(pt(0, 0)).toString());
        assertEquals("[-1,0]", Direction.LEFT.change(pt(0, 0)).toString());
        assertEquals("[0,-1]", Direction.UP.change(pt(0, 0)).toString());
        assertEquals("[0,1]", Direction.DOWN.change(pt(0, 0)).toString());
    }

    private void check(Point expected, Direction direction, Point input) {
        assertEquals(expected.getX(), direction.changeX(input.getX()));
        assertEquals(expected.getY(), direction.changeY(input.getY()));
        assertEquals(expected, direction.change(input));
    }
}
