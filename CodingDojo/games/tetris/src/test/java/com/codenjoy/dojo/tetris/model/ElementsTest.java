package com.codenjoy.dojo.tetris.model;

import org.junit.Test;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class ElementsTest {

    @Test
    public void testValuesExcept() {
        assertEquals("[I, J, L, O, S, T, Z, .]",
                Arrays.toString(Elements.valuesExcept()));

        assertEquals("[I, J, L, O, S, T, Z]",
                Arrays.toString(Elements.valuesExcept(Elements.NONE)));
    }

    @Test
    public void testNumber() {
        assertEquals("['I':2, 'J':3, 'L':4, 'O':1, 'S':5, 'T':7, 'Z':6, '.':0]",
                Arrays.stream(Elements.values())
                        .map(el -> String.format("'%s':%s", el.ch, el.index()))
                        .collect(toList())
                        .toString());
    }
}