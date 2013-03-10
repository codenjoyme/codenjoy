package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BombermanServletTest {

    private final BombermanServlet servlet = new BombermanServlet();

    @Test
    public void shouldLengthToXYWorksOk() {
        assertEquals("[2,2]", new LengthToXY(4).getXY(10).toString());
        assertEquals(10, new LengthToXY(4).getLength(2, 2));
    }

    @Test
    public void shouldGetBombermanPos() {
        assertEquals("[1,1]", new Board(
                "☼☼☼☼" +
                "☼☺ ☼" +
                "☼  ☼" +
                "☼☼☼☼").getBomberman().toString());

        assertEquals("[1,2]", new Board(
                "☼☼☼☼" +
                "☼  ☼" +
                "☼☺ ☼" +
                "☼☼☼☼").getBomberman().toString());

        assertEquals("[4,4]", new Board(
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼   ☺☼" +
                "☼☼☼☼☼☼").getBomberman().toString());
    }

    @Test
    public void shouldGetMeatChoppersPos() {
        assertEquals("[[1,1]]", new Board(
                "☼☼☼☼" +
                "☼& ☼" +
                "☼  ☼" +
                "☼☼☼☼").getMeatChoppers().toString());

        assertEquals("[[1,2], [2,2]]", new Board(
                "☼☼☼☼" +
                "☼  ☼" +
                "☼&&☼" +
                "☼☼☼☼").getMeatChoppers().toString());

        assertEquals("[[2,1], [3,4], [4,4]]", new Board(
                "☼☼☼☼☼☼" +
                "☼ &  ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼  &&☼" +
                "☼☼☼☼☼☼").getMeatChoppers().toString());
    }


}
