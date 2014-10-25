package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

/**
 * Created by Sanja on 25.10.2014.
 */
public class Border extends PointImpl {
    public Border(int x, int y) {
        super(x, y);
    }

    public Border(Point point) {
        super(point);
    }
}
