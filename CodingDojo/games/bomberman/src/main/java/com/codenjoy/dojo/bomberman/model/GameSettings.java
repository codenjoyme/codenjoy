package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.settings.Parameter;

/**
 * User: oleksandr.baglai
 * Date: 3/11/13
 * Time: 6:28 PM
 */
public interface GameSettings {
    Level getLevel();

    Walls getWalls(Bomberman board);

    Hero getBomberman(Level level);

    Parameter<Integer> getBoardSize();
}
