package com.codenjoy.dojo.a2048.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.settings.Settings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScoresTest {
    private PlayerScores scores;

    public void sum(int sum) {
        scores.event(new Events(Events.Event.SUM, sum));
    }

    @Before
    public void setup() {
        scores = new Scores(0);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(0);

        sum(10);
        sum(20);
        sum(30);

        assertEquals(30, scores.getScore());
    }

    @Test
    public void shouldNoCollectWhenLessThenMax() {
        scores = new Scores(40);

        sum(10);
        sum(20);
        sum(30);

        assertEquals(40, scores.getScore());
    }

    @Test
    public void shouldNoCollectWhenSame() {
        scores = new Scores(0);

        sum(10);
        sum(10);
        sum(10);

        assertEquals(10, scores.getScore());
    }


}
