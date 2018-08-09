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

public class Sequence1AlgorithmTest {

    @Test
    public void shouldWork() {
        Sequence1Algorithm algorithm = new Sequence1Algorithm();
        assertEquals("101", algorithm.get("1"));
        assertEquals("112", algorithm.get("2"));
        assertEquals("131", algorithm.get("3"));
        assertEquals("415", algorithm.get("4"));
        assertEquals("161", algorithm.get("5"));
        assertEquals("718", algorithm.get("6"));
        assertEquals("192", algorithm.get("7"));
        assertEquals("021", algorithm.get("8"));
        assertEquals("222", algorithm.get("9"));
        assertEquals("324", algorithm.get("10"));
        assertEquals("252", algorithm.get("11"));
        assertEquals("627", algorithm.get("12"));
        assertEquals("282", algorithm.get("13"));
        assertEquals("930", algorithm.get("14"));
    }
}
