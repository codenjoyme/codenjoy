package com.codenjoy.dojo.kata.client;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by indigo on 2017-03-05.
 */
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
        assertEquals("[string1, string2, string3]", actual);
    }
}