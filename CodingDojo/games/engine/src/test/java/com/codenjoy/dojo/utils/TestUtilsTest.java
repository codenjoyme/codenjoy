package com.codenjoy.dojo.utils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by indigo on 18.03.2000.
 */
public class TestUtilsTest {

    @Test
    public void testInject() {
        assertEquals("1234^*5678^*90AB^*CDEF^*HIJK^*LMNO^*PQRS^*TUVW^*XYZ",
                TestUtils.inject("1234567890ABCDEFHIJKLMNOPQRSTUVWXYZ", 4, "^*"));

        assertEquals("1234^*5678^*90AB^*CDEF^*HIJK^*LMNO^*PQRS^*TUVW^*",
                TestUtils.inject("1234567890ABCDEFHIJKLMNOPQRSTUVW", 4, "^*"));

        assertEquals("1234^*5678^*90AB^*CDEF^*HIJK^*LMNO^*PQRS^*TUV",
                TestUtils.inject("1234567890ABCDEFHIJKLMNOPQRSTUV", 4, "^*"));
    }

    @Test
    public void testInjectN() {
        assertEquals("12345\n" +
                    "67890\n" +
                    "ABCDE\n" +
                    "FHIJK\n" +
                    "LMNOP\n" +
                    "QRSTU\n" +
                    "VWXYZ\n",
                TestUtils.injectN("1234567890ABCDEFHIJKLMNOPQRSTUVWXYZ"));

        assertEquals("1234\n" +
                    "5678\n" +
                    "90AB\n" +
                    "CDEF\n" +
                    "HIJK\n" +
                    "LMNO\n",
                TestUtils.injectN("1234567890ABCDEFHIJKLMNO"));
    }
}
