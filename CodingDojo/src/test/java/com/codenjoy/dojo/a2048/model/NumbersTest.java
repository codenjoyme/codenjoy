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
        Numbers numbers = new Numbers(4);

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

    @Test
    public void shouldMoveLeft() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1))), 4);

        assertN("...." +
                "...." +
                ".4.." +
                "....", numbers);

        numbers.moveLeft();

        assertN("...." +
                "...." +
                "4..." +
                "....", numbers);
    }

    @Test
    public void shouldMoveLeftWithSplit() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1)), new Number(4, pt(3, 1))), 4);

        assertN("...." +
                "...." +
                ".4.4" +
                "....", numbers);

        numbers.moveLeft();

        assertN("...." +
                "...." +
                "8..." +
                "....", numbers);
    }

    @Test
    public void shouldMoveLeftWithSplitTwice() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(2, pt(0, 1)), new Number(2, pt(1, 1)), new Number(2, pt(2, 1)), new Number(2, pt(3, 1))), 4);

        assertN("...." +
                "...." +
                "2222" +
                "....", numbers);

        numbers.moveLeft();

        assertN("...." +
                "...." +
                "44.." +
                "....", numbers);
    }

    private void assertN(String expected, Numbers numbers) {
        assertEquals(LoderunnerTest.injectN(expected), numbers.toString());
    }
}
