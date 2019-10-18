package com.codenjoy.dojo.battlecity.services;

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

    private final Parameter<Integer> killYourTankPenalty;
    private final Parameter<Integer> killOtherHeroTankScore;
    private final Parameter<Integer> killOtherAITankScore;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        killYourTankPenalty = settings.addEditBox("Kill your tank penalty").type(Integer.class).def(0);
        killOtherHeroTankScore = settings.addEditBox("Kill other hero tank score").type(Integer.class).def(50);
        killOtherAITankScore = settings.addEditBox("Kill other AI tank score").type(Integer.class).def(25);
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
    public void event(Object object) {
        if (!(object instanceof Events))
            return;
        Events event = (Events)object;
        if (event.isKillYourTank()) {
            score -= killYourTankPenalty.getValue();
        } else if (event.isKillOtherHeroTank()) {
            score += killOtherHeroTankScore.getValue() * event.getAmount();
        } else if (event.isKillOtherAITank()) {
            score += killOtherAITankScore.getValue();
        }

        score = Math.max(0, score);
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
