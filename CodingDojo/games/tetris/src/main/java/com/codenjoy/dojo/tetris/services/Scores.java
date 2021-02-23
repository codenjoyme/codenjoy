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

import static com.codenjoy.dojo.tetris.services.GameSettings.Keys.*;

public class Scores implements PlayerScores {

    private volatile int score;

    private GameSettings settings;

    public Scores(int score, GameSettings settings) {
        this.score = score;
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
    public void event(Object object) {
        Events event = (Events)object;
        if (event.isLinesRemoved()) {
            linesRemoved(event.getLevel(), event.getRemovedLines());
        } else if (event.isFiguresDropped()) {
            figureDropped(event.getFigureIndex());
        } else if (event.isGlassOverflown()) {
            glassOverflown(event.getLevel());
        }
        score = Math.max(0, score);
    }

    private void figureDropped(int figureIndex) {
        score += settings.integer(FIGURE_DROPPED_SCORE) * figureIndex;
    }

    private void glassOverflown(int level) {
        score -= settings.integer(GLASS_OVERFLOWN_PENALTY) * level;
    }

    private void linesRemoved(int level, int count) {
        switch (count) {
            case 1:
                score += settings.integer(ONE_LINE_REMOVED_SCORE) * level;
                break;
            case 2:
                score += settings.integer(TWO_LINES_REMOVED_SCORE) * level;
                break;
            case 3:
                score += settings.integer(THREE_LINES_REMOVED_SCORE) * level;
                break;
            case 4:
                score += settings.integer(FOUR_LINES_REMOVED_SCORE) * level;
                break;
        }
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
