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

public class Sequence2AlgorithmTest {

    @Test
    public void shouldWork() {
        Sequence2Algorithm algorithm = new Sequence2Algorithm();
        assertEquals("971", algorithm.get("1"));
        assertEquals("874", algorithm.get("2"));
        assertEquals("787", algorithm.get("3"));
        assertEquals("709", algorithm.get("4"));
        assertEquals("639", algorithm.get("5"));
        assertEquals("576", algorithm.get("6"));
        assertEquals("519", algorithm.get("7"));
        assertEquals("468", algorithm.get("8"));
        assertEquals("422", algorithm.get("9"));
        assertEquals("380", algorithm.get("10"));
        assertEquals("342", algorithm.get("11"));
        assertEquals("308", algorithm.get("12"));
        assertEquals("278", algorithm.get("13"));
        assertEquals("251", algorithm.get("14"));
        assertEquals("226", algorithm.get("15"));
        assertEquals("204", algorithm.get("16"));
        assertEquals("184", algorithm.get("17"));
        assertEquals("166", algorithm.get("18"));
        assertEquals("150", algorithm.get("19"));
        assertEquals("135", algorithm.get("20"));
        assertEquals("122", algorithm.get("21"));
        assertEquals("110", algorithm.get("22"));
        assertEquals("99", algorithm.get("23"));
        assertEquals("90", algorithm.get("24"));
        assertEquals("81", algorithm.get("25"));
        assertEquals("73", algorithm.get("26"));
        assertEquals("66", algorithm.get("27"));
        assertEquals("60", algorithm.get("28"));
        assertEquals("54", algorithm.get("29"));
        assertEquals("49", algorithm.get("30"));
        assertEquals("45", algorithm.get("31"));
        assertEquals("41", algorithm.get("32"));
        assertEquals("37", algorithm.get("33"));
        assertEquals("34", algorithm.get("34"));
        assertEquals("31", algorithm.get("35"));
        assertEquals("28", algorithm.get("36"));
        assertEquals("26", algorithm.get("37"));
        assertEquals("24", algorithm.get("38"));
        assertEquals("22", algorithm.get("39"));
        assertEquals("20", algorithm.get("40"));
        assertEquals("18", algorithm.get("41"));
        assertEquals("17", algorithm.get("42"));
        assertEquals("16", algorithm.get("43"));
        assertEquals("15", algorithm.get("44"));
        assertEquals("14", algorithm.get("45"));
        assertEquals("13", algorithm.get("46"));
        assertEquals("12", algorithm.get("47"));
        assertEquals("11", algorithm.get("48"));
        assertEquals("10", algorithm.get("49"));
        assertEquals("9", algorithm.get("50"));
        assertEquals("9", algorithm.get("51"));
        assertEquals("9", algorithm.get("52"));
    }
}
