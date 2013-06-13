package com.codenjoy.dojo.minesweeper.model.objects;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

/**
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 12:44 PM
 */
public class Mine extends PointImpl {

    public Mine(Point point) {
        super(point);
    }

    public Mine(int x, int y) {
        super(x, y);
    }
}
