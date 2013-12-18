package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 5:02
 */
public interface Level {
    int getSize();

    List<Brick> getBricks();

    List<Point> getBorders();

    List<Hero> getHero();

    List<Point> getGold();

    List<Point> getLadder();

    List<Point> getPipe();
}
