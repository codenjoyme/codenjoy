package com.codenjoy.dojo.snake.services;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:57 AM
 */
public enum SnakePlotColor {
    HEAD, TAIL, BODY, STONE, APPLE, SPACE, BREAK;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
