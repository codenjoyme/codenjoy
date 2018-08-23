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
import java.util.*;

public class LevelManager {

    private List<Level> levels;

    public LevelManager() {
        loadLevels();
    }

    public Level getLevel(int level) {
        if (level >= levels.size()) {
            level = 0;
        }
        return levels.get(level);
    }

    private void loadLevels() {
        levels = new ArrayList<>();
        for (int i = 0; i < JSON_LEVELS.length; i++) {
            String json = JSON_LEVELS[i];
            levels.add(parseLevel(json));
        }

        for (int i = JSON_LEVELS.length; i < JSON_LEVELS.length + 8; i++) {
            Level level = prepareLevel(i);
            levels.add(level);
        }

        for (int hardness = 1; hardness < 41; hardness++) {
            for (int i = 0; i < JSON_LEVELGEN.length; i++) {
                JSONObject levelJson = new JSONObject(JSON_LEVELGEN[i]);
                Level level = LevelGenerator.generate(levelJson, hardness);
                levels.add(level);
            }
        }
    }

    private Level parseLevel(String jsonRepresentation) {
        Level result = new Level();
        JSONObject level = new JSONObject(jsonRepresentation);
        result.DryMass = level.getDouble("dryMass");
        result.TargetX = level.getDouble("targetX");
        JSONArray reliefJson = level.getJSONArray("relief");
        result.Relief = parseRelief(reliefJson);
        JSONObject vesselJson = level.getJSONObject("vesselStatus");
        result.VesselStatus = parseVesselStatus(vesselJson);
        return result;
    }

    private List<Point2D.Double> parseRelief(JSONArray reliefJson) {
        List<Point2D.Double> relief = new ArrayList<>();
        for (Object point : reliefJson) {
            if (point instanceof JSONObject) {
                JSONObject p = (JSONObject) point;
                relief.add(new Point2D.Double(p.getDouble("x"), p.getDouble("y")));
            }
        }
        return relief;
    }

    private VesselStatus parseVesselStatus(JSONObject vesselJson) {
        VesselStatus vStatus = new VesselStatus();
        vStatus.X = vesselJson.getDouble("x");
        vStatus.Y = vesselJson.getDouble("y");
        vStatus.Time = vesselJson.getDouble("time");
        vStatus.HSpeed = vesselJson.getDouble("hSpeed");
        vStatus.VSpeed = vesselJson.getDouble("vSpeed");
        vStatus.FuelMass = vesselJson.getDouble("fuelMass");
        vStatus.State = VesselState.values()[vesselJson.getInt("state")];
        return vStatus;
    }

    private static final String[] JSON_LEVELGEN = {
            "{" +
            "  'dryMass': 250.0, 'targetX': 25, 'reliefGeneration': 'linear', 'relief': [" +
            "    { 'x': -10000, 'y': 0 }," +
            "    { 'x': 10000, 'y': 0 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'time': 0, 'hSpeed': 0, 'vSpeed': 0, 'fuelMass': 50.0, 'state': 0 }" +
            "}",
            "{" +
            "  'dryMass': 250.0, 'targetX': 100, 'reliefGeneration': 'cos', 'relief': [" +
            "    { 'x': -10000,'y': 0 }," +
            "    { 'x': 0,'y': 0 }," +
            "    { 'x': 5,'y': 10 }," +
            "    { 'x': 10,'y': 30 }," +
            "    { 'x': 15,'y': 23 }," +
            "    { 'x': 20,'y': 0 }," +
            "    { 'x': 10000,'y': 0 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'time': 0, 'hSpeed': 0, 'vSpeed': 0, 'fuelMass': 50.0, 'state': 0 }" +
            "}",
            "{" +
            "  'dryMass': 250.0, 'targetX': 40, 'reliefGeneration': 'polynomial1', 'relief': [" +
            "    { 'x': -10000,'y': 0 }," +
            "    { 'x': 0,'y': 0 }," +
            "    { 'x': 5,'y': 0 }," +
            "    { 'x': 10,'y': 30 }," +
            "    { 'x': 15,'y': 7 }," +
            "    { 'x': 20,'y': 12 }," +
            "    { 'x': 25,'y': 17 }," +
            "    { 'x': 30,'y': 3 }," +
            "    { 'x': 35,'y': 0 }," +
            "    { 'x': 10000,'y': 0 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'time': 0, 'hSpeed': 0, 'vSpeed': 0, 'fuelMass': 50.0, 'state': 0 }" +
            "}",
            "{" +
            "  'dryMass': 250.0, 'targetX': 40, 'reliefGeneration': 'polynomial2', 'relief': [" +
            "    { 'x': -10000,'y': 0 }," +
            "    { 'x': 0,'y': 0 }," +
            "    { 'x': 5,'y': 0 }," +
            "    { 'x': 10,'y': 30 }," +
            "    { 'x': 15,'y': 7 }," +
            "    { 'x': 20,'y': 12 }," +
            "    { 'x': 25,'y': 17 }," +
            "    { 'x': 30,'y': 3 }," +
            "    { 'x': 35,'y': 0 }," +
            "    { 'x': 10000,'y': 0 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'time': 0, 'hSpeed': 0, 'vSpeed': 0, 'fuelMass': 50.0, 'state': 0 }" +
            "}",
    };

    private static final String[] JSON_LEVELS = {
        // level 0
            "{" +
            "  'dryMass': 250.0, 'targetX': 0, 'relief': [" +
            "    { 'x': -10000, 'y': 3000 }," +
            "    { 'x': -10, 'y': -10 }," +
            "    { 'x': 10, 'y': -10 }," +
            "    { 'x': 10000, 'y': 3000 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'hSpeed': 0, 'vSpeed': 0, 'fuelMass': 50.0, 'time': 0, 'state': 1 }" +
            "}",
        // level 1
            "{" +
            "  'dryMass': 250.0, 'targetX': 25, 'relief': [" +
            "    { 'x': -10000, 'y': 0 }," +
            "    { 'x': 10000, 'y': 0 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'hSpeed': 0, 'vSpeed': 0, 'fuelMass': 50.0, 'time': 0, 'state': 0 }" +
            "}",
        // level 2
            "{" +
            "  'dryMass': 250.0, 'targetX': 40, 'relief': [" +
            "    { 'x': -10000, 'y': 0 }," +
            "    { 'x': 0, 'y': 0 }," +
            "    { 'x': 15, 'y': 0 }," +
            "    { 'x': 20,'y': 5 }," +
            "    { 'x': 25,'y': 0 }," +
            "    { 'x': 10000,'y': 0 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'hSpeed': 0, 'vSpeed': 0, 'fuelMass': 50.0, 'time': 0, 'state': 0 }" +
            "}",
        // level 3
            "{" +
            "  'dryMass': 250.0, 'targetX': 100, 'relief': [" +
            "    { 'x': -10000, 'y': 3 }," +
            "    { 'x': 0,'y': 0 }," +
            "    { 'x': 5,'y': 10 }," +
            "    { 'x': 10,'y': 30 }," +
            "    { 'x': 15,'y': 23 }," +
            "    { 'x': 20,'y': 0 }," +
            "    { 'x': 10000,'y': 0 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'hSpeed': 0, 'vSpeed': 0, 'fuelMass': 50.0, 'time': 0, 'state': 0 }" +
            "}",
        // level 4
            "{" +
            "  'dryMass': 250.0, 'targetX': 40, 'relief': [" +
            "    { 'x': -10000, 'y': 0 }," +
            "    { 'x': 0,'y': 0 }," +
            "    { 'x': 5,'y': 0 }," +
            "    { 'x': 10,'y': 30 }," +
            "    { 'x': 15,'y': 7 }," +
            "    { 'x': 20,'y': 12 }," +
            "    { 'x': 25,'y': 17 }," +
            "    { 'x': 30,'y': 3 }," +
            "    { 'x': 35,'y': 0 }," +
            "    { 'x': 10000,'y': 0 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'hSpeed': 0, 'vSpeed': 0, 'time': 0, 'fuelMass': 50.0, 'state': 0 }" +
            "}",
        // level 5
            "{" +
            "  'dryMass': 250.0, 'targetX': 30, 'relief': [" +
            "    { 'x': -5000, 'y': 5000 }," +
            "    { 'x': -5,'y': 0 }," +
            "    { 'x': 5,'y': 0 }," +
            "    { 'x': 10,'y': 30 }," +
            "    { 'x': 15,'y': -10 }," +
            "    { 'x': 20,'y': 30 }," +
            "    { 'x': 25,'y': 0 }," +
            "    { 'x': 35,'y': 0 }," +
            "    { 'x': 5000,'y': 5000 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'hSpeed': 0, 'vSpeed': 0, 'time': 0, 'fuelMass': 50.0, 'state': 0 }" +
            "}",
        // level 6
            "{" +
            "  'dryMass': 250.0, 'targetX': 232, 'relief': [" +
            "    { 'x': -5000, 'y': 1000 }," +
            "    { 'x': -5,'y': 0 }," +
            "    { 'x': 1,'y': 0 }," +
            "    { 'x': 4,'y': 42 }," +
            "    { 'x': 17,'y': 42 }," +
            "    { 'x': 226,'y': 18 }," +
            "    { 'x': 238,'y': 18 }," +
            "    { 'x': 244,'y': 29 }," +
            "    { 'x': 5000,'y': 2000 }" +
            "  ]," +
            "  'vesselStatus': { 'x': 0, 'y': 0, 'hSpeed': 0, 'vSpeed': 0, 'time': 0, 'fuelMass': 50.0, 'state': 0 }" +
            "}"
    };

    private Level prepareLevel(int levelNumber) {
        Level level = new Level();
        level.DryMass = 250.0;
        level.VesselStatus = new VesselStatus();
        level.Relief = new LinkedList<Point2D.Double>();

        Random random = new Random(levelNumber);
        int step = 8 + random.nextInt(41) * 2;
        int halfstep = step / 2;

        if (levelNumber == 0) {
            level.Relief.add(new Point2D.Double(-10000, 0));
            level.Relief.add(new Point2D.Double(10000, 0));
            level.TargetX = step * 2.0 + halfstep;
        } else {
            Point2D.Double[] array = new Point2D.Double[21];
            array[0] = new Point2D.Double(-10000, 0);
            array[9] = new Point2D.Double(-halfstep, 0);
            array[10] = new Point2D.Double(halfstep, 0);
            double y1 = 0;
            double y2 = 0;
            for (int i = 0; i < 8; i++) {
                y1 += random.nextInt(step + 1) - halfstep;
                y2 += random.nextInt(step + 1) - halfstep;
                array[8 - i] = new Point2D.Double(-step - halfstep * i, y1);
                array[11 + i] = new Point2D.Double(step + halfstep * i, y2);
            }
            array[19] = new Point2D.Double(array[18].x + step, array[18].y);  // target
            array[20] = new Point2D.Double(10000, 0);
            level.Relief = Arrays.asList(array);
            level.TargetX = array[18].x + halfstep;
        }

        level.VesselStatus.FuelMass = 50.0;
        level.VesselStatus.State = VesselState.START;

        return level;
    }

    public int levelsCount() {
        return levels.size();
    }
}
