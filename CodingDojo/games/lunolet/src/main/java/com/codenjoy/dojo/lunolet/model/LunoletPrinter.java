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


import com.codenjoy.dojo.services.printer.Printer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.geom.Point2D;
import java.util.List;

public class LunoletPrinter implements Printer<JSONObject> {

    private LunoletGame game;
    private Player player;

    public LunoletPrinter(LunoletGame game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public JSONObject print() {
        List<Point2D.Double> relief = player.getHero().getLevelRelief();
        List<Point2D.Double> history = player.getHero().getVesselHistory();
        Point2D.Double vesselPoint = player.getHero().getVesselPoint();

        JSONObject result = new JSONObject();
        result.put("vesselPoint", getJsonPoint(vesselPoint));
        result.put("relief", getJsonPointArray(relief));
        result.put("history", getJsonPointArray(history));

        return result;
    }

    private JSONObject getJsonPoint(Point2D.Double point) {
        JSONObject result = new JSONObject();
        result.put("x", point.getX());
        result.put("y", point.getY());
        return result;
    }

    private JSONArray getJsonPointArray(List<Point2D.Double> list) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            result.put(getJsonPoint(list.get(i)));
        }
        return result;
    }
}
