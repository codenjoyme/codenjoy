package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.battlecity.services.Events;
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

    public void event(Events event) {
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