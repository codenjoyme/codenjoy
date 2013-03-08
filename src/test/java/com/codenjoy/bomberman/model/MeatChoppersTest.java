package com.codenjoy.bomberman.model;

import com.codenjoy.bomberman.console.BombermanPrinter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 11:44 PM
 */
public class MeatChoppersTest {

    private static final int SIZE = 11;
    private MeatChoppers walls;
    private BombermanPrinter printer;
    private Dice dice;
    private Board board;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        dice(9, 9);
        walls = new MeatChoppers(new OriginalWalls(SIZE), SIZE, 1, dice);
        board = new Board(walls, null, SIZE);
        printer = new BombermanPrinter(SIZE);
    }

    @Test
    public void shouldRandomMoveMonster() {
        assertBoard(
                "☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼        &☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(Direction.UP.value);
        board.tact();

        assertBoard(
                "☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼&☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(1);
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();

        assertBoard(
                "☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼        &☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(0, Direction.LEFT.value);
        board.tact();

        assertBoard(
                "☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼       & ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        board.tact();

        assertBoard(
                "☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼&        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(Direction.UP.value);
        board.tact();

        assertBoard(
                "☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼☺        ☼\n" +
                "☼&☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        board.tact();

        assertBoard(
                "☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼Ѡ        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        assertTrue(board.isGameOver());
        assertFalse(board.getBomberman().isAlive());
    }

    private void assertBoard(String expected) {
        assertEquals(expected, printer.print(board));
    }

    private void dice(int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }
}
