package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.Point;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 14:45
 */
public interface Field {
    boolean isBarrier(int x, int y);

    Point getFreeRandom();

    boolean isFree(int x, int y);

    boolean isBomb(int x, int y);

    void setBomb(int x, int y);

    void removeBomb(int x, int y);
}
