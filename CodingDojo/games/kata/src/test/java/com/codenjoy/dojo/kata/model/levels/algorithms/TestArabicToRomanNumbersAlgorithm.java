package com.codenjoy.dojo.kata.model.levels.algorithms;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestArabicToRomanNumbersAlgorithm {

    @Test
    public void shouldConvertNumbersCorrectly() {
        ArabicToRomanNumbersAlgorithm algorithm = new ArabicToRomanNumbersAlgorithm();
        assertEquals("V", algorithm.get("5"));
        assertEquals("IX", algorithm.get("9"));
        assertEquals("X", algorithm.get("10"));
        assertEquals("XI", algorithm.get("11"));
        assertEquals("XX", algorithm.get("20"));
        assertEquals("XIX", algorithm.get("19"));
        assertEquals("XXXIX", algorithm.get("39"));
        assertEquals("L", algorithm.get("50"));
        assertEquals("LVI", algorithm.get("56"));
        assertEquals("C", algorithm.get("100"));
        assertEquals("CD", algorithm.get("400"));
        assertEquals("DCCCLXXXIV", algorithm.get("884"));
        assertEquals("DCCCLXXXVIII", algorithm.get("888"));
        assertEquals("MIX", algorithm.get("1009"));
        assertEquals("MXXIV", algorithm.get("1024"));
        assertEquals("XLIX", algorithm.get("49"));
        assertEquals("MMLI", algorithm.get("2051"));
        assertEquals("LXXXIV", algorithm.get("84"));
        assertEquals("DCLXVII", algorithm.get("667"));
        assertEquals("MMMXXVIII", algorithm.get("3028"));
        assertEquals("CMXIV", algorithm.get("914"));
    }
}
