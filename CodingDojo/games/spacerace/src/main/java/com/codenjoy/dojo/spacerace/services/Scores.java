package com.codenjoy.dojo.spacerace.services;

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

    private final Parameter<Integer> destroyBombScore;
    private final Parameter<Integer> destroyStoneScore;
    private final Parameter<Integer> destroyEnemyScore;
    private final Parameter<Integer> loosePenalty;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        destroyBombScore = settings.addEditBox("Destroy bomb score").type(Integer.class).def(30);
        destroyStoneScore = settings.addEditBox("Destroy stone score").type(Integer.class).def(10);
        destroyEnemyScore = settings.addEditBox("Destroy enemy score").type(Integer.class).def(500);
        loosePenalty = settings.addEditBox("Loose penalty").type(Integer.class).def(100);
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
        if (event.equals(Events.DESTROY_BOMB)) {
            score += destroyBombScore.getValue();
        } else if (event.equals(Events.DESTROY_STONE)) {
            score += destroyStoneScore.getValue();
        } else if (event.equals(Events.DESTROY_ENEMY)) {
            score += destroyEnemyScore.getValue();
        } else if (event.equals(Events.LOOSE)) {
            score -= loosePenalty.getValue();
        }
        score = Math.max(0, score);
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
