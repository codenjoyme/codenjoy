package com.codenjoy.dojo.hex.services;

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


import com.codenjoy.dojo.hex.TestGameSettings;
import com.codenjoy.dojo.services.event.EventObject;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.hex.services.GameSettings.Keys.LOSE_PENALTY;
import static com.codenjoy.dojo.hex.services.GameSettings.Keys.WIN_SCORE;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings();
    }

    @Override
    protected Class<? extends ScoresMap> scores() {
        return Scores.class;
    }

    @Override
    protected Class<? extends EventObject> events() {
        return Event.class;
    }

    @Override
    protected Class<? extends Enum> eventTypes() {
        return Event.Type.class;
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("100:\n" +
                "WIN,1 > +2 = 102\n" +
                "WIN,2 > +4 = 106\n" +
                "LOSE,1 > -1 = 105");
    }

    @Test
    public void shouldNotLessThanZero() {
        assertEvents("0:\n" +
                "LOSE,1 > +0 = 0");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("100:\n" +
                "WIN,1 > +2 = 102\n" +
                "WIN,2 > +4 = 106\n" +
                "(CLEAN) > -106 = 0\n" +
                "WIN,1 > +2 = 2\n" +
                "WIN,2 > +4 = 6");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        // given
        settings.integer(WIN_SCORE, 2);

        // when then
        assertEvents("100:\n" +
                "WIN,1 > +2 = 102\n" +
                "WIN,2 > +4 = 106");
    }

    @Test
    public void shouldCollectScores_whenLose() {
        // given
        settings.integer(LOSE_PENALTY, -1);

        // when then
        assertEvents("100:\n" +
                "LOSE,1 > -1 = 99\n" +
                "LOSE,2 > -2 = 97");
    }
}