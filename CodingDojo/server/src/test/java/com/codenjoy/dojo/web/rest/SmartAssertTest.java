package com.codenjoy.dojo.web.rest;

import com.codenjoy.dojo.stuff.SmartAssert;
import static com.codenjoy.dojo.stuff.SmartAssert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SmartAssert.class)
public class SmartAssertTest {

    @Test
    public void test() {   
        q1();
        q2();
        assertEquals("z\n3", "3\nz");
    }

    private void q2() {
        assertEquals("z\n2", "2\nz");
    }

    private void q1() {
        q3();
    }

    private void q3() {
        assertEquals("z", "1");
    }
}
