package com.codenjoy.dojo.loderunner.model;

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

    List<Gold> getGold();

    List<Ladder> getLadder();

    List<Pipe> getPipe();

    List<Enemy> getEnemies();
}
