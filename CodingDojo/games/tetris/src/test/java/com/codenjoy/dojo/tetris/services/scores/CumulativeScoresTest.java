package com.codenjoy.dojo.tetris.services.scores;

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
import com.codenjoy.dojo.tetris.services.Events;
import com.codenjoy.dojo.tetris.services.GameSettings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CumulativeScoresTest {

    protected PlayerScores scores;
    protected GameSettings settings;

    public void isLinesRemoved(int level, int lines) {
        scores.event(Events.linesRemoved(level, lines));
    }

    public void figuresDropped(int level, int figure) {
        scores.event(Events.figuresDropped(level, figure));
    }

    public void glassOverflown(int level) {
        scores.event(Events.glassOverflown(level));
    }

    @Before
    public void setup() {
        settings = new GameSettings();
        scores = getScores(0);
    }

    public PlayerScores getScores(int score) {
        return new CumulativeScores(score, settings);
    }

    public void assertScore(int expected) {
        assertEquals(expected, scores.getScore());
    }

    @Test
    public void shouldCollectScores() {
        scores = getScores(140);

        isLinesRemoved(1, 1);
        isLinesRemoved(1, 2);
        isLinesRemoved(2, 1);
        isLinesRemoved(2, 2);

        figuresDropped(1, 1);
        figuresDropped(1, 2);
        isLinesRemoved(2, 1);
        isLinesRemoved(2, 2);

        glassOverflown(1);
        glassOverflown(2);

        assertEquals(313, scores.getScore());
    }

    @Test
    public void shouldNotLessThanZero() {
        glassOverflown(1);

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        isLinesRemoved(1, 1);

        assertEquals(10, scores.clear());

        assertEquals(0, scores.getScore());
    }
}