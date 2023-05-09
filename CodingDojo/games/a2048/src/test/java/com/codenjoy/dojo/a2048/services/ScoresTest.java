package com.codenjoy.dojo.a2048.services;

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


import com.codenjoy.dojo.a2048.TestGameSettings;
import com.codenjoy.dojo.services.event.EventObject;
import com.codenjoy.dojo.services.event.ScoresMap;
import com.codenjoy.dojo.utils.scorestest.AbstractScoresTest;
import org.junit.Test;

import static com.codenjoy.dojo.services.event.Mode.CUMULATIVELY;

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
    public void shouldCollectScores_whenMoreThenMax() {
        assertEvents("0:\n" +
                "SUM,10 > +10 = 10\n" +
                "SUM,20 > +10 = 20\n" +
                "SUM,30 > +10 = 30");
    }

    @Test
    public void shouldNotCollect_whenLessThenMax() {
        assertEvents("100:\n" +
                "SUM,10 > +0 = 100\n" +
                "SUM,20 > +0 = 100\n" +
                "SUM,30 > +0 = 100");
    }

    @Test
    public void shouldCollect_whenCumulative() {
        // given
        settings.initScore(CUMULATIVELY);

        // when then
        assertEvents("100:\n" +
                "SUM,10 > +10 = 110\n" +
                "SUM,20 > +20 = 130\n" +
                "SUM,30 > +30 = 160");
    }

    @Test
    public void shouldCleanScore_whenCumulative() {
        // given
        settings.initScore(CUMULATIVELY);

        // when then
        assertEvents("0:\n" +
                "SUM,10 > +10 = 10\n" +
                "SUM,20 > +20 = 30\n" +
                "SUM,30 > +30 = 60\n" +
                "(CLEAN) > -60 = 0\n" +
                "SUM,10 > +10 = 10\n" +
                "SUM,20 > +20 = 30\n" +
                "SUM,30 > +30 = 60");
    }

    @Test
    public void shouldNotCollect_whenSame() {
        assertEvents("0:\n" +
                "SUM,10 > +10 = 10\n" +
                "SUM,10 > +0 = 10\n" +
                "SUM,10 > +0 = 10");
    }

    @Test
    public void shouldCleanScore() {
        assertEvents("0:\n" +
                "SUM,10 > +10 = 10\n" +
                "SUM,20 > +10 = 20\n" +
                "SUM,30 > +10 = 30\n" +
                "(CLEAN) > -30 = 0\n" +
                "SUM,10 > +10 = 10\n" +
                "SUM,20 > +10 = 20\n" +
                "SUM,30 > +10 = 30");
    }
}