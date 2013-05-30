package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 30.05.13
 * Time: 18:44
 */
public class LazyJoystick implements Joystick {

    private Joystick joystick;

    public void setJoystick(Joystick joystick) {
        this.joystick = joystick;
    }

    @Override
    public void down() {
        joystick.down();
    }

    @Override
    public void up() {
        joystick.up();
    }

    @Override
    public void left() {
        joystick.left();
    }

    @Override
    public void right() {
        joystick.right();
    }

    @Override
    public void act() {
        joystick.act();
    }

    public Joystick getJoystick() {
        return joystick;
    }
}
