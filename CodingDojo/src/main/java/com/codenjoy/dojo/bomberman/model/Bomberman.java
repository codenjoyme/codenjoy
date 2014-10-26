package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.State;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 8:30 PM
 */
public interface Bomberman extends Joystick, Point, State<Elements,Player> {
    void apply();

    void kill();

    boolean itsMe(Point point);

    boolean isAlive();

    void init(Board board);
}
