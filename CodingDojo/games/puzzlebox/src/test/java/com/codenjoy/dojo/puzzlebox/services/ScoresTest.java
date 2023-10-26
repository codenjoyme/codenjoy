package com.codenjoy.dojo.puzzlebox.services;

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


import com.codenjoy.dojo.puzzlebox.TestGameSettings;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.puzzlebox.services.GameSettings.Keys.FILL_SCORE;
import static com.codenjoy.dojo.puzzlebox.services.GameSettings.Keys.WIN_SCORE;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(WIN_SCORE, 1)
                .integer(FILL_SCORE, 2);
    }

    @Override
    protected Class<? extends ScoresMap> scores() {
        return Scores.class;
    }

    @Override
    protected Class<? extends Enum> eventTypes() {
        return Event.class;
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("100:\n" +
                "WIN > +1 = 101\n" +
                "WIN > +1 = 102\n" +
                "WIN > +1 = 103\n" +
                "WIN > +1 = 104\n" +
                "FILL > +2 = 106");
    }

    @Test
    public void shouldNotBeLessThanZero() {
        // given
        settings.integer(FILL_SCORE, -10);

        // when then
        assertEvents("0:\n" +
                "WIN > +1 = 1\n" +
                "WIN > +1 = 2\n" +
                "FILL > -2 = 0");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("100:\n" +
                "WIN > +1 = 101\n" +
                "(CLEAN) > -101 = 0\n" +
                "WIN > +1 = 1");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        // given
        settings.integer(WIN_SCORE, 1);

        // when then
        assertEvents("100:\n" +
                "WIN > +1 = 101\n" +
                "WIN > +1 = 102");
    }

    @Test
    public void shouldCollectScores_whenFill() {
        // given
        settings.integer(FILL_SCORE, 2);

        // when then
        assertEvents("100:\n" +
                "FILL > +2 = 102\n" +
                "FILL > +2 = 104");
    }
}