package com.codenjoy.dojo.quake2d.services;

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


import com.codenjoy.dojo.quake2d.TestGameSettings;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.quake2d.services.GameSettings.Keys.INJURE_SCORE;
import static com.codenjoy.dojo.quake2d.services.GameSettings.Keys.KILL_SCORE;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(KILL_SCORE, 1)
                .integer(INJURE_SCORE, -1);
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
                "KILL > +1 = 101\n" +
                "KILL > +1 = 102\n" +
                "KILL > +1 = 103\n" +
                "INJURE > -1 = 102");
    }

    @Test
    public void shouldNotBeLessThanZero() {
        // given
        settings.integer(INJURE_SCORE, -10);

        // when
        assertEvents("15:\n" +
                "INJURE > -10 = 5\n" +
                "INJURE > -5 = 0\n" +
                "INJURE > +0 = 0");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("0:\n" +
                "KILL > +1 = 1\n" +
                "(CLEAN) > -1 = 0\n" +
                "KILL > +1 = 1");
    }

    @Test
    public void shouldCollectScores_whenKill() {
        // given
        settings.integer(KILL_SCORE, 1);

        // when then
        assertEvents("100:\n" +
                "KILL > +1 = 101\n" +
                "KILL > +1 = 102");
    }

    @Test
    public void shouldCollectScores_whenInjure() {
        // given
        settings.integer(INJURE_SCORE, -1);

        // when then
        assertEvents("100:\n" +
                "INJURE > -1 = 99\n" +
                "INJURE > -1 = 98");
    }
}