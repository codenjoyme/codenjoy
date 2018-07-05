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
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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

        String levelsFileContent = readResourceFile("levels.json");

        // remove license header
        String jsonLevels = levelsFileContent.replaceFirst("/\\*[\\d\\D]+\\*/", "");

        JSONArray levelsArray = new JSONArray(jsonLevels);
        for (int i = 0; i < levelsArray.length(); i++) {
            JSONObject level = levelsArray.getJSONObject(i);
            levels.add(parseLevel(level));
        }
    }

    private String readResourceFile(String fileName) {
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(is);
        StringBuilder sb = new StringBuilder();
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        try {
            while ((bytesRead = bis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, bytesRead, "windows-1251"));
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch(IOException ioe) {
            System.out.println("Exception while reading the file " + ioe);
        }
        finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException ioe) {
                System.out.println("Error while closing the stream : " + ioe);
            }
        }
        return sb.toString();
    }

    private Level parseLevel(JSONObject jsonLevel) {
        Level result = new Level();
        result.DryMass = jsonLevel.getDouble("dryMass");
        result.TargetX = jsonLevel.getDouble("targetX");
        JSONArray reliefJson = jsonLevel.getJSONArray("relief");
        result.Relief = parseRelief(reliefJson);
        JSONObject vesselJson = jsonLevel.getJSONObject("vesselStatus");
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
}
