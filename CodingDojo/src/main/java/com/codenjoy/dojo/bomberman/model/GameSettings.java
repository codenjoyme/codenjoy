package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/11/13
 * Time: 6:28 PM
 */
public interface GameSettings {
    Level getLevel();

    Walls getWalls();

    Bomberman getBomberman(Level level);

    int getBoardSize();
}
