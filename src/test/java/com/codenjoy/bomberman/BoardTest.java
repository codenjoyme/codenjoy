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
        assertBombermanAt(0, 0);
    }

    @Test
    public void shouldBombermanOnBoardOneRightStep_whenCallRightCommand() {
        bomberman.right();

        assertBombermanAt(1, 0);
    }

    @Test
    public void shouldBombermanOnBoardTwoRightSteps_whenCallRightCommandTwice() {
        bomberman.right();
        bomberman.right();

        assertBombermanAt(2, 0);
    }

    @Test
    public void shouldBombermanOnBoardOneDownStep_whenCallDownCommand() {
        bomberman.down();

        assertBombermanAt(0, 1);
    }

    @Test
    public void shouldBombermanWalkUp() {
        bomberman.down();
        bomberman.down();

        bomberman.up();

        assertBombermanAt(0, 1);
    }

    @Test
    public void shouldBombermanWalkUp_WhenInitPosition() {
        bomberman.up();

        assertBombermanAt(0, 0);
    }

    private void assertBombermanAt(int x, int y) {
        assertEquals(x, bomberman.getX());
        assertEquals(y, bomberman.getY());
    }

    @Test
    public void shouldBombermanWalkLeft() {
        bomberman.right();
        bomberman.right();

        bomberman.left();

        assertBombermanAt(1, 0);
    }

    @Test
    public void shouldBombermanWalkLeft_whenInitPosition() {
        bomberman.left();

        assertBombermanAt(0, 0);
    }
}
