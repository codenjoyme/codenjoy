package com.epam.dojo.expansion.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.DoubleDirection;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import org.json.JSONObject;

import java.util.Map;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * Created by Oleksandr_Baglai on 2017-08-29.
 */
public class Forces {

    private int count;
    private Point region;
    private DoubleDirection direction;

    public Forces(Point region, int count) {
        this.region = region;
        this.count = count;
    }

    public Forces(Point region, int count, DoubleDirection direction) {
        this(region, count);
        this.direction = direction;
    }

    public Forces(JSONObject json) {
        if (json.has("direction")) {
            direction = DoubleDirection.valueOf(json.getString("direction").toUpperCase());
        } else {
            direction = DoubleDirection.NONE;
        }

        count = json.getInt("count");
        JSONObject pt = json.getJSONObject("region");
        region = pt(pt.getInt("x"), pt.getInt("y"));
    }

    public Point getRegion() {
        return region;
    }

    public String getDirection() {
        return direction.toString();
    }

    public int getCount() {
        return count;
    }

    public String json() {
        String directionPart = (direction != null && direction != DoubleDirection.NONE) ?
                (",'direction':" + direction.name().toLowerCase()) :
                "";
        return "{'region':[" +
                region.getX() + "," + region.getY() +
                "],'count':" + count +
                directionPart + "}";
    }

    public Point getDestination(Point from) {
        if (direction == DoubleDirection.NONE) {
            return from;
        }
        return direction.change(from);
    }
}
