package com.codenjoy.dojo.tetris.model;

/**
 * User: oleksandr.baglai
 * Date: 9/29/12
 * Time: 1:36 PM
 */
public interface ChangeLevelListener {
    void levelChanged(int levelNumber, GameLevel level);
}
