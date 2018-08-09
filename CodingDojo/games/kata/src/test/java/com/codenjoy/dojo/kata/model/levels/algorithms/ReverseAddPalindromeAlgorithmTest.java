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
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReverseAddPalindromeAlgorithmTest {

    @Test
    public void shouldWork() {
        Algorithm algorithm = new ReverseAddPalindromeAlgorithm();
        assertEquals("2", algorithm.get("1"));
        assertEquals("4", algorithm.get("2"));
        assertEquals("6", algorithm.get("3"));
        assertEquals("8", algorithm.get("4"));
        assertEquals("11", algorithm.get("5"));
        assertEquals("33", algorithm.get("6"));
        assertEquals("55", algorithm.get("7"));
        assertEquals("77", algorithm.get("8"));
        assertEquals("99", algorithm.get("9"));
        assertEquals("11", algorithm.get("10"));
        assertEquals("22", algorithm.get("11"));
        assertEquals("33", algorithm.get("12"));
        assertEquals("44", algorithm.get("13"));
        assertEquals("55", algorithm.get("14"));
        assertEquals("66", algorithm.get("15"));
        assertEquals("77", algorithm.get("16"));
        assertEquals("88", algorithm.get("17"));
        assertEquals("99", algorithm.get("18"));
        assertEquals("22", algorithm.get("20"));
        assertEquals("33", algorithm.get("21"));
        assertEquals("44", algorithm.get("22"));
        assertEquals("55", algorithm.get("23"));
        assertEquals("66", algorithm.get("24"));
        assertEquals("77", algorithm.get("25"));
        assertEquals("4444", algorithm.get("254"));
        assertEquals("8813200023188", algorithm.get("89"));
        assertEquals("444", algorithm.get("123"));
        assertEquals("848", algorithm.get("127"));
        assertEquals("4664", algorithm.get("3421"));
        assertEquals("99999999", algorithm.get("12345678"));
    }
}
