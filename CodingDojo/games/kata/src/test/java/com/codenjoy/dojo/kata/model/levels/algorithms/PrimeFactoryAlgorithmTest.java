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

public class PrimeFactoryAlgorithmTest {

    @Test
    public void shouldWork() {
        PrimeFactoryAlgorithm algorithm = new PrimeFactoryAlgorithm();
        assertEquals("[1]", algorithm.get("1"));
        assertEquals("[2]", algorithm.get("2"));
        assertEquals("[3]", algorithm.get("3"));
        assertEquals("[2,2]", algorithm.get("4"));
        assertEquals("[5]", algorithm.get("5"));
        assertEquals("[2,3]", algorithm.get("6"));
        assertEquals("[7]", algorithm.get("7"));
        assertEquals("[2,2,2]", algorithm.get("8"));
        assertEquals("[3,3]", algorithm.get("9"));
        assertEquals("[2,5]", algorithm.get("10"));
        assertEquals("[11]", algorithm.get("11"));
        assertEquals("[2,2,3]", algorithm.get("12"));
        assertEquals("[13]", algorithm.get("13"));
        assertEquals("[2,7]", algorithm.get("14"));
        assertEquals("[3,5]", algorithm.get("15"));
        assertEquals("[2,2,2,2]", algorithm.get("16"));
        assertEquals("[17]", algorithm.get("17"));
        assertEquals("[2,3,3]", algorithm.get("18"));
        assertEquals("[19]", algorithm.get("19"));
        assertEquals("[2,2,5]", algorithm.get("20"));
        assertEquals("[3,7]", algorithm.get("21"));
        assertEquals("[2,11]", algorithm.get("22"));
        assertEquals("[23]", algorithm.get("23"));
        assertEquals("[2,2,2,3]", algorithm.get("24"));
        assertEquals("[5,5]", algorithm.get("25"));
        assertEquals("[2,13]", algorithm.get("26"));
    }
}
