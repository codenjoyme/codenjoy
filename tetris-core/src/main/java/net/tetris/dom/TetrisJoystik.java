package net.tetris.dom;

import com.codenjoy.dojo.services.Joystick;

/**
 * User: serhiy.zelenin
 * Date: 6/21/12
 * Time: 6:09 PM
 */
public interface TetrisJoystik extends Joystick {
    void left(int delta);

    void right(int delta);

    void down();

    void act(int times);
}
