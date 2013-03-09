package com.codenjoy.dojo.snake.model.middle;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:23 AM
 */
public interface SnakeEventListener {
    void snakeIsDead();

    void snakeEatApple();

    void snakeEatStone();
}
