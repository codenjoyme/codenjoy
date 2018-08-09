package com.codenjoy.dojo.kata.services.events;

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


import org.junit.Test;

import static com.codenjoy.dojo.kata.services.events.Scores.*;
import static org.junit.Assert.assertEquals;

public class PassTestEventTest {

    @Test
    public void shouldWork() {
        assertScores(3, 30, 100);
        assertScores(6, 30, 50);
        assertScores(10, 30, 30);
        assertScores(30, 30, 10);

        assertScores(10, 100, 100);
        assertScores(20, 100, 50);
        assertScores(33, 100, 30);
        assertScores(100, 100, 10);
    }

    @Test
    public void shouldNoLessThan1PerTest() {
        int manyTests = 1000;
        assertScores(1, 1, manyTests);
        assertScores(1, 10, manyTests);
        assertScores(1, 100, manyTests);
        assertScores(10, 1000, manyTests);
        assertScores(100, 10000, manyTests);
    }

    private void assertScores(int expected, int complexity, int testCount) {
        assertEquals(expected, new PassTestEvent(complexity, testCount).getScore(A, D));
    }

}
