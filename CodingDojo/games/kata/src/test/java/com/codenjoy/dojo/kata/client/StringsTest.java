package com.codenjoy.dojo.kata.client;

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

import java.util.Arrays;

import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void shouldAddStrings() {
        // given
        Strings strings = new Strings();

        // when
        strings.add("string1");
        strings.add("string2");
        strings.add("string3");

        // then
        assertEquals("[string1, string2, string3]",
                strings.getStrings().toString());
    }

    @Test
    public void shouldIterable() {
        // given
        Strings strings = new Strings();
        strings.add("string1");
        strings.add("string2");
        strings.add("string3");

        // when
        String result = "";
        for (String string : strings) {
            result += string;
        }

        // then
        assertEquals("string1string2string3", result);
    }

    @Test
    public void shouldToString() {
        // given
        Strings strings = new Strings(Arrays.asList("string1", "string2", "string3"));

        // when
        String actual = strings.toString();

        // then
        assertEquals("['string1', 'string2', 'string3']", actual);
    }
}
