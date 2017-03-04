package com.codenjoy.dojo.kata.model.levels.algorithms;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 Codenjoy
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

import static junit.framework.TestCase.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 2/13/13
 * Time: 11:42 PM
 */
public class FizzBuzzAlgorithmTest {

    @Test
    public void shouldWork() {
        FizzBuzzAlgorithm monster = new FizzBuzzAlgorithm();
        assertEquals("1", monster.get("1"));
        assertEquals("2", monster.get("2"));
        assertEquals("Fizz", monster.get("3"));
        assertEquals("4", monster.get("4"));
        assertEquals("Buzz", monster.get("5"));
        assertEquals("Fizz", monster.get("6"));
        assertEquals("7", monster.get("7"));
        assertEquals("8", monster.get("8"));
        assertEquals("Fizz", monster.get("9"));
        assertEquals("Buzz", monster.get("10"));
        assertEquals("11", monster.get("11"));
        assertEquals("Fizz", monster.get("12"));
        assertEquals("13", monster.get("13"));
        assertEquals("14", monster.get("14"));
        assertEquals("FizzBuzz", monster.get("15"));
        assertEquals("16", monster.get("16"));
        assertEquals("17", monster.get("17"));
        assertEquals("Fizz", monster.get("18"));
        assertEquals("19", monster.get("19"));
        assertEquals("Buzz", monster.get("20"));
        assertEquals("Fizz", monster.get("21"));
        assertEquals("22", monster.get("22"));
        assertEquals("23", monster.get("23"));
        assertEquals("Fizz", monster.get("24"));
        assertEquals("Buzz", monster.get("25"));
        assertEquals("26", monster.get("26"));
    }
}
