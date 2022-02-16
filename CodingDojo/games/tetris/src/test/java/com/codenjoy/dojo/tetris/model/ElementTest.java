package com.codenjoy.dojo.tetris.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.games.tetris.Element;
import com.codenjoy.dojo.games.tetris.ElementUtils;
import org.junit.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

public class ElementTest {

    @Test
    public void testValuesExcept() {
        assertEquals("[I, J, L, O, S, T, Z, .]",
                Arrays.toString(ElementUtils.valuesExcept()));

        assertEquals("[I, J, L, O, S, T, Z]",
                Arrays.toString(ElementUtils.valuesExcept(Element.NONE)));
    }

    @Test
    public void testNumber() {
        assertEquals("['I':2, 'J':3, 'L':4, 'O':1, 'S':5, 'T':7, 'Z':6, '.':0]",
                Arrays.stream(Element.values())
                        .map(el -> String.format("'%s':%s", el.ch(), ElementUtils.index(el)))
                        .collect(toList())
                        .toString());
    }
}
