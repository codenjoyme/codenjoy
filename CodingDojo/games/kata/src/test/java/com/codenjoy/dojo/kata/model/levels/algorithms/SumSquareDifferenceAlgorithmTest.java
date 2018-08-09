package com.codenjoy.dojo.kata.model.levels.algorithms;

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

import static org.junit.Assert.assertEquals;

public class SumSquareDifferenceAlgorithmTest {

    @Test
    public void shouldWork() {
        int[] primes = new int[]{0, 0, 4, 22, 70, 170, 350, 644, 1092, 1740, 2640, 3850, 5434, 7462, 10010, 13160, 17000, 21624};
        SumSquareDifferenceAlgorithm algorithm = new SumSquareDifferenceAlgorithm();
        Assertions.assertAlgorithm(primes, algorithm);

        assertEquals("25164150", algorithm.get("100"));
        assertEquals("250166416500", algorithm.get("1000"));
        assertEquals("2500166641665000", algorithm.get("10000"));
    }
}
