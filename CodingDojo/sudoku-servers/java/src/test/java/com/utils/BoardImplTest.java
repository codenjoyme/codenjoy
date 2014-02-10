package com.utils;

import com.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sanja on 11.02.14.
 */
public class BoardImplTest {

    private BoardImpl board;

    @Before
    public void setup() {
         board = new BoardImpl(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼53 ☼ 7 ☼   ☼" +
                "☼6  ☼195☼   ☼" +
                "☼ 98☼   ☼ 6 ☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼8  ☼ 6 ☼  3☼" +
                "☼4  ☼8 3☼  1☼" +
                "☼7  ☼ 2 ☼  6☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼" +
                "☼ 6 ☼   ☼28 ☼" +
                "☼   ☼419☼  5☼" +
                "☼   ☼ 8 ☼ 79☼" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼");
    }

    @Test
    public void shouldToString() {
        assertEquals("Board:\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼53 ☼ 7 ☼   ☼\n" +
                "☼6  ☼195☼   ☼\n" +
                "☼ 98☼   ☼ 6 ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼8  ☼ 6 ☼  3☼\n" +
                "☼4  ☼8 3☼  1☼\n" +
                "☼7  ☼ 2 ☼  6☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ 6 ☼   ☼28 ☼\n" +
                "☼   ☼419☼  5☼\n" +
                "☼   ☼ 8 ☼ 79☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "\n", board.toString());
    }

    @Test
    public void shouldGetElements() {
        assertElementAt(Element.ONE, "[[4,8], [5,2], [9,5]]");
        assertElementAt(Element.TWO, "[[5,4], [7,3]]");
        assertElementAt(Element.THREE, "[[2,9], [6,5], [9,6]]");
        assertElementAt(Element.FOUR, "[[1,5], [4,2]]");
        assertElementAt(Element.FIVE, "[[1,9], [6,8], [9,2]]");
        assertElementAt(Element.SIX, "[[1,8], [2,3], [5,6], [8,7], [9,4]]");
        assertElementAt(Element.SEVEN, "[[1,4], [5,9], [8,1]]");
        assertElementAt(Element.EIGHT, "[[1,6], [3,7], [4,5], [5,1], [8,3]]");
        assertElementAt(Element.NINE, "[[2,7], [5,8], [6,2], [9,1]]");
        assertElementAt(Element.NONE, "[[1,1], [1,2], [1,3], [1,7], " +
                "[2,1], [2,2], [2,4], [2,5], [2,6], [2,8], " +
                "[3,1], [3,2], [3,3], [3,4], [3,5], [3,6], [3,8], [3,9], " +
                "[4,1], [4,3], [4,4], [4,6], [4,7], [4,9], " +
                "[5,3], [5,5], [5,7], " +
                "[6,1], [6,3], [6,4], [6,6], [6,7], [6,9], " +
                "[7,1], [7,2], [7,4], [7,5], [7,6], [7,7], [7,8], [7,9], " +
                "[8,2], [8,4], [8,5], [8,6], [8,8], [8,9], " +
                "[9,3], [9,7], [9,8], [9,9]]");
    }

    private void assertElementAt(Element element, String expected) {
        assertEquals(expected, board.get(element).toString());
    }

    @Test
    public void shouldGetAt() {
        assertEquals("FIVE", board.getAt(1, 9).toString());
        assertEquals("NONE", board.getAt(2, 8).toString());
        assertEquals("EIGHT", board.getAt(3, 7).toString());
        assertEquals("NONE", board.getAt(4, 6).toString());
        assertEquals("NONE", board.getAt(5, 5).toString());
        assertEquals("NONE", board.getAt(6, 4).toString());
        assertEquals("TWO",  board.getAt(7, 3).toString());
        assertEquals("NONE", board.getAt(8, 2).toString());
        assertEquals("NINE", board.getAt(9, 1).toString());
    }

    @Test
    public void shouldIsAt() {
        assertTrue(board.isAt(1, 9, Element.FIVE));
        assertFalse(board.isAt(1, 9, Element.SIX));
    }
}
