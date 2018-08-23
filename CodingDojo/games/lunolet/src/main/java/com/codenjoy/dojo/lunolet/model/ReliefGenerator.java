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


import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class ReliefGenerator {
    private List<DoubleUnaryOperator> functions;

    public ReliefGenerator() {
        functions = new ArrayList<>();
    }

    public void addSin(double amplitude, double frequency) {
        functions.add(x -> amplitude * Math.sin(frequency * x * 2.0 * Math.PI));
    }

    public void addTrend(DoubleUnaryOperator trend) {
        functions.add(trend);
    }

    public List<Point2D.Double> generate(double start, double end) {
        return generate(start, end, x -> x + 10);
    }

    public List<Point2D.Double> generate(double start, double end, DoubleUnaryOperator sequenceGenerator) {
        double x = start;
        List<Point2D.Double> relief = new ArrayList<>();
        double minY = Double.MAX_VALUE;
        while (x <= end) {
            double y = calculateYValue(x);
            if (y < minY)
                minY = y;
            relief.add(new Point2D.Double(x, y));
            x = sequenceGenerator.applyAsDouble(x);
        }

        for (Point2D.Double point : relief) {
            if (minY < 0.0)
                point.y -= minY;
            point.y = Math.floor(point.y * 100) / 100;
        }

//        for (Point2D.Double point : relief) {
//            System.out.println("x: " + point.x + ", y: " + point.y);
//        }

        return relief;
    }

    private double calculateYValue(double x) {
        double y = 0.0;
        for (DoubleUnaryOperator function : functions) {
            y += function.applyAsDouble(x);
        }
        return y / (double) functions.size();
    }
}
