package com.codenjoy.dojo.pong.services;

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

import com.codenjoy.dojo.pong.TestGameSettings;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.pong.services.GameSettings.Keys.WIN_SCORE;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(WIN_SCORE, 50);
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
    public void shouldCollectScores_whenWin() {
        // when then
        assertEvents("140:\n" +
                "WIN > +50 = 190\n" +
                "WIN > +50 = 240\n" +
                "WIN > +50 = 290\n" +
                "WIN > +50 = 340");
    }

    @Test
    public void shouldCollectScoresIfMoreThanOne_caseMultiple() {
        // given
        settings.integer(WIN_SCORE, -100);

        // when then
        assertEvents("140:\n" +
                "WIN > -100 = 40\n" +
                "WIN > -40 = 0");
    }
}