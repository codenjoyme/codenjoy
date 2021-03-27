package com.codenjoy.dojo.bomberman.services;

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

import static com.codenjoy.dojo.bomberman.services.Events.*;

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
        score += scoreFor(settings, event);
        score = Math.max(0, score);
    }

    public static int scoreFor(GameSettings settings, Object event) {
        if (DIED.equals(event)) {
            return - settings.diePenalty().getValue();
        }

        if (KILL_OTHER_HERO.equals(event)) {
            return settings.killOtherHeroScore().getValue();
        }

        if (KILL_MEAT_CHOPPER.equals(event)) {
            return settings.killMeatChopperScore().getValue();
        }

        if (KILL_DESTROY_WALL.equals(event)) {
            return settings.killWallScore().getValue();
        }

        if (CATCH_PERK.equals(event)) {
            return settings.catchPerkScore().getValue();
        }

        if (WIN_ROUND.equals(event)) {
            return settings.winRoundScore().getValue();
        }

        return 0;
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
