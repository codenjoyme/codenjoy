package com.codenjoy.dojo.sudoku.client.utils;

import com.codenjoy.dojo.sudoku.client.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
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
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    private void asrtBrd(String expected) {
        assertEquals(expected, board.toString());
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
        assertEquals("5", board.getAt(1, 9).toString());
        assertEquals("?", board.getAt(2, 8).toString());
        assertEquals("8", board.getAt(3, 7).toString());
        assertEquals("?", board.getAt(4, 6).toString());
        assertEquals("?", board.getAt(5, 5).toString());
        assertEquals("?", board.getAt(6, 4).toString());
        assertEquals("2", board.getAt(7, 3).toString());
        assertEquals("?", board.getAt(8, 2).toString());
        assertEquals("9", board.getAt(9, 1).toString());
    }

    @Test
    public void shouldIsAt() {
        assertTrue(board.isAt(1, 9, Element.FIVE));
        assertFalse(board.isAt(1, 9, Element.SIX));
    }

    @Test
    public void shouldGetY() {
        assertEquals("[5, 3, 0, 0, 7, 0, 0, 0, 0]", board.getY(9).toString());
        assertEquals("[6, 0, 0, 1, 9, 5, 0, 0, 0]", board.getY(8).toString());
        assertEquals("[0, 9, 8, 0, 0, 0, 0, 6, 0]", board.getY(7).toString());
        assertEquals("[8, 0, 0, 0, 6, 0, 0, 0, 3]", board.getY(6).toString());
        assertEquals("[4, 0, 0, 8, 0, 3, 0, 0, 1]", board.getY(5).toString());
        assertEquals("[7, 0, 0, 0, 2, 0, 0, 0, 6]", board.getY(4).toString());
        assertEquals("[0, 6, 0, 0, 0, 0, 2, 8, 0]", board.getY(3).toString());
        assertEquals("[0, 0, 0, 4, 1, 9, 0, 0, 5]", board.getY(2).toString());
        assertEquals("[0, 0, 0, 0, 8, 0, 0, 7, 9]", board.getY(1).toString());
    }

    @Test
    public void shouldGetX() {
        assertEquals("[0, 0, 0, 7, 4, 8, 0, 6, 5]", board.getX(1).toString());
        assertEquals("[0, 0, 6, 0, 0, 0, 9, 0, 3]", board.getX(2).toString());
        assertEquals("[0, 0, 0, 0, 0, 0, 8, 0, 0]", board.getX(3).toString());
        assertEquals("[0, 4, 0, 0, 8, 0, 0, 1, 0]", board.getX(4).toString());
        assertEquals("[8, 1, 0, 2, 0, 6, 0, 9, 7]", board.getX(5).toString());
        assertEquals("[0, 9, 0, 0, 3, 0, 0, 5, 0]", board.getX(6).toString());
        assertEquals("[0, 0, 2, 0, 0, 0, 0, 0, 0]", board.getX(7).toString());
        assertEquals("[7, 0, 8, 0, 0, 0, 6, 0, 0]", board.getX(8).toString());
        assertEquals("[9, 5, 0, 6, 1, 3, 0, 0, 0]", board.getX(9).toString());
    }

    @Test
    public void shouldGetC() {
        assertEquals("[0, 0, 0, 0, 0, 0, 0, 6, 0]", board.getC(1, 1).toString());
        assertEquals("[7, 0, 0, 4, 0, 0, 8, 0, 0]", board.getC(1, 2).toString());
        assertEquals("[0, 9, 8, 6, 0, 0, 5, 3, 0]", board.getC(1, 3).toString());
        assertEquals("[0, 8, 0, 4, 1, 9, 0, 0, 0]", board.getC(2, 1).toString());
        assertEquals("[0, 2, 0, 8, 0, 3, 0, 6, 0]", board.getC(2, 2).toString());
        assertEquals("[0, 0, 0, 1, 9, 5, 0, 7, 0]", board.getC(2, 3).toString());
        assertEquals("[0, 7, 9, 0, 0, 5, 2, 8, 0]", board.getC(3, 1).toString());
        assertEquals("[0, 0, 6, 0, 0, 1, 0, 0, 3]", board.getC(3, 2).toString());
        assertEquals("[0, 6, 0, 0, 0, 0, 0, 0, 0]", board.getC(3, 3).toString());
    }

    @Test
    public void shouldSet() {
        board.set(1, 1, 1);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
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
                "☼1  ☼ 8 ☼ 79☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        board.set(3, 7, 3);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼53 ☼ 7 ☼   ☼\n" +
                "☼6  ☼195☼   ☼\n" +
                "☼ 93☼   ☼ 6 ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼8  ☼ 6 ☼  3☼\n" +
                "☼4  ☼8 3☼  1☼\n" +
                "☼7  ☼ 2 ☼  6☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ 6 ☼   ☼28 ☼\n" +
                "☼   ☼419☼  5☼\n" +
                "☼1  ☼ 8 ☼ 79☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");

        board.set(9, 1, 2);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼53 ☼ 7 ☼   ☼\n" +
                "☼6  ☼195☼   ☼\n" +
                "☼ 93☼   ☼ 6 ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼8  ☼ 6 ☼  3☼\n" +
                "☼4  ☼8 3☼  1☼\n" +
                "☼7  ☼ 2 ☼  6☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ 6 ☼   ☼28 ☼\n" +
                "☼   ☼419☼  5☼\n" +
                "☼1  ☼ 8 ☼ 72☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼\n");
    }
}
