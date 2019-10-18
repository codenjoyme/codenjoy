package com.codenjoy.dojo.snakebattle.services;

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

    private final Parameter<Integer> winScore;
    private final Parameter<Integer> appleScore;
    private final Parameter<Integer> goldScore;
    private final Parameter<Integer> diePenalty;
    private final Parameter<Integer> stoneScore;
    private final Parameter<Integer> eatScore;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        winScore = settings.addEditBox("Win score").type(Integer.class).def(50);
        appleScore = settings.addEditBox("Apple score").type(Integer.class).def(1);
        goldScore = settings.addEditBox("Gold score").type(Integer.class).def(10);
        diePenalty = settings.addEditBox("Die penalty").type(Integer.class).def(0);
        stoneScore = settings.addEditBox("Stone score").type(Integer.class).def(5);
        eatScore = settings.addEditBox("Eat enemy score").type(Integer.class).def(10);
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
        if (event.isWin()) {
            score += winScore.getValue();
        } else if (event.isApple()) {
            score += appleScore.getValue();
        } else if (event.isGold()) {
            score += goldScore.getValue();
        } else if (event.isDie()) {
            score -= diePenalty.getValue();
        } else if (event.isStone()) {
            score += stoneScore.getValue();
        } else if (event.isEat()) {
            score += eatScore.getValue() * event.getAmount();
        }
        score = Math.max(0, score);
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
