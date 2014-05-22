package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;

/**
 * Created by Sanja on 22.05.14.
 */
public class NumbersTest {

    @Test
    public void shouldEmptyWhenNew() {
        Numbers numbers = new Numbers(new LinkedList<Number>(), 4);

        assertN("...." +
                "...." +
                "...." +
                "....", numbers);
    }

    @Test
    public void shouldSomeNumbersAtStart() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1)), new Number(8, pt(3, 2))), 4);

        assertN("...." +
                "...8" +
                ".4.." +
                "....", numbers);
    }

    private void assertN(String expected, Numbers numbers) {
        assertEquals(LoderunnerTest.injectN(expected), numbers.toString());
    }
}
