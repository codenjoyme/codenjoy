package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 5:06
 */
public class Brick extends PointImpl implements Tickable {

    public static final int DRILL_TIMER = 10;
    public static final Brick NULL = new Brick(new PointImpl(-1, -1));
    private int drill;

    public Brick(Point xy) {
        super(xy);
        drill = -1;
    }

    public void drill() {
        drill = 0;
    }

    public int drillCount() {
        return drill;
    }

    @Override
    public void tick() {
        if (drill != -1) {
            drill++;
        }
    }
}
