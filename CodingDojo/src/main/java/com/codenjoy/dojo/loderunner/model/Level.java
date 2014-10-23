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

    List<Border> getBorders();

    List<Hero> getHeroes();

    List<Point> getGold();

    List<Ladder> getLadder();

    List<Pipe> getPipe();

    List<Enemy> getEnemies();
}
