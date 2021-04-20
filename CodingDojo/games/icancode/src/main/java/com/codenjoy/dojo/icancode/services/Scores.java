package com.codenjoy.dojo.icancode.services;

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

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;

public class Scores implements PlayerScores {

    private volatile int score;
    private GameSettings settings;

    public Scores(int startScore, GameSettings settings) {
        this.score = startScore;
        this.settings = settings;
    }

    @Override
    public int clear() {
        score = 0;
        return 0;
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }

    @Override
    public Object getScore() {
        return score;
    }

    @Override
    public void event(Object input) {
        Events events = (Events) input;

        Events.Type eventsType = events.getType();
        switch (eventsType) {
            case WIN:
                if (!events.isMultiple()) {
                    score += settings.integer(WIN_SCORE);
                }
                score += settings.integer(GOLD_SCORE) * events.getGoldCount();
                break;
            case LOSE:
                score -= settings.integer(LOSE_PENALTY);
                break;
            case KILL_ZOMBIE:
                if (settings.bool(ENABLE_KILL_SCORE) && events.isMultiple())
                    score += events.getKillCount() * settings.integer(KILL_ZOMBIE_SCORE);
                break;
            case KILL_HERO:
                if (settings.bool(ENABLE_KILL_SCORE) && events.isMultiple())
                    score += events.getKillCount() * settings.integer(KILL_HERO_SCORE);
                break;
        }
        score = Math.max(0, score);
    }
}
