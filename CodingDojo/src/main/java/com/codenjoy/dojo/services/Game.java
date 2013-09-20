package com.codenjoy.dojo.services;

import com.codenjoy.dojo.snake.model.Board;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 5:31 PM
 */
public interface Game extends Tickable {

    Joystick getJoystick();

    int getMaxScore();

    int getCurrentScore();

    boolean isGameOver();

    void newGame();

    String getBoardAsString();

    void destroy();

    void clearScore();
}
