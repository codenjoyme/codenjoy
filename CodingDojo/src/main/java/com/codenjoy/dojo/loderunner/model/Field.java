package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Point;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 14:45
 */
public interface Field {
    boolean isBarrier(int x, int y);

    boolean tryToDrill(int x, int y);

    boolean isPit(int x, int y);

    Point getFreeRandom();

    boolean isLadder(int x, int y);

    boolean isPipe(int x, int y);
}
