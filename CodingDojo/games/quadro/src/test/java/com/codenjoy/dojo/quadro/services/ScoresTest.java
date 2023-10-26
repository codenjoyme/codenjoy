package com.codenjoy.dojo.quadro.services;

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


import com.codenjoy.dojo.quadro.TestGameSettings;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.quadro.services.GameSettings.Keys.*;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(WIN_SCORE, 10)
                .integer(LOSE_PENALTY, -1)
                .integer(DRAW_SCORE, 3);
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
                "WIN > +10 = 110\n" +
                "WIN > +10 = 120\n" +
                "WIN > +10 = 130\n" +
                "DRAW > +3 = 133\n" +
                "LOSE > -1 = 132");
    }

    @Test
    public void shouldNotBeLessThanZero() {
        assertEvents("1:\n" +
                "LOSE > -1 = 0\n" +
                "LOSE > +0 = 0\n" +
                "LOSE > +0 = 0");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("0:\n" +
                "WIN > +10 = 10\n" +
                "(CLEAN) > -10 = 0\n" +
                "WIN > +10 = 10");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        // given
        settings.integer(WIN_SCORE, 10);

        // when then
        assertEvents("100:\n" +
                "WIN > +10 = 110\n" +
                "WIN > +10 = 120");
    }

    @Test
    public void shouldCollectScores_whenDraw() {
        // given
        settings.integer(DRAW_SCORE, 3);

        // when then
        assertEvents("100:\n" +
                "DRAW > +3 = 103\n" +
                "DRAW > +3 = 106");
    }

    @Test
    public void shouldCollectScores_whenLose() {
        // given
        settings.integer(LOSE_PENALTY, -1);

        // when then
        assertEvents("100:\n" +
                "LOSE > -1 = 99\n" +
                "LOSE > -1 = 98");
    }
}
