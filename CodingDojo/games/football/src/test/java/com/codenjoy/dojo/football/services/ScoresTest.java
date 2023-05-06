package com.codenjoy.dojo.football.services;

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

import com.codenjoy.dojo.football.TestGameSettings;
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
    protected Class<? extends Enum> eventTypes() {
        return Event.class;
    }

    @Test
    public void shouldCollectScores() {
        assertEvents("0:\n" +
                "WIN > +1 = 1\n" +
                "WIN > +1 = 2\n" +
                "WIN > +1 = 3\n" +
                "LOSE > -1 = 2");
    }

    @Test
    public void cantBeLessThanZero() {
        assertEvents("0:\n" +
                "LOSE > +0 = 0");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("140:\n" +
                "WIN > +1 = 141\n" +
                "WIN > +1 = 142\n" +
                "(CLEAN) > -142 = 0\n" +
                "WIN > +1 = 1\n" +
                "WIN > +1 = 2");
    }

    @Test
    public void shouldCollectScores_whenWin() {
        assertEvents("140:\n" +
                "WIN > +1 = 141\n" +
                "WIN > +1 = 142");
    }

    @Test
    public void shouldCollectScores_whenLose() {
        assertEvents("140:\n" +
                "LOSE > -1 = 139\n" +
                "LOSE > -1 = 138");
    }
}