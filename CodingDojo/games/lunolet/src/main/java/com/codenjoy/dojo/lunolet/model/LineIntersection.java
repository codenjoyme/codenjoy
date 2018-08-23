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


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Returns Point of intersection if do intersect otherwise default Point (null). See
 * https://github.com/justcoding121/Advanced-Algorithms/blob/master/Advanced.Algorithms/Geometry/LineIntersection.cs
 */
public class LineIntersection {

    public static Point2D.Double findIntersection(Line2D lineA, Line2D lineB) {

        double x1 = lineA.getX1(), y1 = lineA.getY1();
        double x2 = lineA.getX2(), y2 = lineA.getY2();

        double x3 = lineB.getX1(), y3 = lineB.getY1();
        double x4 = lineB.getX2(), y4 = lineB.getY2();

        double tolerance = 1e-5;

        //equations of the form x=c (two vertical lines)
        if (Math.abs(x1 - x2) < tolerance && Math.abs(x3 - x4) < tolerance && Math.abs(x1 - x3) < tolerance) {
            //throw new Exception("Both lines overlap vertically, ambiguous intersection points.");
            return null;
        }

        //equations of the form y=c (two horizontal lines)
        if (Math.abs(y1 - y2) < tolerance && Math.abs(y3 - y4) < tolerance && Math.abs(y1 - y3) < tolerance) {
            //throw new Exception("Both lines overlap horizontally, ambiguous intersection points.");
            return null;
        }

        //equations of the form x=c (two vertical lines)
        if (Math.abs(x1 - x2) < tolerance && Math.abs(x3 - x4) < tolerance) {
            return null;
        }

        //equations of the form y=c (two horizontal lines)
        if (Math.abs(y1 - y2) < tolerance && Math.abs(y3 - y4) < tolerance) {
            return null;
        }

        //general equation of line is y = mx + c where m is the slope
        //assume equation of line 1 as y1 = m1x1 + c1
        //=> -m1x1 + y1 = c1 ----(1)
        //assume equation of line 2 as y2 = m2x2 + c2
        //=> -m2x2 + y2 = c2 -----(2)
        //if line 1 and 2 intersect then x1=x2=x & y1=y2=y where (x,y) is the intersection point
        //so we will get below two equations
        //-m1x + y = c1 --------(3)
        //-m2x + y = c2 --------(4)

        double x, y;

        //lineA is vertical x1 = x2
        //slope will be infinity
        //so lets derive another solution
        if (Math.abs(x1 - x2) < tolerance) {
            //compute slope of line 2 (m2) and c2
            double m2 = (y4 - y3) / (x4 - x3);
            double c2 = -m2 * x3 + y3;

            //equation of vertical line is x = c
            //if line 1 and 2 intersect then x1=c1=x
            //substitute x=x1 in (4) => -m2x1 + y = c2
            // => y = c2 + m2x1
            x = x1;
            y = c2 + m2 * x1;
        }
        //lineB is vertical x3 = x4
        //slope will be infinity
        //so lets derive another solution
        else if (Math.abs(x3 - x4) < tolerance) {
            //compute slope of line 1 (m1) and c2
            double m1 = (y2 - y1) / (x2 - x1);
            double c1 = -m1 * x1 + y1;

            //equation of vertical line is x = c
            //if line 1 and 2 intersect then x3=c3=x
            //substitute x=x3 in (3) => -m1x3 + y = c1
            // => y = c1 + m1x3
            x = x3;
            y = c1 + m1 * x3;
        }
        //lineA & lineB are not vertical
        //(could be horizontal we can handle it with slope = 0)
        else {
            //compute slope of line 1 (m1) and c2
            double m1 = (y2 - y1) / (x2 - x1);
            double c1 = -m1 * x1 + y1;

            //compute slope of line 2 (m2) and c2
            double m2 = (y4 - y3) / (x4 - x3);
            double c2 = -m2 * x3 + y3;

            //solving equations (3) & (4) => x = (c1-c2)/(m2-m1)
            //plugging x value in equation (4) => y = c2 + m2 * x
            x = (c1 - c2) / (m2 - m1);
            y = c2 + m2 * x;

            //verify by plugging intersection point (x, y)
            //in original equations (1) & (2) to see if they intersect
            //otherwise x,y values will not be finite and will fail this check
            if (!(Math.abs(-m1 * x + y - c1) < tolerance
                    && Math.abs(-m2 * x + y - c2) < tolerance)) {
                return null;
            }
        }

        //x,y can intersect outside the line segment since line is infinitely long
        //so finally check if x, y is within both the line segments
        if (IsInsideLine(round5(x1), round5(y1), round5(x2), round5(y2), round5(x), round5(y)) &&
                IsInsideLine(round5(x3), round5(y3), round5(x4), round5(y4), round5(x), round5(y))) {
            return new Point2D.Double(round5(x), round5(y));
        }

        //return default null (no intersection)
        return null;
    }

    private static double round5(double v) {
        return Math.round(v * 100000.0) / 100000.0;
    }

    /// <summary>
    /// Returns true if given point(x,y) is inside the given line segment
    /// </summary>
    /// <param name="line"></param>
    /// <param name="x"></param>
    /// <param name="y"></param>
    /// <returns></returns>
    private static boolean IsInsideLine(double x1, double y1, double x2, double y2, double x, double y) {
        return (x >= x1 && x <= x2 || x >= x2 && x <= x1) &&
                (y >= y1 && y <= y2 || y >= y2 && y <= y1);
    }
}
