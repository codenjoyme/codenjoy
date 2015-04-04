package com.codenjoy.dojo.collapse.client;

import com.codenjoy.dojo.collapse.model.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sanja on 11.02.14.
 */
public class BoardTest {

    private Board board;

    @Before
    public void setup() {
         board = (Board) new Board().forString(
                 "☼☼☼☼☼☼☼" +
                 "☼12345☼" +
                 "☼67891☼" +
                 "☼23456☼" +
                 "☼78912☼" +
                 "☼3456 ☼" +
                 "☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldToString() {
        asrtBrd("Board:\n" +
                "☼☼☼☼☼☼☼\n" +
                "☼12345☼\n" +
                "☼67891☼\n" +
                "☼23456☼\n" +
                "☼78912☼\n" +
                "☼3456 ☼\n" +
                "☼☼☼☼☼☼☼\n" +
                "\n");
    }

    private void asrtBrd(String expected) {
        assertEquals(expected, board.toString());
    }

    @Test
    public void shouldGetElements() {
        assertElementAt(Elements.ONE,   "[[1,1], [5,2], [4,4]]");
        assertElementAt(Elements.TWO,   "[[2,1], [1,3], [5,4]]");
        assertElementAt(Elements.THREE, "[[3,1], [2,3], [1,5]]");
        assertElementAt(Elements.FOUR,  "[[4,1], [3,3], [2,5]]");
        assertElementAt(Elements.FIVE,  "[[5,1], [4,3], [3,5]]");
        assertElementAt(Elements.SIX,   "[[1,2], [5,3], [4,5]]");
        assertElementAt(Elements.SEVEN, "[[2,2], [1,4]]");
        assertElementAt(Elements.EIGHT, "[[3,2], [2,4]]");
        assertElementAt(Elements.NINE,  "[[4,2], [3,4]]");
        assertElementAt(Elements.NONE,  "[[5,5]]");
    }

    private void assertElementAt(Elements element, String expected) {
        assertEquals(expected, board.get(element).toString());
    }

    @Test
    public void shouldGetAt() {
        assertEquals("☼", board.getAt(0, 6).toString());
        assertEquals("3", board.getAt(1, 5).toString());
        assertEquals("8", board.getAt(2, 4).toString());
        assertEquals("4", board.getAt(3, 3).toString());
        assertEquals("9", board.getAt(4, 2).toString());
        assertEquals("5", board.getAt(5, 1).toString());
        assertEquals("☼", board.getAt(6, 0).toString());
    }

    @Test
    public void shouldIsAt() {
        assertTrue(board.isAt(5, 2, Elements.ONE));
        assertFalse(board.isAt(2, 5, Elements.ONE));
    }
}
