package com.codenjoy.dojo.sudoku.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.games.sudoku.Element;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.games.sudoku.Element.*;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class ElementsTest {

    @Test
    public void testValuesExcept() {
        assertEquals("[ , ☼, *, 1, 2, 3, 4, 5, 6, 7, 8, 9]",
                Arrays.toString(Element.valuesExcept()));

        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]",
                Arrays.toString(Element.valuesExcept(BORDER, NONE, HIDDEN)));
    }

    @Test
    public void testNumber() {
        assertEquals("[' ':0, '☼':-1, '*':-1, '1':1, '2':2, '3':3, " +
                        "'4':4, '5':5, '6':6, '7':7, '8':8, '9':9]",
                Arrays.stream(Element.values())
                        .map(el -> String.format("'%s':%s", el.ch, el.value()))
                        .collect(toList())
                        .toString());
    }

}
