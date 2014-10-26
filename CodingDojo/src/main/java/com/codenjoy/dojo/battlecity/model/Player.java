package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.battlecity.services.BattlecityEvents;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;

public class Player {
    public static final int TICKS_PER_BULLETS = 4;

    private Tank tank;
    private EventListener listener;
    private int maxScore;
    private int score;

    public Player(EventListener listener, Dice dice) {
        this.listener = listener;
        clearScore();
        tank = new Tank(0, 0, Direction.UP, dice, TICKS_PER_BULLETS);
    }

    public Tank getTank() {
        return tank;
    }

    private void increaseScore() {
        score = score + 1;
        maxScore = Math.max(maxScore, score);
    }

    public int getMaxScore() {
        return maxScore;
    }

    public int getScore() {
        return score;
    }

    public void event(BattlecityEvents event) {
        switch (event) {
            case KILL_OTHER_TANK: increaseScore(); break;
            case KILL_YOUR_TANK: gameOver(); break;
        }

        if (listener != null) {
            listener.event(event);
        }
    }

    private void gameOver() {
        tank.kill(null);
        score = 0;
    }

    public void clearScore() {   // TODO test me
        score = 0;
        maxScore = 0;
    }

    public void newHero(Battlecity tanks) {
        tank.removeBullets();
        tank.setField(tanks);
    }
}