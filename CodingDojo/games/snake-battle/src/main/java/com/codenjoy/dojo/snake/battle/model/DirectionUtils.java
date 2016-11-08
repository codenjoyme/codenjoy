package com.codenjoy.dojo.snake.battle.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import static com.codenjoy.dojo.services.Direction.*;

/**
 * Created by ilya on 09.11.2016.
 */
public class DirectionUtils {

    static Point getPointAt(Point p, Direction d) {
        return new PointImpl(d.changeX(p.getX()), d.changeY(p.getY()));
    }

    static void movePointTo(Point p, Direction d) {
        p.move(d.changeX(p.getX()), d.changeY(p.getY()));
    }
}
