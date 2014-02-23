package com.codenjoy.dojo.services;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 21:22
 */
public class BoardGameStateTest {

    @Test
    public void test() {
        BoardGameState state = new BoardGameState("brd");
        assertEquals("board=brd", state.asString());
    }
}
