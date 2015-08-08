package com.codenjoy.dojo.puzzlebox.model;

/**
 * Так случилось что у меня доска знает про героя, а герой про доску. И чтобы герой не знал про всю доску, я ему даю вот эту часть доски.
 */
public interface Field {


    // TODO
    boolean isBarrier(int x, int y);

    boolean isTarget(int x, int y);

    void fillEvent();
//
//    Point getFreeRandom();
//
//    boolean isFree(int x, int y);
//
//    boolean isBomb(int x, int y);
//
//    void setBomb(int x, int y);
//
//    void removeBomb(int x, int y);
}
