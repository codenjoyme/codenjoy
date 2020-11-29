package com.codenjoy.dojo.sudoku.model;

import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.sudoku.model.Elements.*;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;

public class ElementsTest {

    @Test
    public void testValuesExcept() {
        assertEquals("[ , ☼, *, 1, 2, 3, 4, 5, 6, 7, 8, 9]",
                Arrays.toString(Elements.valuesExcept()));

        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9]",
                Arrays.toString(Elements.valuesExcept(BORDER, NONE, HIDDEN)));
    }

    @Test
    public void testNumber() {
        assertEquals("[' ':0, '☼':-1, '*':-1, '1':1, '2':2, '3':3, " +
                        "'4':4, '5':5, '6':6, '7':7, '8':8, '9':9]",
                Arrays.stream(Elements.values())
                        .map(el -> String.format("'%s':%s", el.ch, el.value()))
                        .collect(toList())
                        .toString());
    }

}