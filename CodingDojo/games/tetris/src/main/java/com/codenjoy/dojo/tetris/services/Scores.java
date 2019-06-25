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

    private final Parameter<Integer> oneLineRemovedScore;
    private final Parameter<Integer> twoLinesRemovedScore;
    private final Parameter<Integer> threeLinesRemovedScore;
    private final Parameter<Integer> fourLinesRemovedScore;
    private final Parameter<Integer> figureDroppedScore;
    private final Parameter<Integer> glassOverflownPenalty;

    private volatile int score;

    public Scores(int score, Settings settings) {
        this.score = score;

        figureDroppedScore = settings.addEditBox("Figure dropped score score").type(Integer.class).def(1);
        oneLineRemovedScore = settings.addEditBox("One line removed score").type(Integer.class).def(10);
        twoLinesRemovedScore = settings.addEditBox("Two lines removed score").type(Integer.class).def(30);
        threeLinesRemovedScore = settings.addEditBox("Three lines removed score").type(Integer.class).def(50);
        fourLinesRemovedScore = settings.addEditBox("Four lines removed score").type(Integer.class).def(100);
        glassOverflownPenalty = settings.addEditBox("Glass overflown penalty").type(Integer.class).def(10);
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
        score += figureDroppedScore.getValue() * figureIndex;
    }

    private void glassOverflown(int level) {
        score -= glassOverflownPenalty.getValue() * level;
    }

    private void linesRemoved(int level, int count) {
        switch (count) {
            case 1:
                score += oneLineRemovedScore.getValue() * level;
                break;
            case 2:
                score += twoLinesRemovedScore.getValue() * level;
                break;
            case 3:
                score += threeLinesRemovedScore.getValue() * level;
                break;
            case 4:
                score += fourLinesRemovedScore.getValue() * level;
                break;
        }
    }

    @Override
    public void update(Object score) {
        this.score = Integer.valueOf(score.toString());
    }
}
