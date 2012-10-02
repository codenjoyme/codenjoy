package com;

import static org.junit.Assert.assertEquals;

import com.SnakeServlet;
import org.junit.Test;

public class SnakeServletTest {

    private final SnakeServlet servlet = new SnakeServlet();

    @Test
    public void should_accept_missing_input() {
        Board board = new Board(
                "******" +
                "* @  *" +
                "*  X *" +
                "*  # *" +
                "*  0 *" +
                "******");
        System.out.println(board);
        assertEquals("up", servlet.answer(board));
    }

}
