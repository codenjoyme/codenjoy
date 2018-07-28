package com.codenjoy.dojo.tetris.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

    private final Parameter<Integer> lineRemovedScore;
    private final Parameter<Integer> glassOverflownPenalty;

    private volatile int score;

    public Scores(int startScore, Settings settings) {
        this.score = startScore;

        lineRemovedScore = settings.addEditBox("Lines removed score").type(Integer.class).def(10);
        glassOverflownPenalty = settings.addEditBox("Glass overflown penalty").type(Integer.class).def(100);
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
        Events event = (Events)object;
        if (event.isLinesRemoved()) {
            score += getMultiplier(event.getData()) * lineRemovedScore.getValue();
        } else if (event.isGlassOverflown()) {
            score -= glassOverflownPenalty.getValue();
        }
        score = Math.max(0, score);
    }

    private int getMultiplier(int lines) {
        switch (lines) {
            case 1 : return 1;
            case 2 : return 3;
            case 3 : return 5;
            case 4 : return 7;
            default: return 0;
        }
    }
}
