package com.codenjoy.dojo.football.model;

import com.codenjoy.dojo.football.model.elements.Ball;
import com.codenjoy.dojo.services.Point;

/**
 * Так случилось что у меня доска знает про героя, а герой про доску. И чтобы герой не знал про всю доску, я ему даю вот эту часть доски.
 */
public interface Field {

    boolean isBarrier(int x, int y);
    
    boolean isHero(int x, int y);
    
    boolean isWall(int x, int y);

    Point getFreeRandom();

	Point getFreeRandomOnMyHalf(Player player);

    boolean isFree(int x, int y);

	Ball getBall(int x, int y);

	boolean isBall(int x, int y);

}
