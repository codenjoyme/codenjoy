package com.codenjoy.bomberman;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:07 AM
 */
public class BoardTest {

    @Test
    public void shouldBoard_whenStartGame() {
        Board board = new Board(10);
        assertEquals(10, board.getSize());
    }

    @Test
    public void shouldBoard_whenStartGame2() {
        Board board = new Board(20);
        assertEquals(20, board.getSize());
    }

    @Test
    public void shouldBombermanOnBoardAtInitPos_whenGameStart() {
        // TODO to setup()
        Board board = new Board(20);

        Bomberman bomberman = board.getBomberman();

        assertEquals(0, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }
}
