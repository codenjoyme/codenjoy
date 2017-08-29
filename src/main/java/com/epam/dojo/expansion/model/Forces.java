package com.epam.dojo.expansion.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.DoubleDirection;
import com.codenjoy.dojo.services.Point;
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

    public DoubleDirection getDirection() {
        return direction;
    }

    public int getCount() {
        return count;
    }
}
