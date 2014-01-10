package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 05.06.13
 * Time: 18:44
 */
public class NullJoystick implements Joystick {

    NullJoystick() {
        // do nothing
    }

    @Override
    public void down() {
        // do nothing
    }

    @Override
    public void up() {
        // do nothing
    }

    @Override
    public void left() {
        // do nothing
    }

    @Override
    public void right() {
        // do nothing
    }

    @Override
    public void act(int... p) {
        // do nothing
    }
}
