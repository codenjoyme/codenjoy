package com.codenjoy.dojo.clifford.services;

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

import static com.codenjoy.dojo.clifford.services.GameSettings.Keys.*;

public class Scores implements PlayerScores {

    private volatile int score;
    private GameSettings settings;
    private volatile int countRing;
    private volatile int countGlove;
    private volatile int countKnife;

    public Scores(int startScore, GameSettings settings) {
        this.score = startScore;
        this.settings = settings;
    }

    @Override
    public int clear() {
        clearSeries();
        return score = 0;
    }

    private void clearSeries() {
        countRing = 0;
        countGlove = 0;
        countKnife = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.GET_KNIFE_CLUE)) {
            score += settings.integer(CLUE_SCORE_KNIFE) + countKnife;
            countKnife += settings.integer(CLUE_SCORE_KNIFE_INCREMENT);
        } else if (event.equals(Events.GET_GLOVE_CLUE)) {
            score += settings.integer(CLUE_SCORE_GLOVE) + countGlove;
            countGlove += settings.integer(CLUE_SCORE_GLOVE_INCREMENT);
        } else if (event.equals(Events.GET_RING_CLUE)) {
            score += settings.integer(CLUE_SCORE_RING) + countRing;
            countRing += settings.integer(CLUE_SCORE_RING_INCREMENT);
        } else if (event.equals(Events.KILL_ROBBER)) {
            score += settings.integer(KILL_ROBBER_SCORE);
        } else if (event.equals(Events.KILL_HERO)) {
            clearSeries();
            score -= settings.integer(KILL_HERO_PENALTY);
        } else if (event.equals(Events.SUICIDE)) {
            clearSeries();
            score -= settings.integer(SUICIDE_PENALTY);
        }
        score = Math.max(0, score);
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }

    @Override
    public String toString() {
        return "Scores{" +
                "score=" + score +
                ", ring=" + countRing +
                ", glove=" + countGlove +
                ", knife=" + countKnife +
                '}';
    }
}