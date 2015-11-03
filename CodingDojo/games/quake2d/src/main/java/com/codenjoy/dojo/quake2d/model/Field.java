package com.codenjoy.dojo.quake2d.model;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.Point;

/**
 * Так случилось что у меня доска знает про героя, а герой про доску. И чтобы герой не знал про всю доску, я ему даю вот эту часть доски.
 */
public interface Field {

    boolean isBarrier(int x, int y);

    Point getFreeRandom();

    boolean isFree(int x, int y);

    boolean isBulletHitHero(int x, int y);

    boolean catchAbility(int x, int y);

//    void setBomb(int x, int y);
//
//    void removeBomb(int x, int y);



    void fireBullet(int x, int y, Direction direction, Field field, Hero hero);

    void newRobot(int x, int y);


//    void fireBullet(int x, int y, com.codenjoy.dojo.services.Direction direction, Field field, Hero hero);
}
