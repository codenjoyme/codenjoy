package com.codenjoy.dojo.tetris.services.scores;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.event.Calculator;
import com.codenjoy.dojo.services.event.ScoresImpl;
import com.codenjoy.dojo.tetris.TestGameSettings;
import com.codenjoy.dojo.tetris.services.Event;
import com.codenjoy.dojo.tetris.services.GameSettings;
import com.codenjoy.dojo.tetris.services.Scores;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.services.event.Mode.CUMULATIVELY;
import static org.junit.Assert.assertEquals;

public class CumulativeScoresTest {

    protected PlayerScores scores;
    protected GameSettings settings;

    public void linesRemoved(int level, int lines) {
        scores.event(Event.linesRemoved(level, lines));
    }

    public void figuresDropped(int level, int figure) {
        scores.event(Event.figuresDropped(level, figure));
    }

    public void glassOverflown(int level) {
        scores.event(Event.glassOverflown(level));
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
        settings.initScore(CUMULATIVELY);
    }

    protected void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Calculator<>(new Scores(settings)));
    }

    public void assertScore(int expected) {
        assertEquals(expected, scores.getScore());
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        linesRemoved(1, 1);
        linesRemoved(1, 2);
        linesRemoved(2, 1);
        linesRemoved(2, 2);

        figuresDropped(1, 1);
        figuresDropped(1, 2);
        linesRemoved(2, 1);
        linesRemoved(2, 2);

        glassOverflown(1);
        glassOverflown(2);

        // then
        assertEquals(313, scores.getScore());
    }

    @Test
    public void shouldNotLessThanZero() {
        // given
        givenScores(0);

        // when
        glassOverflown(1);

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);

        linesRemoved(1, 1);

        // when
        assertEquals(10, scores.clear());

        // then
        assertEquals(0, scores.getScore());
    }
}