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

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings();
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
        assertEvents("140:\n" +
                "BONUS,2,5 > +250 = 390\n" +
                "BONUS,3,7 > +233 = 623\n" +
                "WIN > +30 = 653");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        assertEvents("140:\n" +
                "WIN > +30 = 170\n" +
                "WIN > +30 = 200");
    }

    @Test
    public void shouldCollectScores_whenBonus() {
        assertEvents("1140:\n" +
                "BONUS,2,5 > +250 = 1390\n" +
                "BONUS,3,7 > +233 = 1623");
    }

    @Test
    public void shouldClean() {
        assertEvents("140:\n" +
                "WIN > +30 = 170\n" +
                "WIN > +30 = 200\n" +
                "(CLEAN) > -200 = 0\n" +
                "WIN > +30 = 30\n" +
                "WIN > +30 = 60");
    }
}