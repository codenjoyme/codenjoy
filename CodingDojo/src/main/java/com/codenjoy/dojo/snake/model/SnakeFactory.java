package com.codenjoy.dojo.snake.model;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 6:56 AM
 */
public interface SnakeFactory {
    Hero create(int x, int y);
}
