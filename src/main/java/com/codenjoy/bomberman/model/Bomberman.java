package com.codenjoy.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:24 AM
 */
public interface Bomberman {
    int getX();

    int getY();

    void right();

    void down();

    void up();

    void left();

    void bomb();

    boolean isAlive();
}
