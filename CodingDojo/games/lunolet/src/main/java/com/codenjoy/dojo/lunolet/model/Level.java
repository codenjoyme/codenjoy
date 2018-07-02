package com.codenjoy.dojo.lunolet.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Level {

    int levelNumber;

    double DryMass;

    VesselStatus VesselStatus;

    List<Point2D.Double> Relief;

    double TargetX;

    public Level() {
        levelNumber = 0;

        prepareLevel();
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void levelUp() {
        levelNumber++;

        prepareLevel();
    }

    private void prepareLevel() {
        DryMass = 250.0;
        VesselStatus = new VesselStatus();
        Relief = new LinkedList<Point2D.Double>();

        if (levelNumber == 0) {
            Relief.add(new Point2D.Double(-10000, 0));
            Relief.add(new Point2D.Double(10000, 0));
            TargetX = 25;
        } else {
            Random random = new Random(levelNumber);
            Point2D.Double[] array = new Point2D.Double[21];
            array[0] = new Point2D.Double(-10000, 0);
            array[9] = new Point2D.Double(-5, 0);
            array[10] = new Point2D.Double(5, 0);
            double y1 = 0;
            double y2 = 0;
            for (int i = 0; i < 8; i++) {
                y1 += random.nextInt(11) - 5;
                y2 += random.nextInt(11) - 5;
                array[8 - i] = new Point2D.Double(-10 - 5 * i, y1);
                array[11 + i] = new Point2D.Double(10 + 5 * i, y2);
            }
            array[19] = new Point2D.Double(array[18].x + 10, array[18].y);  // target
            array[20] = new Point2D.Double(10000, 0);
            Relief = Arrays.asList(array);
            TargetX = array[18].x + 5;
        }

        VesselStatus.FuelMass = 50.0;
        VesselStatus.State = VesselState.START;
    }
}
