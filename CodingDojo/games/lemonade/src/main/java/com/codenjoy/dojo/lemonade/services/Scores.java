package com.codenjoy.dojo.lemonade.services;

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

import static com.codenjoy.dojo.lemonade.services.GameSettings.Keys.BANKRUPT_PENALTY;

public class Scores implements PlayerScores {

    private volatile int score;
    private GameSettings settings;

    public Scores(int startScore, GameSettings settings) {
        this.score = startScore;
        this.settings = settings;
    }

    @Override
    public int clear() {
        return score = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        EventArgs eventArgs = (EventArgs) event;
        ScoreMode mode = settings.scoreMode();
        switch (eventArgs.type) {
            case WIN:
                if (mode == ScoreMode.SUM_OF_PROFITS)
                    score += toScore(eventArgs.profit);
                if (mode == ScoreMode.LAST_DAY_ASSETS)
                    score = Math.max(score, toScore(eventArgs.assetsAfter));
                break;
            case LOSE:
                if (mode == ScoreMode.SUM_OF_PROFITS)
                    score -= settings.integer(BANKRUPT_PENALTY);
                break;
        }
        score = Math.max(0, score);
    }

    private int toScore(double value) {
        return (int) (100 * value);
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }

}
