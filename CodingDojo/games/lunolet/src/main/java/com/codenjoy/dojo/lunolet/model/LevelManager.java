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


import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private int currentLevel;
    private List<Level> levels;

    public LevelManager(){
        LoadLevels();
        currentLevel = 0;
    }

    public Level getLevel() {
        return levels.get(currentLevel);
    }

    public int getLevelNumber() {
        return currentLevel;
    }

    public void levelUp() {
        // roll levels
        currentLevel = currentLevel < levels.size() - 1 ? currentLevel + 1 : 0;
    }

    private void LoadLevels() {
        levels = new ArrayList<>();
        for (int i = 0; i < JSON_LEVELS.length; i++) {
            String json = JSON_LEVELS[i];
            levels.add(parseLevel(json));
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
        for (Object point : reliefJson){
            if(point instanceof JSONObject){
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

    private static final String[] JSON_LEVELS =
            {
                    "{\n" +
                    "  \"dryMass\": 250.0,\n" +
                    "  \"targetX\": 25,\n" +
                    "  \"relief\": [\n" +
                    "    { \"x\": -10000, \"y\": 0 },\n" +
                    "    { \"x\": 10000, \"y\": 0 }\n" +
                    "  ],\n" +
                    "  \"vesselStatus\": {\n" +
                    "    \"x\": 0,\n" +
                    "    \"y\": 0,\n" +
                    "    \"time\": 0,\n" +
                    "    \"hSpeed\": 0,\n" +
                    "    \"vSpeed\": 0,\n" +
                    "    \"fuelMass\": 50.0,\n" +
                    "    \"state\": 0\n" +
                    "  }\n" +
                    "}",

                    "{\n" +
                    "  \"dryMass\": 250.0,\n" +
                    "  \"targetX\": 100,\n" +
                    "  \"relief\": [\n" +
                    "    { \"x\": -10000,\"y\": 0 },\n" +
                    "    { \"x\": 0,\"y\": 0 },\n" +
                    "    { \"x\": 5,\"y\": 10 },\n" +
                    "    { \"x\": 10,\"y\": 30 },\n" +
                    "    { \"x\": 15,\"y\": 23 },\n" +
                    "    { \"x\": 20,\"y\": 0 },\n" +
                    "    { \"x\": 10000,\"y\": 0 }\n" +
                    "  ],\n" +
                    "  \"vesselStatus\": {\n" +
                    "    \"x\": 0,\n" +
                    "    \"y\": 0,\n" +
                    "    \"time\": 0,\n" +
                    "    \"hSpeed\": 0,\n" +
                    "    \"vSpeed\": 0,\n" +
                    "    \"fuelMass\": 50.0,\n" +
                    "    \"state\": 0\n" +
                    "  }\n" +
                    "}",

                    "{\n" +
                    "  \"dryMass\": 250.0,\n" +
                    "  \"targetX\": 40,\n" +
                    "  \"relief\": [\n" +
                    "    { \"x\": -10000,\"y\": 0 },\n" +
                    "    { \"x\": 0,\"y\": 0 },\n" +
                    "    { \"x\": 5,\"y\": 0 },\n" +
                    "    { \"x\": 10,\"y\": 30 },\n" +
                    "    { \"x\": 15,\"y\": 7 },\n" +
                    "    { \"x\": 20,\"y\": 12 },\n" +
                    "    { \"x\": 25,\"y\": 17 },\n" +
                    "    { \"x\": 30,\"y\": 3 },\n" +
                    "    { \"x\": 35,\"y\": 0 },\n" +
                    "    { \"x\": 10000,\"y\": 0 }\n" +
                    "  ],\n" +
                    "  \"vesselStatus\": {\n" +
                    "    \"x\": 0,\n" +
                    "    \"y\": 0,\n" +
                    "    \"time\": 0,\n" +
                    "    \"hSpeed\": 0,\n" +
                    "    \"vSpeed\": 0,\n" +
                    "    \"fuelMass\": 50.0,\n" +
                    "    \"state\": 0\n" +
                    "  }\n" +
                    "}"
            };
}
