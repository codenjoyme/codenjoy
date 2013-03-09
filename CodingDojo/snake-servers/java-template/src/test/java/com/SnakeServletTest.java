package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnakeServletTest {

    private final SnakeServlet servlet = new SnakeServlet();

    @Test
    public void should_when() {
        assertEquals("LEFT", servlet.answer(
                "☼☼☼☼☼☼" +
                "☼ ☺  ☼" +
                "☼  ☻ ☼" +
                "☼  ▲ ☼" +
                "☼  ○ ☼" +
                "☼☼☼☼☼☼"));
    }

}
