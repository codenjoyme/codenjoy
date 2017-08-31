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

public class QDirectionTest {

    public static final int N = 250;

    @Test
    public void test_valueOfIndex() {
        assertEquals("LEFT", QDirection.valueOf(0).toString());
        assertEquals("RIGHT", QDirection.valueOf(1).toString());
        assertEquals("UP", QDirection.valueOf(2).toString());
        assertEquals("DOWN", QDirection.valueOf(3).toString());
        assertEquals("LEFT_UP", QDirection.valueOf(4).toString());
        assertEquals("RIGHT_UP", QDirection.valueOf(5).toString());
        assertEquals("LEFT_DOWN", QDirection.valueOf(6).toString());
        assertEquals("RIGHT_DOWN", QDirection.valueOf(7).toString());
        assertEquals("NONE", QDirection.valueOf(8).toString());
    }

    @Test
    public void test_valueOfName() {
        assertEquals(0, QDirection.valueOf("LEFT").value());
        assertEquals(1, QDirection.valueOf("RIGHT").value());
        assertEquals(2, QDirection.valueOf("UP").value());
        assertEquals(3, QDirection.valueOf("DOWN").value());
        assertEquals(4, QDirection.valueOf("LEFT_UP").value());
        assertEquals(5, QDirection.valueOf("RIGHT_UP").value());
        assertEquals(6, QDirection.valueOf("LEFT_DOWN").value());
        assertEquals(7, QDirection.valueOf("RIGHT_DOWN").value());
        assertEquals(8, QDirection.valueOf("NONE").value());
    }

    @Test
    public void test_inverted() {
        assertEquals(QDirection.RIGHT, QDirection.LEFT.inverted());
        assertEquals(QDirection.LEFT, QDirection.RIGHT.inverted());
        assertEquals(QDirection.DOWN, QDirection.UP.inverted());
        assertEquals(QDirection.UP, QDirection.DOWN.inverted());
        assertEquals(QDirection.RIGHT_DOWN, QDirection.LEFT_UP.inverted());
        assertEquals(QDirection.LEFT_DOWN, QDirection.RIGHT_UP.inverted());
        assertEquals(QDirection.RIGHT_UP, QDirection.LEFT_DOWN.inverted());
        assertEquals(QDirection.LEFT_UP, QDirection.RIGHT_DOWN.inverted());
        assertEquals(QDirection.NONE, QDirection.NONE.inverted());
    }

    @Test
    public void test_random() {
        int[] c = new int[8];
        for (int count = 0; count < N*(8 + 2); count ++) {
            c[QDirection.random().value()]++;
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
        check(new PointImpl(0, -1), QDirection.DOWN, new PointImpl(0, 0));
        check(new PointImpl(-1, 0), QDirection.LEFT, new PointImpl(0, 0));
        check(new PointImpl(0, 1), QDirection.UP,   new PointImpl(0, 0));
        check(new PointImpl(1, 0), QDirection.RIGHT, new PointImpl(0, 0));
        check(new PointImpl(-1, 1), QDirection.LEFT_UP, new PointImpl(0, 0));
        check(new PointImpl(1, 1), QDirection.RIGHT_UP, new PointImpl(0, 0));
        check(new PointImpl(-1, -1), QDirection.LEFT_DOWN, new PointImpl(0, 0));
        check(new PointImpl(1, -1), QDirection.RIGHT_DOWN, new PointImpl(0, 0));
        check(new PointImpl(0, 0), QDirection.NONE, new PointImpl(0, 0));
    }

    @Test
    public void test_changePoint() {
        assertEquals("[1,0]", QDirection.RIGHT.change(pt(0, 0)).toString());
        assertEquals("[-1,0]", QDirection.LEFT.change(pt(0, 0)).toString());
        assertEquals("[0,1]", QDirection.UP.change(pt(0, 0)).toString());
        assertEquals("[0,-1]", QDirection.DOWN.change(pt(0, 0)).toString());
        assertEquals("[-1,1]", QDirection.LEFT_UP.change(pt(0, 0)).toString());
        assertEquals("[1,1]", QDirection.RIGHT_UP.change(pt(0, 0)).toString());
        assertEquals("[-1,-1]", QDirection.LEFT_DOWN.change(pt(0, 0)).toString());
        assertEquals("[1,-1]", QDirection.RIGHT_DOWN.change(pt(0, 0)).toString());
        assertEquals("[0,0]", QDirection.NONE.change(pt(0, 0)).toString());
    }

    private void check(Point expected, QDirection direction, Point input) {
        assertEquals(expected.getX(), direction.changeX(input.getX()));
        assertEquals(expected.getY(), direction.changeY(input.getY()));
        assertEquals(expected, direction.change(input));
    }
}
