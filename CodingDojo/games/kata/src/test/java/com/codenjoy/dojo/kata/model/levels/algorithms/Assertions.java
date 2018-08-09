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


import com.codenjoy.dojo.kata.model.levels.Algorithm;

import static org.junit.Assert.assertEquals;

public class Assertions {
    public static void assertAlgorithm(String[] expected, Algorithm algorithm) {
        for (int index = 0; index < expected.length; index++) {
            assertEquals("At index " + index,
                    expected[index],
                    algorithm.get(String.valueOf(index)));
        }
    }

    public static void assertAlgorithm(int[] expected, Algorithm algorithm) {
        for (int index = 0; index < expected.length; index++) {
            assertEquals("At index " + index,
                    String.valueOf(expected[index]),
                    algorithm.get(String.valueOf(index)));
        }
    }
}
