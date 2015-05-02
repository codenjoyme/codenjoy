package com.codenjoy.dojo.services;

public class NullJoystick implements Joystick {

    public static final Joystick INSTANCE = new NullJoystick();

    private NullJoystick() {
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
