package com.codenjoy.dojo.minesweeper.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.PlayerScores;

import static com.codenjoy.dojo.minesweeper.services.GameSettings.Keys.*;

public class Scores implements PlayerScores {

    private volatile int score;
    private GameSettings settings;
    private volatile int destroyed;

    public Scores(int startScore, GameSettings settings) {
        this.score = startScore;
        this.settings = settings;
        destroyed = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Event.DESTROY_MINE)) {
            onDestroyMine();
        } else if (event.equals(Event.FORGET_CHARGE)) {
            onForgotCharge();
        } else if (event.equals(Event.KILL_ON_MINE)) {
            onKillOnMine();
        } else if (event.equals(Event.NO_MORE_CHARGE)) {
            onNoMoreCharge();
        } else if (event.equals(Event.WIN)) {
            onWin();
        } else if (event.equals(Event.CLEAN_BOARD)) {
            onClearBoard();
        }
        score = Math.max(0, score);
    }

    private void onClearBoard() {
        score += settings.integer(CLEAR_BOARD_SCORE);
    }

    private void onWin() {
        score += settings.integer(WIN_SCORE);
    }

    private void onNoMoreCharge() {
        onKillOnMine();
    }

    private void onDestroyMine() {
        destroyed++;
        score += destroyed;
    }

    private void onForgotCharge() {
        score -= settings.integer(DESTROYED_FORGOT_PENALTY);
        destroyed -= settings.integer(DESTROYED_PENALTY);
        destroyed = Math.max(0, destroyed);
    }

    private void onKillOnMine() {
        score -= settings.integer(GAME_OVER_PENALTY);
        destroyed = 0;
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
