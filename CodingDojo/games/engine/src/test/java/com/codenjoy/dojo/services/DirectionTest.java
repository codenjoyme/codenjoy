package com.codenjoy.dojo.services;

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


import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.*;

public class DirectionTest {

    @Test
    public void test_values() {
        assertEquals("[LEFT, RIGHT, UP, DOWN]", Direction.getValues().toString());
    }

    @Test
    public void test_valueOfIndex() {
        assertEquals("LEFT", Direction.valueOf(0).toString());
        assertEquals("RIGHT", Direction.valueOf(1).toString());
        assertEquals("UP", Direction.valueOf(2).toString());
        assertEquals("DOWN", Direction.valueOf(3).toString());
        assertEquals("ACT", Direction.valueOf(4).toString());
        assertEquals("STOP", Direction.valueOf(5).toString());
    }

    @Test
    public void test_valueOfIndex_validation() {
        try {
            Direction.valueOf(6);
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("No such Direction for: 6", exception.getMessage());
        }
    }

    @Test
    public void test_isValid() {
        assertEquals(true, Direction.isValid("LEFT"));
        assertEquals(true, Direction.isValid("RIGHT"));
        assertEquals(true, Direction.isValid("UP"));
        assertEquals(true, Direction.isValid("DOWN"));
        assertEquals(true, Direction.isValid("ACT"));
        assertEquals(true, Direction.isValid("STOP"));
        assertEquals(true, Direction.isValid("left"));
        assertEquals(true, Direction.isValid("Left"));

        assertEquals(false, Direction.isValid("qwe"));
        assertEquals(false, Direction.isValid(null));
    }

    @Test
    public void test_clockwise() {
        assertEquals(Direction.UP, Direction.LEFT.clockwise());
        assertEquals(Direction.DOWN, Direction.RIGHT.clockwise());
        assertEquals(Direction.RIGHT, Direction.UP.clockwise());
        assertEquals(Direction.LEFT, Direction.DOWN.clockwise());
    }

    @Test
    public void test_clockwise_validation() {
        try {
            Direction.STOP.clockwise();
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("Cant clockwise for: STOP", exception.getMessage());
        }

        try {
            Direction.ACT.clockwise();
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("Cant clockwise for: ACT", exception.getMessage());
        }
    }

    @Test
    public void test_counterClockwise() {
        assertEquals(Direction.DOWN, Direction.LEFT.counterClockwise());
        assertEquals(Direction.UP, Direction.RIGHT.counterClockwise());
        assertEquals(Direction.LEFT, Direction.UP.counterClockwise());
        assertEquals(Direction.RIGHT, Direction.DOWN.counterClockwise());
    }

    @Test
    public void test_counterClockwise_validation() {
        try {
            Direction.STOP.counterClockwise();
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("Cant counter clockwise for: STOP", exception.getMessage());
        }

        try {
            Direction.ACT.counterClockwise();
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("Cant counter clockwise for: ACT", exception.getMessage());
        }
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
    public void test_inverted_validation() {
        try {
            Direction.STOP.inverted();
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("Cant invert for: STOP", exception.getMessage());
        }

        try {
            Direction.ACT.inverted();
            fail("Expected exception");
        } catch (IllegalArgumentException exception) {
            assertEquals("Cant invert for: ACT", exception.getMessage());
        }
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
        check(pt(0, -1), Direction.DOWN, pt(0, 0));
        check(pt(-1, 0), Direction.LEFT, pt(0, 0));
        check(pt(0, 1), Direction.UP, pt(0, 0));
        check(pt(1, 0), Direction.RIGHT, pt(0, 0));
    }

    @Test
    public void test_changePoint() {
        assertEquals("[1,0]", Direction.RIGHT.change(pt(0, 0)).toString());
        assertEquals("[-1,0]", Direction.LEFT.change(pt(0, 0)).toString());
        assertEquals("[0,1]", Direction.UP.change(pt(0, 0)).toString());
        assertEquals("[0,-1]", Direction.DOWN.change(pt(0, 0)).toString());
    }

    private void check(Point expected, Direction direction, Point input) {
        assertEquals(expected.getX(), direction.changeX(input.getX()));
        assertEquals(expected.getY(), direction.changeY(input.getY()));
        assertEquals(expected, direction.change(input));
    }

    @Test
    public void test_act() {
        assertEquals("ACT()", Direction.ACT());
        assertEquals("ACT(-1)", Direction.ACT(-1));
        assertEquals("ACT(null)", Direction.ACT(null));
        assertEquals("ACT(1)", Direction.ACT(1));
        assertEquals("ACT(1,2)", Direction.ACT(1, 2));
        assertEquals("ACT(1,2,3)", Direction.ACT(1, 2, 3));
    }

    @Test
    public void test_actBeforeAfter() {
        assertEquals("ACT,LEFT", Direction.LEFT.ACT(true));
        assertEquals("LEFT,ACT", Direction.LEFT.ACT(false));

        assertEquals("ACT,ACT", Direction.ACT.ACT(true));
        assertEquals("ACT,ACT", Direction.ACT.ACT(false));

        assertEquals("ACT,STOP", Direction.STOP.ACT(true));
    }
}
