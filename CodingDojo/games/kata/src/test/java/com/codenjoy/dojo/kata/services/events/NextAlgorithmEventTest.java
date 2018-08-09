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
import static org.junit.Assert.*;

public class NextAlgorithmEventTest {

    @Test
    public void shouldWork_whenComplexityIs0() throws InterruptedException {
        int complexity = 0;
        assertScores(0, complexity, 0);
        assertScores(0, complexity, 0.1);
        assertScores(0, complexity, 1);
        assertScores(0, complexity, 10);
        assertScores(0, complexity, 100);
    }

    @Test
    public void shouldWork_whenDoubleTimeLessThan1() throws InterruptedException {
        int complexity = 1;
        assertScores(100, complexity, 1);
        assertScores(30, complexity, 3);
        assertScores(117, complexity, 0.5);
        assertScores(100-18, complexity, 1.5);
        assertScores(30, complexity, 4);
    }


    @Test
    public void shouldWork_whenComplexityIs30Minutes() {
        int complexity = 30;
        assertScores(4015, complexity, 1);
        assertScores(3875, complexity, 5);
        assertScores(3700, complexity, 10);
        assertScores(3525, complexity, 15);
        assertScores(3350, complexity, 20);
        assertScores(3000, complexity, complexity);
        assertScores(2650, complexity, 40);
        assertScores(2300, complexity, 50);
        assertScores(1950, complexity, 60);
        assertScores(900, complexity, 90);
        assertScores(900, complexity, 120); // same as 90
        assertScores(900, complexity, 300); // same as 90
    }

    @Test
    public void shouldWork_whenComplexityIs10Minutes() {
        int complexity = 10;
        assertScores(1315, complexity, 1);
        assertScores(1175, complexity, 5);
        assertScores(1000, complexity, complexity);
        assertScores(825, complexity, 15);
        assertScores(650, complexity, 20);
        assertScores(300, complexity, 30);
        assertScores(300, complexity, 40);  // same as 30
        assertScores(300, complexity, 50);  // same as 30
        assertScores(300, complexity, 60);  // same as 30
        assertScores(300, complexity, 90);  // same as 30
        assertScores(300, complexity, 120); // same as 30
        assertScores(300, complexity, 300); // same as 30
    }

    private void assertScores(int expected, int complexity, double time) {
        assertEquals(expected, new NextAlgorithmEvent(complexity, time).getScore(A, B, C));
    }

}
