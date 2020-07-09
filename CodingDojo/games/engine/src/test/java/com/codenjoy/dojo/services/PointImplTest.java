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

import static org.junit.Assert.*;
import static com.codenjoy.dojo.services.PointImpl.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class PointImplTest {

    @Test
    public void shouldSaveXY() {
        Point pt = pt(10, 12);

        assertEquals(10, pt.getX());
        assertEquals(12, pt.getY());
    }

    @Test
    public void shouldSaveXY_staticMethod() {
        Point pt = pt(10, 12);

        assertEquals(10, pt.getX());
        assertEquals(12, pt.getY());
    }

    @Test
    public void shouldPrintToString() {
        Point pt = pt(10, 12);

        assertEquals("[10,12]", pt.toString());
    }

    @Test
     public void shouldCopyConstructor() {
        Point pt = new PointImpl(pt(10, 12));

        assertEquals("[10,12]", pt.toString());
    }

    @Test
    public void shouldItsMe() {
        Point pt = pt(10, 12);

        assertTrue(pt.itsMe(10, 12));
        assertFalse(pt.itsMe(10 + 1, 12));
        assertFalse(pt.itsMe(10, 12 + 1));

        assertTrue(pt.itsMe(pt(10, 12)));
        assertFalse(pt.itsMe(pt(10 + 1, 12)));
        assertFalse(pt.itsMe(pt(10, 12 + 1)));
    }

    @Test
    public void shouldIsOutOf_byY() {
        Point pt = pt(10, 12);

        assertTrue(pt.isOutOf(9));
        assertTrue(pt.isOutOf(10));
        assertTrue(pt.isOutOf(11));
        assertTrue(pt.isOutOf(12));
        assertFalse(pt.isOutOf(13));
        assertFalse(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byX() {
        Point pt = pt(12, 10);

        assertTrue(pt.isOutOf(9));
        assertTrue(pt.isOutOf(10));
        assertTrue(pt.isOutOf(11));
        assertTrue(pt.isOutOf(12));
        assertFalse(pt.isOutOf(13));
        assertFalse(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byYNegative() {
        Point pt = pt(10, -12);

        assertTrue(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOf_byXNegative() {
        Point pt = pt(-12, 10);

        assertTrue(pt.isOutOf(14));
    }

    @Test
    public void shouldIsOutOfDelta_from0() {
        Point pt = pt(1, 5);

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
        Point pt = pt(10, 15);

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
        Point pt = pt(10, 15);

        assertEquals(0.0, pt.distance(pt(10, 15)), 0.001);
        assertEquals(11.180339887498949, pt.distance(pt(20, 20)), 0.001);
    }

    @Test
    public void shouldEqualsAndHashCode() {
        Point pt = pt(10, 15);

        assertTrue(pt.equals(pt(10, 15)));
        assertFalse(pt.equals(pt(10 + 1, 15)));
        assertFalse(pt.equals(pt(10, 15 + 1)));
        assertFalse(pt.equals(null));
        assertFalse(pt.equals(new Object()));

        assertEquals(pt.hashCode(), pt(10, 15).hashCode());
        assertNotSame(pt.hashCode(), pt(10 + 1, 15).hashCode());
        assertNotSame(pt.hashCode(), pt(10, 15 + 1).hashCode());
    }

    @Test
    public void shouldMove() {
        Point pt = pt(10, 15);

        pt.move(pt(20, 23));

        assertEquals("[20,23]", pt.toString());

        pt.move(40, 43);

        assertEquals("[40,43]", pt.toString());
    }

    @Test
    public void shouldDefaultConstructor() {
        Point pt = new PointImpl();

        assertEquals("[-1,-1]", pt.toString());
    }

    @Test
    public void shouldSet() {
        Point pt = pt(10, 15);

        pt.setX(20);
        pt.setY(23);

        assertEquals("[20,23]", pt.toString());

        pt.setX(40);
        pt.setY(43);

        assertEquals("[40,43]", pt.toString());
    }

    @Test
    public void shouldCopy() {
        Point pt = pt(10, 15);

        Point pt2 = pt.copy();
        pt.move(1, 2);

        assertEquals("[1,2]", pt.toString());
        assertEquals("[10,15]", pt2.toString());
    }

    @Test
    public void shouldChange() {
        Point pt = pt(10, 15);

        pt.change(pt(12, -23));

        assertEquals("[22,-8]", pt.toString());
    }

    @Test
    public void shouldCompareTo() {
        Point pt = pt(10, 15);

        assertEquals(1, pt.compareTo(pt(10, 14)));
        assertEquals(0, pt.compareTo(pt(10, 15)));
        assertEquals(-1, pt.compareTo(pt(10, 16)));

        assertEquals(1, pt.compareTo(pt(9, 15)));
        assertEquals(0, pt.compareTo(pt(10, 15)));
        assertEquals(-1, pt.compareTo(pt(11, 15)));

        assertEquals(1, pt.compareTo(pt(9, 100)));
        assertEquals(0, pt.compareTo(pt(10, 15)));
        assertEquals(-1, pt.compareTo(pt(11, 1)));

        assertEquals(-1, pt.compareTo(null));
    }

    @Test
    public void shouldRelative() {
        Point pt = pt(10, 15);

        assertEquals("[9,14]", pt.relative(pt(1, 1)).toString());
        assertEquals("[8,14]", pt.relative(pt(2, 1)).toString());
        assertEquals("[9,13]", pt.relative(pt(1, 2)).toString());
        assertEquals("[11,16]", pt.relative(pt(-1, -1)).toString());
        assertEquals("[10,15]", pt.relative(pt(0, 0)).toString());
        assertEquals("[-30,15]", pt.relative(pt(40, 0)).toString());
    }

    @Test
    public void shouldGenerateRandom() {
        Dice dice = mock(Dice.class);
        when(dice.next(anyInt())).thenReturn(100, 101);

        int size = 24;
        Point pt = random(dice, size);

        verify(dice, times(2)).next(size);
        assertEquals("[100,101]", pt.toString());
    }
}
