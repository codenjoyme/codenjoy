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

import static org.junit.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 2/13/13
 * Time: 11:42 PM
 */
public class PrimeFactoryAlgorithmTest {

    @Test
    public void shouldWork() {
        PrimeFactoryAlgorithm monster = new PrimeFactoryAlgorithm();
        assertEquals("[1]", monster.get("1"));
        assertEquals("[2]", monster.get("2"));
        assertEquals("[3]", monster.get("3"));
        assertEquals("[2,2]", monster.get("4"));
        assertEquals("[5]", monster.get("5"));
        assertEquals("[2,3]", monster.get("6"));
        assertEquals("[7]", monster.get("7"));
        assertEquals("[2,2,2]", monster.get("8"));
        assertEquals("[3,3]", monster.get("9"));
        assertEquals("[2,5]", monster.get("10"));
        assertEquals("[11]", monster.get("11"));
        assertEquals("[2,2,3]", monster.get("12"));
        assertEquals("[13]", monster.get("13"));
        assertEquals("[2,7]", monster.get("14"));
        assertEquals("[3,5]", monster.get("15"));
        assertEquals("[2,2,2,2]", monster.get("16"));
        assertEquals("[17]", monster.get("17"));
        assertEquals("[2,3,3]", monster.get("18"));
        assertEquals("[19]", monster.get("19"));
        assertEquals("[2,2,5]", monster.get("20"));
        assertEquals("[3,7]", monster.get("21"));
        assertEquals("[2,11]", monster.get("22"));
        assertEquals("[23]", monster.get("23"));
        assertEquals("[2,2,2,3]", monster.get("24"));
        assertEquals("[5,5]", monster.get("25"));
        assertEquals("[2,13]", monster.get("26"));
    }
}
