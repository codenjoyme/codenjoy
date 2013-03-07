package com.codenjoy.bomberman;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:07 AM
 */
public class BoardTest {

    public static final int SIZE = 20;
    private Board board;
    private Bomberman bomberman;

    @Before
    public void setUp() throws Exception {
        board = new Board(SIZE);
        bomberman = board.getBomberman();
    }

    @Test
    public void shouldBoard_whenStartGame() {
        Board board = new Board(10);
        assertEquals(10, board.getSize());
    }

    @Test
    public void shouldBoard_whenStartGame2() {
        assertEquals(20, board.getSize());
    }

    @Test
    public void shouldBombermanOnBoardAtInitPos_whenGameStart() {
        assertBombermanAt(0, 0);
    }

    @Test
    public void shouldBombermanOnBoardOneRightStep_whenCallRightCommand() {
        bomberman.right();
        board.tact();

        assertBombermanAt(1, 0);
    }

    @Test
    public void shouldBombermanOnBoardTwoRightSteps_whenCallRightCommandTwice() {
        bomberman.right();
        board.tact();
        bomberman.right();
        board.tact();

        assertBombermanAt(2, 0);
    }

    @Test
    public void shouldBombermanOnBoardOneDownStep_whenCallDownCommand() {
        bomberman.down();
        board.tact();

        assertBombermanAt(0, 1);
    }

    @Test
    public void shouldBombermanWalkUp() {
        bomberman.down();
        board.tact();
        bomberman.down();
        board.tact();

        bomberman.up();
        board.tact();

        assertBombermanAt(0, 1);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallUp() {
        bomberman.up();
        board.tact();

        assertBombermanAt(0, 0);
    }

    private void assertBombermanAt(int x, int y) {
        assertEquals(x, bomberman.getX());
        assertEquals(y, bomberman.getY());
    }

    @Test
    public void shouldBombermanWalkLeft() {
        bomberman.right();
        board.tact();
        bomberman.right();
        board.tact();

        bomberman.left();
        board.tact();

        assertBombermanAt(1, 0);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallLeft() {
        bomberman.left();
        board.tact();

        assertBombermanAt(0, 0);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallRight() {
        for (int x = 0; x <= SIZE + 1; x++) {
            bomberman.right();
            board.tact();
        }

        assertBombermanAt(SIZE - 1, 0);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallDown() {
        for (int y = 0; y <= SIZE + 1; y++) {
            bomberman.down();
            board.tact();
        }

        assertBombermanAt(0, SIZE - 1);
    }

    @Test
    public void shouldBombermanMovedOncePerTact() {
        bomberman.down();
        bomberman.right();
        bomberman.up();
        bomberman.left();
        board.tact();

        assertBombermanAt(0, 1);

        bomberman.right();
        bomberman.up();
        bomberman.left();
        bomberman.down();
        board.tact();

        assertBombermanAt(1, 1);
    }

    @Test
    public void shouldBombDropped_whenBombermanDropBomb() {
        bomberman.bomb();
        board.tact();

        assertBombAt(0, 0);
    }

    private void assertBombAt(int x, int y) {
        List<Bomb> bombs = board.getBombs();
        assertEquals(1, bombs.size());

        Bomb bomb = bombs.get(0);
        assertEquals(x, bomb.getX());
        assertEquals(y, bomb.getY());
    }
}
