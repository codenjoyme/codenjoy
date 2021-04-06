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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MaxScoresTest extends CumulativeScoresTest {

    @Override
    public PlayerScores getScores(int score) {
        return new MaxScores(score, settings);
    }

    @Test
    public void shouldCollectScores() {
        // given
        scores = new MaxScores(140, settings);

        assertScore(140);
        assertCurrentScore(0);

        // when
        someActions();

        // then
        assertScore(203);
        assertCurrentScore(203);

        // when
        // ignored but max score saved
        glassOverflown(1);
        glassOverflown(2);

        // then
        assertScore(203);
        assertCurrentScore(0);

        // when
        // insufficient action
        someActions();

        // then
        // nothing changed
        assertScore(203);
        assertCurrentScore(203);

        // when
        // a bit more
        isLinesRemoved(2, 2);
        figuresDropped(1, 2);

        // then
        // max score was increased
        assertScore(265);
        assertCurrentScore(265);

        // when
        // a bit more
        isLinesRemoved(2, 2);
        figuresDropped(1, 2);

        // then
        // max score was increased
        assertScore(327);
        assertCurrentScore(327);

        // when
        // ignored but max score saved
        glassOverflown(15);

        // then
        assertScore(327);
        assertCurrentScore(0);

        // when
        // insufficient action
        someActions();

        // then
        // nothing changed
        assertScore(327);
        assertCurrentScore(203);

        // when
        // one more time
        someActions();

        // then
        // new maximum result
        assertScore(406);
        assertCurrentScore(406);
    }

    @Test
    public void shouldClearScore() {
        // given
        scores = new MaxScores(140, settings);

        assertScore(140);
        assertCurrentScore(0);

        // when
        someActions();

        // then
        assertScore(203);
        assertCurrentScore(203);

        // when
        assertEquals(203, scores.clear());

        // then
        assertScore(0);
        assertCurrentScore(0);

        // when
        // insufficient action
        someActions();

        // then
        // nothing changed
        assertScore(203);
        assertCurrentScore(203);

        // when
        // a bit more
        isLinesRemoved(2, 2);
        figuresDropped(1, 2);

        // then
        // max score was increased
        assertScore(265);
        assertCurrentScore(265);

        // when
        assertEquals(265, scores.clear());

        // then
        assertScore(0);
        assertCurrentScore(0);
    }

    public void assertCurrentScore(int expected) {
        assertEquals(expected, ((MaxScores)scores).getCurrent());
    }

    public void someActions() {
        isLinesRemoved(1, 1);
        isLinesRemoved(1, 2);
        isLinesRemoved(2, 1);
        isLinesRemoved(2, 2);

        figuresDropped(1, 1);
        figuresDropped(1, 2);
        isLinesRemoved(2, 1);
        isLinesRemoved(2, 2);
    }

}
