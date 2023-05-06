package com.codenjoy.dojo.fifteen.services;

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

import com.codenjoy.dojo.fifteen.TestGameSettings;
import com.codenjoy.dojo.services.event.EventObject;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.fifteen.services.GameSettings.Keys.BONUS_SCORE;
import static com.codenjoy.dojo.fifteen.services.GameSettings.Keys.WIN_SCORE;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(WIN_SCORE, 30)
                .integer(BONUS_SCORE, 100);
    }

    @Override
    protected Class<? extends ScoresMap<?>> scores() {
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
                "BONUS,2,5 > +250 = 350\n" +
                "BONUS,3,7 > +233 = 583\n" +
                "WIN > +30 = 613");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        // given
        settings.integer(WIN_SCORE, 30);

        // when then
        assertEvents("100:\n" +
                "WIN > +30 = 130\n" +
                "WIN > +30 = 160");
    }

    @Test
    public void shouldCollectScores_whenBonus() {
        // given
        settings.integer(BONUS_SCORE, 100);

        // when then
        assertEvents("100:\n" +
                "BONUS,2,5 > +250 = 350\n" +
                "BONUS,3,7 > +233 = 583");
    }

    @Test
    public void shouldClean() {
        assertEvents("100:\n" +
                "WIN > +30 = 130\n" +
                "WIN > +30 = 160\n" +
                "(CLEAN) > -160 = 0\n" +
                "WIN > +30 = 30\n" +
                "WIN > +30 = 60");
    }
}