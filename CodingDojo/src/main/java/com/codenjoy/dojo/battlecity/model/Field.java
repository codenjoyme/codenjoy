package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 18:58
 */
public interface Field {
    int getSize();

    List<Point> getBorders();

    List<Tank> getTanks();

    List<Construction> getConstructions();

    boolean isBarrier(int x, int y);

    boolean outOfField(int x, int y);

    void affect(Bullet bullet);
}
