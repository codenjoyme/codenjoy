package com.codenjoy.dojo.lunolet.client;

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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.lunolet.model.VesselState;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Board implements ClientBoard {
    protected JSONObject source;

    int Level;
    double Time;
    double X;
    double Y;
    double HSpeed;
    double VSpeed;
    double FuelMass;
    String State;
    double Angle;
    Point2D.Double Target;
    ArrayList<Point2D.Double> Relief;
    ArrayList<Point2D.Double> History;

    @Override
    public ClientBoard forString(String boardString) {
        source = new JSONObject(boardString);

        Level = source.getInt("level");
        Time = source.getDouble("time");
        X = source.getDouble("x");
        Y = source.getDouble("y");
        HSpeed = source.getDouble("hspeed");
        VSpeed = source.getDouble("vspeed");
        FuelMass = source.getDouble("fuelmass");
        State = source.getString("state");
        Angle = source.getDouble("angle");
        Target = getPoint(source, "target");
        Relief = getPointList(source, "relief");
        History = getPointList(source, "history");

        return this;
    }

    public double getTime() {
        return Time;
    }

    public Point2D.Double getPoint() {
        return new Point2D.Double(X, Y);
    }

    public double getHSpeed() {
        return HSpeed;
    }

    public double getVSpeed() {
        return VSpeed;
    }

    public Point2D.Double getTarget() {
        return Target;
    }

    public VesselState getState() {
        return VesselState.valueOf(State);
    }

    public double getAngle() { return Angle; }

    public double getFuelMass() {
        return FuelMass;
    }

    public ArrayList<Point2D.Double> getRelief() {
        return Relief;
    }

    public ArrayList<Point2D.Double> getHistory() {
        return History;
    }

    private static Point2D.Double getPoint(JSONObject source, String key) {
        JSONObject obj = source.getJSONObject(key);
        return new Point2D.Double(
                obj.getDouble("x"), obj.getDouble("y")
        );
    }

    private static ArrayList<Point2D.Double> getPointList(JSONObject source, String key) {
        JSONArray arr = source.getJSONArray(key);
        ArrayList<Point2D.Double> result = new ArrayList<Point2D.Double>(arr.length());
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            result.add(new Point2D.Double(
                    obj.getDouble("x"), obj.getDouble("y")
            ));
        }
        return result;
    }
}
