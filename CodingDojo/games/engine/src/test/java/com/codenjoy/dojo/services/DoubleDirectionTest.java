package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class DoubleDirectionTest {

    public static final int N = 250;

    @Test
    public void test_valueOfIndex() {
        assertEquals("LEFT", DoubleDirection.valueOf(0).toString());
        assertEquals("RIGHT", DoubleDirection.valueOf(1).toString());
        assertEquals("UP", DoubleDirection.valueOf(2).toString());
        assertEquals("DOWN", DoubleDirection.valueOf(3).toString());
        assertEquals("LEFT_UP", DoubleDirection.valueOf(4).toString());
        assertEquals("RIGHT_UP", DoubleDirection.valueOf(5).toString());
        assertEquals("LEFT_DOWN", DoubleDirection.valueOf(6).toString());
        assertEquals("RIGHT_DOWN", DoubleDirection.valueOf(7).toString());
        assertEquals("NONE", DoubleDirection.valueOf(8).toString());
    }

    @Test
    public void test_valueOfName() {
        assertEquals(0, DoubleDirection.valueOf("LEFT").value());
        assertEquals(1, DoubleDirection.valueOf("RIGHT").value());
        assertEquals(2, DoubleDirection.valueOf("UP").value());
        assertEquals(3, DoubleDirection.valueOf("DOWN").value());
        assertEquals(4, DoubleDirection.valueOf("LEFT_UP").value());
        assertEquals(5, DoubleDirection.valueOf("RIGHT_UP").value());
        assertEquals(6, DoubleDirection.valueOf("LEFT_DOWN").value());
        assertEquals(7, DoubleDirection.valueOf("RIGHT_DOWN").value());
        assertEquals(8, DoubleDirection.valueOf("NONE").value());
    }

    @Test
    public void test_inverted() {
        assertEquals(DoubleDirection.RIGHT, DoubleDirection.LEFT.inverted());
        assertEquals(DoubleDirection.LEFT, DoubleDirection.RIGHT.inverted());
        assertEquals(DoubleDirection.DOWN, DoubleDirection.UP.inverted());
        assertEquals(DoubleDirection.UP, DoubleDirection.DOWN.inverted());
        assertEquals(DoubleDirection.RIGHT_DOWN, DoubleDirection.LEFT_UP.inverted());
        assertEquals(DoubleDirection.LEFT_DOWN, DoubleDirection.RIGHT_UP.inverted());
        assertEquals(DoubleDirection.RIGHT_UP, DoubleDirection.LEFT_DOWN.inverted());
        assertEquals(DoubleDirection.LEFT_UP, DoubleDirection.RIGHT_DOWN.inverted());
        assertEquals(DoubleDirection.NONE, DoubleDirection.NONE.inverted());
    }

    @Test
    public void test_random() {
        int[] c = new int[8];
        for (int count = 0; count < N*(8 + 1); count ++) {
            c[DoubleDirection.random().value()]++;
        }
        String message = Arrays.toString(c);
        assertTrue(message, c[0] > N);
        assertTrue(message, c[1] > N);
        assertTrue(message, c[2] > N);
        assertTrue(message, c[3] > N);
        assertTrue(message, c[4] > N);
        assertTrue(message, c[5] > N);
        assertTrue(message, c[6] > N);
        assertTrue(message, c[7] > N);
    }

    @Test
    public void test_change() {
        check(new PointImpl(0, -1), DoubleDirection.DOWN, new PointImpl(0, 0));
        check(new PointImpl(-1, 0), DoubleDirection.LEFT, new PointImpl(0, 0));
        check(new PointImpl(0, 1), DoubleDirection.UP,   new PointImpl(0, 0));
        check(new PointImpl(1, 0), DoubleDirection.RIGHT, new PointImpl(0, 0));
        check(new PointImpl(-1, 1), DoubleDirection.LEFT_UP, new PointImpl(0, 0));
        check(new PointImpl(1, 1), DoubleDirection.RIGHT_UP, new PointImpl(0, 0));
        check(new PointImpl(-1, -1), DoubleDirection.LEFT_DOWN, new PointImpl(0, 0));
        check(new PointImpl(1, -1), DoubleDirection.RIGHT_DOWN, new PointImpl(0, 0));
        check(new PointImpl(0, 0), DoubleDirection.NONE, new PointImpl(0, 0));
    }

    @Test
    public void test_changePoint() {
        assertEquals("[1,0]", DoubleDirection.RIGHT.change(pt(0, 0)).toString());
        assertEquals("[-1,0]", DoubleDirection.LEFT.change(pt(0, 0)).toString());
        assertEquals("[0,1]", DoubleDirection.UP.change(pt(0, 0)).toString());
        assertEquals("[0,-1]", DoubleDirection.DOWN.change(pt(0, 0)).toString());
        assertEquals("[-1,1]", DoubleDirection.LEFT_UP.change(pt(0, 0)).toString());
        assertEquals("[1,1]", DoubleDirection.RIGHT_UP.change(pt(0, 0)).toString());
        assertEquals("[-1,-1]", DoubleDirection.LEFT_DOWN.change(pt(0, 0)).toString());
        assertEquals("[1,-1]", DoubleDirection.RIGHT_DOWN.change(pt(0, 0)).toString());
        assertEquals("[0,0]", DoubleDirection.NONE.change(pt(0, 0)).toString());
    }

    private void check(Point expected, DoubleDirection direction, Point input) {
        assertEquals(expected.getX(), direction.changeX(input.getX()));
        assertEquals(expected.getY(), direction.changeY(input.getY()));
        assertEquals(expected, direction.change(input));
    }
}
