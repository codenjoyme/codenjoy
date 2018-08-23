package com.codenjoy.dojo.lunolet.model;

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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import static org.junit.Assert.*;

/**
 * LineIntersection class unit tests. See
 * https://github.com/justcoding121/Advanced-Algorithms/blob/master/Advanced.Algorithms.Tests/Geometry/LineIntersection_Tests.cs
 */
public class LineIntersectionTest {

    @Test
    public void lineIntersection_smokeTest() {

        Line2D line1 = new Line2D.Double(new Point2D.Double(1, 1), new Point2D.Double(10, 1));
        Line2D line2 = new Line2D.Double(new Point2D.Double(1, 2), new Point2D.Double(10, 2));

        assertNull(LineIntersection.findIntersection(line1, line2));

        line1 = new Line2D.Double(new Point2D.Double(10, 0), new Point2D.Double(0, 10));
        line2 = new Line2D.Double(new Point2D.Double(0, 10), new Point2D.Double(10, 10));

        assertEquals(new Point2D.Double(0, 10), LineIntersection.findIntersection(line1, line2));

        line1 = new Line2D.Double(new Point2D.Double(0, 0), new Point2D.Double(10, 10));
        line2 = new Line2D.Double(new Point2D.Double(0, 10), new Point2D.Double(10, 10));

        assertEquals(new Point2D.Double(10, 10), LineIntersection.findIntersection(line1, line2));

        line1 = new Line2D.Double(new Point2D.Double(10, 0), new Point2D.Double(0, 10));
        line2 = new Line2D.Double(new Point2D.Double(0, 0), new Point2D.Double(10, 10));

        assertEquals(new Point2D.Double(5, 5), LineIntersection.findIntersection(line1, line2));

        line1 = new Line2D.Double(new Point2D.Double(-5, -5), new Point2D.Double(0, 0));
        line2 = new Line2D.Double(new Point2D.Double(1, 1), new Point2D.Double(10, 10));

        assertNull(LineIntersection.findIntersection(line1, line2));

        line1 = new Line2D.Double(new Point2D.Double(3, -5), new Point2D.Double(3, 10));
        line2 = new Line2D.Double(new Point2D.Double(0, 5), new Point2D.Double(10, 5));

        assertEquals(new Point2D.Double(3, 5), LineIntersection.findIntersection(line1, line2));

        line1 = new Line2D.Double(new Point2D.Double(0, 5), new Point2D.Double(10, 5));
        line2 = new Line2D.Double(new Point2D.Double(3, -5), new Point2D.Double(3, 10));

        assertEquals(new Point2D.Double(3, 5), LineIntersection.findIntersection(line1, line2));

        line1 = new Line2D.Double(new Point2D.Double(0, 5), new Point2D.Double(10, 5));
        line2 = new Line2D.Double(new Point2D.Double(3, -5), new Point2D.Double(5, 15));

        assertEquals(new Point2D.Double(4, 5), LineIntersection.findIntersection(line1, line2));

        line1 = new Line2D.Double(new Point2D.Double(0, -5), new Point2D.Double(0, 5));
        line2 = new Line2D.Double(new Point2D.Double(-3, 0), new Point2D.Double(3, 0));

        assertEquals(new Point2D.Double(0, 0), LineIntersection.findIntersection(line1, line2));

    }
}
