package com.codenjoy.dojo.moebius.services;

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


import com.codenjoy.dojo.moebius.TestGameSettings;
import com.codenjoy.dojo.services.event.EventObject;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.moebius.services.GameSettings.Keys.LOSE_PENALTY;
import static com.codenjoy.dojo.moebius.services.GameSettings.Keys.WIN_SCORE;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(WIN_SCORE, 1)
                .integer(LOSE_PENALTY, -1);
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
                "WIN,1 > +1 = 101\n" +
                "WIN,2 > +2 = 103\n" +
                "WIN,3 > +3 = 106\n" +
                "WIN,4 > +4 = 110\n" +
                "GAME_OVER > -1 = 109");
    }

    @Test
    public void shouldNotBeLessThanZero() {
        assertEvents("2:\n" +
                "GAME_OVER > -1 = 1\n" +
                "GAME_OVER > -1 = 0\n" +
                "GAME_OVER > +0 = 0");
    }

    @Test
    public void shouldClearScore() {
        assertEvents("100:\n" +
                "WIN,1 > +1 = 101\n" +
                "(CLEAN) > -101 = 0\n" +
                "WIN,4 > +4 = 4");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        // given
        settings.integer(WIN_SCORE, 1);

        // when then
        assertEvents("100:\n" +
                "WIN,1 > +1 = 101\n" +
                "WIN,2 > +2 = 103");
    }

    @Test
    public void shouldCollectScores_whenGameOver() {
        // given
        settings.integer(LOSE_PENALTY, -1);

        // when then
        assertEvents("100:\n" +
                "GAME_OVER > -1 = 99\n" +
                "GAME_OVER > -1 = 98");
    }
}