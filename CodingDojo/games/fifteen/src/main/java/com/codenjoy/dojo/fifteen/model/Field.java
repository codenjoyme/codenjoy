package com.codenjoy.dojo.fifteen.model;

import com.codenjoy.dojo.services.Point;

/**
 * Так случилось что у меня доска знает про героя, а герой про доску. И чтобы герой не знал про всю доску, я ему даю вот эту часть доски.
 */
public interface Field {

    boolean isBarrier(int x, int y);

    Digit getDigit(int x, int y);

    Point getFreeRandom();

    boolean isFree(int x, int y);

}
