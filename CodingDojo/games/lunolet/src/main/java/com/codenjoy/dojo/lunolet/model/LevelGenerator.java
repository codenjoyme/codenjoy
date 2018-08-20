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


import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleUnaryOperator;

public class LevelGenerator {

    private static int level;

    public static Level generate(JSONObject levelJson) {
        return generate(levelJson, 1);
    }

    public static Level generate(JSONObject levelJson, int hardness) {
        level = hardness;
        return parseLevel(levelJson);
    }

    private static Level parseLevel(JSONObject jsonLevel) {
        Level result = new Level();

        result.DryMass = jsonLevel.getDouble("dryMass");

        JSONObject vesselJson = jsonLevel.getJSONObject("vesselStatus");
        result.VesselStatus = parseVesselStatus(vesselJson);

        result.Relief = generateRelief(jsonLevel);

        result.TargetX = 100 * (level - 1) > result.VesselStatus.X
                ? 100 * (level - 1)
                : result.Relief.get(result.Relief.size() - 1).x - 50;//jsonLevel.getDouble("targetX");

        Point2D.Double startPoint = new Point2D.Double(result.VesselStatus.X, result.VesselStatus.Y);

        normalizeSquares(result.Relief,
                startPoint,
                result.TargetX);

        result.VesselStatus.Y = startPoint.y;

        return result;
    }

    private static List<Point2D.Double> generateRelief(JSONObject jsonLevel) {
        String generationType = jsonLevel.getString("reliefGeneration");

        if (generationType == "none")
            return generateSimple(jsonLevel.getJSONArray("relief"));

        ReliefGenerator reliefGen = new ReliefGenerator();

        DoubleUnaryOperator trend = null;
        switch (generationType) {
            case "linear":
                trend = x -> level / 50.0 * x;
                break;
            case "cos":
                trend = x -> 4.0 * level * Math.cos((0.05 / level) * x * 2.0 * Math.PI);
                break;
            case "polynomial1":
                trend = x -> {
                    x /= level * 18.0;
                    x -= 3.0;
                    return 10.0 * level * ((x + 2) * (x + 2) * (x - 2) - 50.0);
                };
                break;
            case "polynomial2":
                trend = x -> {
                    x /= level * 18.0;
                    x -= 3.0;
                    return -10.0 * level * ((x + 2) * (x + 2) * (x - 2) - 50.0);
                };
                break;
        }
        reliefGen.addSin(level * 10, 1.0 / (level == 0 ? 1.0 : level));
        reliefGen.addTrend(trend);
        return reliefGen.generate(-100, 100 * level);
    }

    private static List<Point2D.Double> generateSimple(JSONArray reliefJson) {
        List<Point2D.Double> relief = new ArrayList<>();
        for (Object point : reliefJson) {
            if (point instanceof JSONObject) {
                JSONObject p = (JSONObject) point;
                relief.add(new Point2D.Double(p.getDouble("x"), p.getDouble("y")));
            }
        }
        return relief;
    }

    private static VesselStatus parseVesselStatus(JSONObject vesselJson) {
        VesselStatus vStatus = new VesselStatus();
        vStatus.X = vesselJson.getDouble("x");
        vStatus.Y = vesselJson.getDouble("y");
        vStatus.Time = vesselJson.getDouble("time");
        vStatus.HSpeed = vesselJson.getDouble("hSpeed");
        vStatus.VSpeed = vesselJson.getDouble("vSpeed");
        vStatus.FuelMass = vesselJson.getDouble("fuelMass") * level;
        vStatus.State = VesselState.values()[vesselJson.getInt("state")];
        return vStatus;
    }

    private static void normalizeSquares(List<Point2D.Double> relief, Point2D.Double start, double targetX) {
        Point2D.Double prevPoint = null;
        boolean startNormalized = false;
        for (Point2D.Double point : relief) {
            if (!startNormalized && point.x >= start.x) {
                if (prevPoint == null)
                    return;
                if (point.x == start.x)
                    point.x += 10;

                double startHeight = Math.min(prevPoint.y, point.y);

                // make canyon on start
                if (((startHeight + targetX) % 10.0) < 1.5)
                    startHeight -= 100.0;

                start.y = prevPoint.y = point.y = startHeight;

                startNormalized = true;
            } else if (point.x >= targetX) {
                if (point.x == targetX)
                    point.x += 10;
                if (prevPoint != null)
                    prevPoint.y = point.y;
                return;
            }
            prevPoint = point;
        }
    }
}
