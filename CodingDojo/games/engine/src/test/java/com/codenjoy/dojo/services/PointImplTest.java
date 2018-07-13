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

import static org.junit.Assert.*;

public class PointImplTest {

    @Test
    public void shouldSaveXY() {
        Point pt = new PointImpl(10, 12);

        assertEquals(10, pt.getX());
        assertEquals(12, pt.getY());
    }

    @Test
    public void shouldSaveXY_staticMethod() {
        Point pt = PointImpl.pt(10, 12);

        assertEquals(10, pt.getX());
        assertEquals(12, pt.getY());
    }

    @Test
    public void shouldPrintToString() {
        Point pt = new PointImpl(10, 12);

        assertEquals("[10,12]", pt.toString());
    }

    @Test
     public void shouldCopyConstructor() {
        Point pt = new PointImpl(new PointImpl(10, 12));

        assertEquals("[10,12]", pt.toString());
    }

    @Test
    public void shouldItsMe() {
        Point pt = new PointImpl(10, 12);

        assertTrue(pt.itsMe(10, 12));
        assertFalse(pt.itsMe(10 + 1, 12));
        assertFalse(pt.itsMe(10, 12 + 1));

        assertTrue(pt.itsMe(new PointImpl(10, 12)));
        assertFalse(pt.itsMe(new PointImpl(10 + 1, 12)));
        assertFalse(pt.itsMe(new PointImpl(10, 12 + 1)));
    }

    @Test
    public void shouldIsOutOf_byY() {
        Point pt = new PointImpl(10, 12);

        assertTrue(pt.isOutOf(9));
        assertTrue(pt.isOutOf(10));
        assertTrue(pt.isOutOf(11));
        assertTrue(pt.isOutOf(12));
        assertFalse(pt.isOutOf(13));
        assertFalse(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byX() {
        Point pt = new PointImpl(12, 10);

        assertTrue(pt.isOutOf(9));
        assertTrue(pt.isOutOf(10));
        assertTrue(pt.isOutOf(11));
        assertTrue(pt.isOutOf(12));
        assertFalse(pt.isOutOf(13));
        assertFalse(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byYNegative() {
        Point pt = new PointImpl(10, -12);

        assertTrue(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byXNegative() {
        Point pt = new PointImpl(-12, 10);

        assertTrue(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOfDelta_from0() {
        Point pt = new PointImpl(1, 5);

        assertFalse(pt.isOutOf(0, 0, 20));
        assertFalse(pt.isOutOf(1, 0, 20));
        assertTrue(pt.isOutOf(2, 0, 20));
        assertTrue(pt.isOutOf(3, 0, 20));

        assertFalse(pt.isOutOf(0, 0, 20));
        assertFalse(pt.isOutOf(0, 1, 20));
        assertFalse(pt.isOutOf(0, 2, 20));
        assertFalse(pt.isOutOf(0, 3, 20));
        assertFalse(pt.isOutOf(0, 4, 20));
        assertFalse(pt.isOutOf(0, 5, 20));
        assertTrue(pt.isOutOf(0, 6, 20));
        assertTrue(pt.isOutOf(0, 7, 20));
    }

    @Test
    public void shouldIsOutOfDelta_fromSize() {
        Point pt = new PointImpl(10, 15);

        assertFalse(pt.isOutOf(0, 0, 20));
        assertFalse(pt.isOutOf(0, 1, 20));
        assertFalse(pt.isOutOf(0, 2, 20));
        assertFalse(pt.isOutOf(0, 3, 20));
        assertFalse(pt.isOutOf(0, 4, 20));
        assertTrue(pt.isOutOf(0, 5, 20));
        assertTrue(pt.isOutOf(0, 6, 20));
        assertTrue(pt.isOutOf(0, 7, 20));

        assertFalse(pt.isOutOf(8, 0, 20));
        assertFalse(pt.isOutOf(9, 0, 20));
        assertTrue(pt.isOutOf(10, 0, 20));
        assertTrue(pt.isOutOf(11, 0, 20));
    }

    @Test
    public void shouldDistance() {
        Point pt = new PointImpl(10, 15);

        assertEquals(0.0, pt.distance(new PointImpl(10, 15)), 0.001);
        assertEquals(11.180339887498949, pt.distance(new PointImpl(20, 20)), 0.001);
    }

    @Test
    public void shouldEqualsAndHashCode() {
        Point pt = new PointImpl(10, 15);

        assertTrue(pt.equals(new PointImpl(10, 15)));
        assertFalse(pt.equals(new PointImpl(10 + 1, 15)));
        assertFalse(pt.equals(new PointImpl(10, 15 + 1)));
        assertFalse(pt.equals(null));
        assertFalse(pt.equals(new Object()));

        assertEquals(pt.hashCode(), new PointImpl(10, 15).hashCode());
        assertNotSame(pt.hashCode(), new PointImpl(10 + 1, 15).hashCode());
        assertNotSame(pt.hashCode(), new PointImpl(10, 15 + 1).hashCode());
    }

    @Test
    public void shouldMove() {
        Point pt = new PointImpl(10, 15);

        pt.move(new PointImpl(20, 23));

        assertEquals("[20,23]", pt.toString());

        pt.move(40, 43);

        assertEquals("[40,43]", pt.toString());
    }

    @Test
    public void shouldCopy() {
        Point pt = new PointImpl(10, 15);

        Point pt2 = pt.copy();
        pt.move(1, 2);

        assertEquals("[1,2]", pt.toString());
        assertEquals("[10,15]", pt2.toString());
    }

    @Test
    public void shouldChange() {
        Point pt = new PointImpl(10, 15);

        pt.change(new PointImpl(12, -23));

        assertEquals("[22,-8]", pt.toString());
    }

    @Test
    public void shouldCompareTo() {
        Point pt = new PointImpl(10, 15);

        assertEquals(1, pt.compareTo(new PointImpl(10, 14)));
        assertEquals(0, pt.compareTo(new PointImpl(10, 15)));
        assertEquals(-1, pt.compareTo(new PointImpl(10, 16)));

        assertEquals(1, pt.compareTo(new PointImpl(9, 15)));
        assertEquals(0, pt.compareTo(new PointImpl(10, 15)));
        assertEquals(-1, pt.compareTo(new PointImpl(11, 15)));

        assertEquals(1, pt.compareTo(new PointImpl(9, 100)));
        assertEquals(0, pt.compareTo(new PointImpl(10, 15)));
        assertEquals(-1, pt.compareTo(new PointImpl(11, 1)));

        assertEquals(-1, pt.compareTo(null));
    }
}
