package net.tetris.dom;

/**
 * User: serhiy.zelenin
 * Date: 6/21/12
 * Time: 6:09 PM
 */
public interface Joystick {
    void left(int delta);

    void right(int delta);

    void down();

    void act(int times);
}
