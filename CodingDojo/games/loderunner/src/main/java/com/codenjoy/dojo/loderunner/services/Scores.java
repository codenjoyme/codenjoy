package com.codenjoy.dojo.loderunner.services;

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

    private final Parameter<Integer> killHeroPenalty;
    private final Parameter<Integer> killEnemyScore;
    private final Parameter<Integer> getGoldScore;
    private final Parameter<Integer> forNextGoldIncScore;

    private volatile int score;
    private volatile int count;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        killHeroPenalty = settings.addEditBox("Kill hero penalty").type(Integer.class).def(0);
        killEnemyScore = settings.addEditBox("Kill enemy score").type(Integer.class).def(10);
        getGoldScore = settings.addEditBox("Get gold score").type(Integer.class).def(1);
        forNextGoldIncScore = settings.addEditBox("Get next gold increment score").type(Integer.class).def(1);
    }

    @Override
    public int clear() {
        count = 0;
        return score = 0;
    }

    @Override
    public Integer getScore() {
        return score;
    }

    @Override
    public void event(Object event) {
        if (event.equals(Events.GET_GOLD)) {
            score += getGoldScore.getValue() + count;
            count += forNextGoldIncScore.getValue();
        } else if (event.equals(Events.KILL_ENEMY)) {
            score += killEnemyScore.getValue();
        } else if (event.equals(Events.KILL_HERO)) {
            count = 0;
            score -= killHeroPenalty.getValue();
        }
        score = Math.max(0, score);
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
