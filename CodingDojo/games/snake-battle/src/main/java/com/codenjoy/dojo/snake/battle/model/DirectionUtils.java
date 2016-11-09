package com.codenjoy.dojo.snake.battle.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import static com.codenjoy.dojo.services.Direction.*;

/**
 * @author Kors
 */
class DirectionUtils {

    static Point getPointAt(Point p, Direction d) {
        return new PointImpl(d.changeX(p.getX()), d.changeY(p.getY()));
    }

    static void movePointTo(Point p, Direction d) {
        p.move(d.changeX(p.getX()), d.changeY(p.getY()));
    }
}
