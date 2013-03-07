package com.codenjoy.bomberman;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:07 AM
 */
public class BoardTest {

    private Board board;

    @Before
    public void setUp() throws Exception {
        board = new Board(20);
    }

    @Test
    public void shouldBoard_whenStartGame() {
        Board board = new Board(10);
        assertEquals(10, board.getSize());
    }

    @Test
    public void shouldBoard_whenStartGame2() {
        assertEquals(20, this.board.getSize());
    }

    @Test
    public void shouldBombermanOnBoardAtInitPos_whenGameStart() {
        Bomberman bomberman = board.getBomberman();

        assertEquals(0, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }

    @Test
    public void shouldBombermanOnBoardOneRightStep_whenCallRightCommand() {
        Bomberman bomberman = board.getBomberman();

        bomberman.right();

        assertEquals(1, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }

    @Test
    public void shouldBombermanOnBoardTwoRightSteps_whenCallRightCommandTwice() {
        Bomberman bomberman = board.getBomberman();

        bomberman.right();
        bomberman.right();

        assertEquals(2, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }

    @Test
    public void shouldBombermanOnBoardOneDownStep_whenCallDownCommand() {
        Bomberman bomberman = board.getBomberman();

        bomberman.down();

        assertEquals(0, bomberman.getX());
        assertEquals(1, bomberman.getY());
    }

    @Test
    public void shouldBombermanWalkUp() {
        Bomberman bomberman = board.getBomberman();
        bomberman.down();
        bomberman.down();

        bomberman.up();

        assertEquals(0, bomberman.getX());
        assertEquals(1, bomberman.getY());
    }
}
