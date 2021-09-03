package com.codenjoy.dojo.stuff;

import org.junit.After;
import org.junit.Test;

import java.util.Arrays;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static java.util.stream.Collectors.joining;

public class SmartAssertTest {

    private boolean enable = false;

    @Test
    public void test() {
        q1();
        q2();
        String expected = "z\n3";
        assertEquals(expected, change(expected));
    }

    private String change(String expected) {
        if (enable) {
            return Arrays.stream(expected.split("\n"))
                    .sorted()
                    .collect(joining("\n"));
        } else {
            return expected;
        }
    }

    @Test
    public void test2() {
        String expected = "z\n3";
        assertEquals(expected, change(expected));
        q2();
        q1();
    }

    @After
    public void after() throws Exception {
        SmartAssert.checkResult();
    }

    private void q2() {
        String expected = "z\n2";
        assertEquals(expected, change(expected));
    }

    private void q1() {
        q3();
    }

    private void q3() {
        String expected = "z\n1";
        assertEquals(expected, change(expected));
    }
}
