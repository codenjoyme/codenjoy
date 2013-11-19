package com;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SnakeTest {

    private final SnakeServlet servlet = new SnakeServlet();

    @Test
    public void should_accept_missing_input() {
        Board board = new Board(
                "☼☼☼☼☼☼" +
                "☼ ☺  ☼" +
                "☼  ☻ ☼" +
                "☼  ▲ ☼" +
                "☼  ○ ☼" +
                "☼☼☼☼☼☼");
        System.out.println(board);
        assertEquals("RIGHT", servlet.answer(board));
    }

}
