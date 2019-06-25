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
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class Scores implements PlayerScores {

    private final Parameter<Integer> gameOverPenalty;
    private final Parameter<Integer> destroyedPenalty;
    private final Parameter<Integer> destroyedForgotPenalty;
    private final Parameter<Integer> winScore;
    private final Parameter<Integer> clearBoardScore;

    private volatile int score;
    private volatile int destroyed;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;
        destroyed = 0;

        gameOverPenalty = settings.addEditBox("Game over penalty").type(Integer.class).def(15);
        destroyedPenalty = settings.addEditBox("Forgot penalty").type(Integer.class).def(5);
        destroyedForgotPenalty = settings.addEditBox("Destoyed forgot penalty").type(Integer.class).def(2);
        winScore = settings.addEditBox("Win score").type(Integer.class).def(300);
        clearBoardScore = settings.addEditBox("Clear board score").type(Integer.class).def(1);
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
        if (event.equals(Events.DESTROY_MINE)) {
            onDestroyMine();
        } else if (event.equals(Events.FORGET_CHARGE)) {
            onForgotCharge();
        } else if (event.equals(Events.KILL_ON_MINE)) {
            onKillOnMine();
        } else if (event.equals(Events.NO_MORE_CHARGE)) {
            onNoMoreCharge();
        } else if (event.equals(Events.WIN)) {
            onWin();
        } else if (event.equals(Events.CLEAN_BOARD)) {
            onClearBoard();
        }
        score = Math.max(0, score);
    }

    private void onClearBoard() {
        score += clearBoardScore.getValue();
    }

    private void onWin() {
        score += winScore.getValue();
    }

    private void onNoMoreCharge() {
        onKillOnMine();
    }

    private void onDestroyMine() {
        destroyed++;
        score += destroyed;
    }

    private void onForgotCharge() {
        score -= destroyedPenalty.getValue();
        destroyed -= destroyedForgotPenalty.getValue();
        destroyed = Math.max(0, destroyed);
    }

    private void onKillOnMine() {
        score -= gameOverPenalty.getValue();
        destroyed = 0;
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
