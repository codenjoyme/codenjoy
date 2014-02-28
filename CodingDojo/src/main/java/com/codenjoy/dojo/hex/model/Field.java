package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.Point;

public interface Field {

    boolean isBarrier(int x, int y);

    Point getFreeRandom();

    boolean isFree(int x, int y);

    boolean isBomb(int x, int y);

    void setBomb(int x, int y);

    void removeBomb(int x, int y);
}
