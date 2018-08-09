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

public class FizzBuzzAlgorithmTest {

    @Test
    public void shouldWork() {
        FizzBuzzAlgorithm algorithm = new FizzBuzzAlgorithm();
        assertEquals("1", algorithm.get("1"));
        assertEquals("2", algorithm.get("2"));
        assertEquals("Fizz", algorithm.get("3"));
        assertEquals("4", algorithm.get("4"));
        assertEquals("Buzz", algorithm.get("5"));
        assertEquals("Fizz", algorithm.get("6"));
        assertEquals("7", algorithm.get("7"));
        assertEquals("8", algorithm.get("8"));
        assertEquals("Fizz", algorithm.get("9"));
        assertEquals("Buzz", algorithm.get("10"));
        assertEquals("11", algorithm.get("11"));
        assertEquals("Fizz", algorithm.get("12"));
        assertEquals("13", algorithm.get("13"));
        assertEquals("14", algorithm.get("14"));
        assertEquals("FizzBuzz", algorithm.get("15"));
        assertEquals("16", algorithm.get("16"));
        assertEquals("17", algorithm.get("17"));
        assertEquals("Fizz", algorithm.get("18"));
        assertEquals("19", algorithm.get("19"));
        assertEquals("Buzz", algorithm.get("20"));
        assertEquals("Fizz", algorithm.get("21"));
        assertEquals("22", algorithm.get("22"));
        assertEquals("23", algorithm.get("23"));
        assertEquals("Fizz", algorithm.get("24"));
        assertEquals("Buzz", algorithm.get("25"));
        assertEquals("26", algorithm.get("26"));
    }
}
