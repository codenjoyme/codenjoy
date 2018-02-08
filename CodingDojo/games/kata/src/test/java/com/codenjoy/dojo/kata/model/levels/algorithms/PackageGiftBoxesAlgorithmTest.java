package com.codenjoy.dojo.kata.model.levels.algorithms;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PackageGiftBoxesAlgorithmTest {

    @Test
    public void shouldCalculateSmallBoxesForGifts() {
        PackageGiftBoxesAlgorithm algorithm = new PackageGiftBoxesAlgorithm();
        assertEquals("4", algorithm.get("4, 1, 9"));
        assertEquals("-1", algorithm.get("4, 1, 10"));
        assertEquals("2", algorithm.get("4, 1, 7"));
        assertEquals("4", algorithm.get("5, 2, 14"));
        assertEquals("-1", algorithm.get("1, 3, 14"));
        assertEquals("-1", algorithm.get("20, 180, 1000"));
        assertEquals("100", algorithm.get("100, 180, 1000"));

        assertEquals("3", algorithm.get("3, 1, 8"));
        assertEquals("0", algorithm.get("3, 2, 10"));
        assertEquals("3", algorithm.get("3, 2, 8"));
        assertEquals("-1", algorithm.get("6, 0, 11"));
        assertEquals("1", algorithm.get("1, 4, 11"));
        assertEquals("0", algorithm.get("0, 3, 10"));
        assertEquals("-1", algorithm.get("1, 4, 12"));
        assertEquals("-1", algorithm.get("1, 1, 7"));
        assertEquals("2", algorithm.get("2, 1, 7"));
        assertEquals("6", algorithm.get("7, 1, 11"));
        assertEquals("3", algorithm.get("7, 1, 8"));
        assertEquals("-1", algorithm.get("7, 1, 13"));
        assertEquals("41", algorithm.get("43, 1, 46"));
        assertEquals("-1", algorithm.get("40, 1, 46"));
        assertEquals("37", algorithm.get("40, 2, 47"));
        assertEquals("40", algorithm.get("40, 2, 50"));
        assertEquals("-1", algorithm.get("40, 2, 52"));
        assertEquals("-1", algorithm.get("22, 2, 33"));
        assertEquals("0", algorithm.get("0, 2, 10"));
        assertEquals("995100", algorithm.get("1000000, 1000, 1000100"));
        assertEquals("-1", algorithm.get("2, 1000000, 100003"));
        assertEquals("19", algorithm.get("20, 0, 19"));
        assertEquals("-1", algorithm.get("20, 0, 21"));
        assertEquals("-1", algorithm.get("20, 4, 51"));
        assertEquals("19", algorithm.get("20, 4, 39"));
    }
}
