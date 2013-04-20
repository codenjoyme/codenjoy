package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Joystick;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 8:30 PM
 */
public interface Bomberman extends Joystick, IPoint {
    void apply();

    void kill();

    boolean itsMe(IPoint point);

    boolean isAlive();

    void init(Board board);
}
