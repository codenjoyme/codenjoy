package com.codenjoy.dojo.services;

import org.apache.commons.lang.StringUtils;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class NullGame implements Game {

    public static final Game INSTANCE = new NullGame();

    private NullGame() {
        // do nothing
    }

    @Override
    public Joystick getJoystick() {
        return NullJoystick.INSTANCE;
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
    public Point getHero() {
        return pt(-1, -1);
    }

    @Override
    public void tick() {
        // do nothing
    }
}
