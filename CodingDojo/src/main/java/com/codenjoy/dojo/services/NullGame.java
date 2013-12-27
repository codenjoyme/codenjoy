package com.codenjoy.dojo.services;

import org.apache.commons.lang.StringUtils;

/**
 * User: sanja
 * Date: 27.12.13
 * Time: 22:22
 */
public class NullGame implements Game {

    NullGame() {
        // do nothing
    }

    @Override
    public Joystick getJoystick() {
        return Joystick.NULL;
    }

    @Override
    public int getMaxScore() {
        return 0; 
    }

    @Override
    public int getCurrentScore() {
        return 0; 
    }

    @Override
    public boolean isGameOver() {
        return false; 
    }

    @Override
    public void newGame() {
        // do nothing
    }

    @Override
    public String getBoardAsString() {
        return StringUtils.EMPTY;
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void clearScore() {
        // do nothing
    }

    @Override
    public void tick() {
        // do nothing
    }
}
