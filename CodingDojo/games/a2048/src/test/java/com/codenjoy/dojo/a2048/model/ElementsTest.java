package com.codenjoy.dojo.a2048.model;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ElementsTest {

    @Test
    public void testValuesExcept() {
        assertEquals("[x, 2, 4, 8, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S,  ]",
                Arrays.toString(Elements.valuesExcept()));

        assertEquals("[2, 4, 8, A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S]",
                Arrays.toString(Elements.valuesExcept(Elements.NONE, Elements._x)));

    }

}