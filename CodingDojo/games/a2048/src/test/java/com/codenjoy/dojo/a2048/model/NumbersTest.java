package com.codenjoy.dojo.a2048.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Test;

import java.lang.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static junit.framework.Assert.assertEquals;

public class NumbersTest {

    private static final List<Number> EMPTY = new LinkedList<Number>();

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
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1)), new Number(8, pt(3, 2))), 4, EMPTY);

        assertN("...." +
                "...8" +
                ".4.." +
                "....", numbers);
    }

    @Test
    public void shouldMoveLeft() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                ".4.." +
                "....", numbers);

        numbers.move(Direction.LEFT);

        assertN("...." +
                "...." +
                "4..." +
                "....", numbers);
    }

    @Test
    public void shouldMoveLeftWithSplit() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1)), new Number(4, pt(3, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                ".4.4" +
                "....", numbers);

        numbers.move(Direction.LEFT);

        assertN("...." +
                "...." +
                "8..." +
                "....", numbers);
    }

    @Test
    public void shouldMoveLeftWithSplitTwice() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(2, pt(0, 1)), new Number(2, pt(1, 1)), new Number(2, pt(2, 1)), new Number(2, pt(3, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                "2222" +
                "....", numbers);

        numbers.move(Direction.LEFT);

        assertN("...." +
                "...." +
                "44.." +
                "....", numbers);

        numbers.move(Direction.LEFT);

        assertN("...." +
                "...." +
                "8..." +
                "....", numbers);
    }

    @Test
    public void shouldMoveLeftWithSplitTwice2() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(0, 1)), new Number(2, pt(2, 1)), new Number(2, pt(3, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                "4.22" +
                "....", numbers);

        numbers.move(Direction.LEFT);

        assertN("...." +
                "...." +
                "44.." +
                "....", numbers);
    }

    @Test
    public void shouldMoveRight() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                ".4.." +
                "....", numbers);

        numbers.move(Direction.RIGHT);

        assertN("...." +
                "...." +
                "...4" +
                "....", numbers);
    }

    @Test
    public void shouldMoveRightWithSplit() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1)), new Number(4, pt(3, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                ".4.4" +
                "....", numbers);

        numbers.move(Direction.RIGHT);

        assertN("...." +
                "...." +
                "...8" +
                "....", numbers);
    }

    @Test
    public void shouldMoveRightWithSplitTwice() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(2, pt(0, 1)), new Number(2, pt(1, 1)), new Number(2, pt(2, 1)), new Number(2, pt(3, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                "2222" +
                "....", numbers);

        numbers.move(Direction.RIGHT);

        assertN("...." +
                "...." +
                "..44" +
                "....", numbers);

        numbers.move(Direction.RIGHT);

        assertN("...." +
                "...." +
                "...8" +
                "....", numbers);
    }

    @Test
    public void shouldMoveRightWithSplitTwice2() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(2, pt(0, 1)), new Number(2, pt(1, 1)), new Number(4, pt(3, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                "22.4" +
                "....", numbers);

        numbers.move(Direction.RIGHT);

        assertN("...." +
                "...." +
                "..44" +
                "....", numbers);
    }

    private void assertN(String expected, Numbers numbers) {
        assertEquals(TestUtils.injectN(expected), numbers.toString());
    }

    @Test
    public void shouldMoveUp() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                ".4.." +
                "....", numbers);

        numbers.move(Direction.UP);

        assertN(".4.." +
                "...." +
                "...." +
                "....", numbers);
    }

    @Test
    public void shouldMoveUpWithSplit() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 0)), new Number(4, pt(1, 2))), 4, EMPTY);

        assertN("...." +
                ".4.." +
                "...." +
                ".4..", numbers);

        numbers.move(Direction.UP);

        assertN(".8.." +
                "...." +
                "...." +
                "....", numbers);
    }

    @Test
    public void shouldMoveUpWithSplitTwice() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(2, pt(1, 0)), new Number(2, pt(1, 1)), new Number(2, pt(1, 2)), new Number(2, pt(1, 3))), 4, EMPTY);

        assertN(".2.." +
                ".2.." +
                ".2.." +
                ".2..", numbers);

        numbers.move(Direction.UP);

        assertN(".4.." +
                ".4.." +
                "...." +
                "....", numbers);

        numbers.move(Direction.UP);

        assertN(".8.." +
                "...." +
                "...." +
                "....", numbers);
    }

    @Test
    public void shouldMoveUpWithSplitTwice2() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(2, pt(1, 0)), new Number(2, pt(1, 1)), new Number(4, pt(1, 3))), 4, EMPTY);

        assertN(".4.." +
                "...." +
                ".2.." +
                ".2..", numbers);

        numbers.move(Direction.UP);

        assertN(".4.." +
                ".4.." +
                "...." +
                "....", numbers);
    }

    @Test
    public void shouldMoveDown() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1))), 4, EMPTY);

        assertN("...." +
                "...." +
                ".4.." +
                "....", numbers);

        numbers.move(Direction.DOWN);

        assertN("...." +
                "...." +
                "...." +
                ".4..", numbers);
    }

    @Test
    public void shouldMoveDownWithSplit() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 1)), new Number(4, pt(1, 3))), 4, EMPTY);

        assertN(".4.." +
                "...." +
                ".4.." +
                "....", numbers);

        numbers.move(Direction.DOWN);

        assertN("...." +
                "...." +
                "...." +
                ".8..", numbers);
    }

    @Test
    public void shouldMoveDownWithSplitTwice() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(2, pt(1, 0)), new Number(2, pt(1, 1)), new Number(2, pt(1, 2)), new Number(2, pt(1, 3))), 4, EMPTY);

        assertN(".2.." +
                ".2.." +
                ".2.." +
                ".2..", numbers);

        numbers.move(Direction.DOWN);

        assertN("...." +
                "...." +
                ".4.." +
                ".4..", numbers);

        numbers.move(Direction.DOWN);

        assertN("...." +
                "...." +
                "...." +
                ".8..", numbers);
    }

    @Test
    public void shouldMoveDownWithSplitTwice2() {
        Numbers numbers = new Numbers(Arrays.asList(new Number(4, pt(1, 0)), new Number(2, pt(1, 2)), new Number(2, pt(1, 3))), 4, EMPTY);

        assertN(".2.." +
                ".2.." +
                "...." +
                ".4..", numbers);

        numbers.move(Direction.DOWN);

        assertN("...." +
                "...." +
                ".4.." +
                ".4..", numbers);
    }
}
