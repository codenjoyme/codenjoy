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


import com.codenjoy.dojo.services.printer.Printer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.geom.Point2D;
import java.util.List;

public class LunoletPrinter implements Printer<JSONObject> {

    private Player player;

    public LunoletPrinter(Player player) {
        this.player = player;
    }

    @Override
    public JSONObject print(Object... parameters) {
        List<Point2D.Double> relief = player.getHero().getLevelRelief();
        List<Point2D.Double> history = player.getHero().getVesselHistory();
        VesselStatus vesselStatus = player.getHero().getVesselStatus();
        double lastAngle = player.getHero().getLastAngle();
        Point2D.Double target = player.getHero().getTarget();
        int levelNumber = player.getCurrentLevel();
        List<Point2D.Double> crashes = player.getCrashes();

        JSONObject result = new JSONObject();

        result.put("level", levelNumber);
        result.put("time", round3(vesselStatus.Time));
        result.put("x", round3(vesselStatus.X));
        result.put("y", round3(vesselStatus.Y));
        result.put("hspeed", round3(vesselStatus.HSpeed));
        result.put("vspeed", round3(vesselStatus.VSpeed));
        result.put("fuelmass", round3(vesselStatus.FuelMass));
        result.put("state", vesselStatus.State);
        result.put("angle", round3(lastAngle));
        result.put("consumption", round3(vesselStatus.Consumption));
        result.put("target", getJsonPoint(target));
        result.put("relief", getJsonPointArray(relief));
        result.put("history", getJsonPointArray(history));
        result.put("crashes", getJsonPointArray(crashes));

        return result;
    }

    private JSONObject getJsonPoint(Point2D.Double point) {
        JSONObject result = new JSONObject();
        result.put("x", round3(point.getX()));
        result.put("y", round3(point.getY()));
        return result;
    }

    private JSONArray getJsonPointArray(List<Point2D.Double> list) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            result.put(getJsonPoint(list.get(i)));
        }
        return result;
    }

    private static double round3(double v) {
        return Math.round(v * 1000.0) / 1000.0;
    }
}
