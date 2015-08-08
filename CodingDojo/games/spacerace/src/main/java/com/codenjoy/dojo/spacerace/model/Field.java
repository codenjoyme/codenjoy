package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.Point;

/**
 * Так случилось что у меня доска знает про героя, а герой про доску. И чтобы герой не знал про всю доску, я ему даю вот эту часть доски.
 */
public interface Field {

    boolean isBarrier(int x, int y);

    Point getFreeRandom();

    boolean isFree(int x, int y);

    boolean isBomb(int x, int y);

    void setBomb(int x, int y);

    void removeBomb(int x, int y);

    void addBullet(int x, int y, Hero hero);
}
