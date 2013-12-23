package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 5:02
 */
public interface Level {
    int getSize();

    List<Point> getWalls();

    List<Hero> getHero();

    List<Point> getGold();
}
