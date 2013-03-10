package com.codenjoy.dojo.snake.services;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:57 AM
 */
public enum SnakePlotColor {
    BAD_APPLE, GOOD_APPLE, BREAK, SPACE,
    HEAD_DOWN, HEAD_LEFT, HEAD_RIGHT, HEAD_UP,
    TAIL_END_DOWN, TAIL_END_LEFT, TAIL_END_UP, TAIL_END_RIGHT,
    TAIL_HORIZONTAL, TAIL_VERTICAL,
    TAIL_LEFT_DOWN, TAIL_LEFT_UP, TAIL_RIGHT_DOWN, TAIL_RIGHT_UP;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
