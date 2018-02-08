package com.codenjoy.dojo.kata.model.levels.algorithms;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CountTrippleAlgorithmTest {

    @Test
    public void shouldCountTripples() {
        CountTrippleAlgorithm algorithm = new CountTrippleAlgorithm();
        assertEquals("1", algorithm.get("abcXXXabc"));
        assertEquals("0", algorithm.get("vvaad"));
        assertEquals("3", algorithm.get("xxxabyyyycd"));
        assertEquals("0", algorithm.get("c"));
        assertEquals("6", algorithm.get("saedfasaaaaasadddfsffff"));
        assertEquals("4", algorithm.get("9991jd12Xxx8888daadaaa"));
        assertEquals("8", algorithm.get("jgbansdkaaffffasssjjjjjjddsaasss0"));

        assertEquals("2", algorithm.get("ss23412;;;dsdawrfas^^^"));
        assertEquals("3", algorithm.get(">>> (>0_0)>POKEMOO0NNN<(0_0<) <<<"));
        assertEquals("2", algorithm.get("    "));
        assertEquals("6", algorithm.get("0xffffffff"));
    }
}
