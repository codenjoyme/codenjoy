package net.tetris.dom;

/**
 * User: serhiy.zelenin
 * Date: 6/21/12
 * Time: 6:09 PM
 */
public interface Joystick {
    void moveLeft(int delta);

    void moveRight(int delta);

    void drop();

    void rotate(int times);
}
