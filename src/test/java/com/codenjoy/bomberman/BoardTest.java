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
    private Bomberman bomberman;

    @Before
    public void setUp() throws Exception {
        board = new Board(20);
        bomberman = board.getBomberman();
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
        assertEquals(0, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }

    @Test
    public void shouldBombermanOnBoardOneRightStep_whenCallRightCommand() {
        bomberman.right();

        assertEquals(1, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }

    @Test
    public void shouldBombermanOnBoardTwoRightSteps_whenCallRightCommandTwice() {
        bomberman.right();
        bomberman.right();

        assertEquals(2, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }

    @Test
    public void shouldBombermanOnBoardOneDownStep_whenCallDownCommand() {
        bomberman.down();

        assertEquals(0, bomberman.getX());
        assertEquals(1, bomberman.getY());
    }

    @Test
    public void shouldBombermanWalkUp() {
        bomberman.down();
        bomberman.down();

        bomberman.up();

        assertEquals(0, bomberman.getX());
        assertEquals(1, bomberman.getY());
    }

    @Test
    public void shouldBombermanWalkUp_WhenInitPosition() {
        bomberman.up();

        assertEquals(0, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }

    @Test
    public void shouldBombermanWalkLeft() {
        bomberman.right();
        bomberman.right();

        bomberman.left();

        assertEquals(1, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }

    @Test
    public void shouldBombermanWalkLeft_whenInitPosition() {
        bomberman.left();

        assertEquals(0, bomberman.getX());
        assertEquals(0, bomberman.getY());
    }
}
