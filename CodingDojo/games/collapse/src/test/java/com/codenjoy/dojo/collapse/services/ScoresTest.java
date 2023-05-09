package com.codenjoy.dojo.collapse.services;

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


import com.codenjoy.dojo.collapse.TestGameSettings;
import com.codenjoy.dojo.services.event.EventObject;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.collapse.services.GameSettings.Keys.SUCCESS_SCORE;

public class ScoresTest extends AbstractScoresTest {

    @Override
    public GameSettings settings() {
        return new TestGameSettings()
                .integer(SUCCESS_SCORE, 1);
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
        assertEvents("140:\n" +
                "SUCCESS,1 > +1 = 141\n" +
                "SUCCESS,1 > +1 = 142\n" +
                "SUCCESS,1 > +1 = 143\n" +
                "SUCCESS,1 > +1 = 144");
    }

    @Test
    public void shouldCollectScores_whenMoreThanOne_caseMultiple() {
        assertEvents("140:\n" +
                "SUCCESS,5 > +15 = 155\n" +
                "SUCCESS,5 > +15 = 170");
    }

    @Test
    public void shouldCollectScores_whenMoreThanOne_caseSingle() {
        assertEvents("140:\n" +
                "SUCCESS,15 > +120 = 260");
    }

    @Test
    public void shouldClean() {
        assertEvents("140:\n" +
                "SUCCESS,1 > +1 = 141\n" +
                "SUCCESS,1 > +1 = 142\n" +
                "SUCCESS,1 > +1 = 143\n" +
                "(CLEAN) > -143 = 0\n" +
                "SUCCESS,1 > +1 = 1\n" +
                "SUCCESS,1 > +1 = 2\n" +
                "SUCCESS,1 > +1 = 3");
    }
}