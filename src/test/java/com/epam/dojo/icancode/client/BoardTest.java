package com.epam.dojo.icancode.client;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Mikhail_Udalyi on 20.09.2016.
 */
public class BoardTest {
    private Board board;

    public static Board board(String... boardString) {
        return (Board) new Board().forString(boardString);
    }

    @Before
    public void before() {
        board = board("╔═════════┐" +
                        "║.........│" +
                        "║.S.┌─╗...│" +
                        "║...│ ║...│" +
                        "║.┌─┘ └─╗.│" +
                        "║.│     ║.│" +
                        "║.╚═┐ ╔═╝.│" +
                        "║...│ ║...│" +
                        "║...╚═╝...│" +
                        "║OB$.....E│" +
                        "└─────────┘",
                        "-----------" +
                        "-----------" +
                        "--☺--------" +
                        "-----------" +
                        "-----------" +
                        "-----------" +
                        "-----------" +
                        "-----------" +
                        "-----------" +
                        "-----------" +
                        "-----------");
    }

    @Test
    public void shouldWorkToString() {
        assertEquals("Board:\n" +
                "╔═════════┐\n" +
                "║.........│\n" +
                "║.S.┌─╗...│\n" +
                "║...│ ║...│\n" +
                "║.┌─┘ └─╗.│\n" +
                "║.│     ║.│\n" +
                "║.╚═┐ ╔═╝.│\n" +
                "║...│ ║...│\n" +
                "║...╚═╝...│\n" +
                "║OB$.....E│\n" +
                "└─────────┘\n" +
                "\n" +
                "-----------\n" +
                "-----------\n" +
                "--☺--------\n" +
                "-----------\n" +
                "-----------\n" +
                "-----------\n" +
                "-----------\n" +
                "-----------\n" +
                "-----------\n" +
                "-----------\n" +
                "-----------\n", board.toString());
    }

    @Test
    public void shouldFindMe() {
        assertEquals("[2,2]", board.getMe().toString());
    }

    @Test
    public void shouldFindExit() {
        assertEquals("[9,9]", board.getExit().toString());
    }

    @Test
    public void shouldBeBarriers() {
        assertEquals(true, board.isBarrierAt(0, 0));
        assertEquals(true, board.isBarrierAt(1, 0));
        assertEquals(true, board.isBarrierAt(0, 1));

        assertEquals(true, board.isBarrierAt(4, 4));
        assertEquals(true, board.isBarrierAt(5, 4));
        assertEquals(true, board.isBarrierAt(4, 5));

        assertEquals(true, board.isBarrierAt(5, 8));
        assertEquals(true, board.isBarrierAt(6, 8));
        assertEquals(true, board.isBarrierAt(6, 7));

        assertEquals(true, board.isBarrierAt(10, 10));
        assertEquals(true, board.isBarrierAt(9, 10));
        assertEquals(true, board.isBarrierAt(10, 9));

        assertEquals(true, board.isBarrierAt(5, 5));

        assertEquals(true, board.isBarrierAt(2, 2));//there is my robot

        assertEquals(false, board.isBarrierAt(1, 1));
        assertEquals(false, board.isBarrierAt(9, 9));

        assertEquals(true, board.isBarrierAt(1, 9));
        assertEquals(true, board.isBarrierAt(2, 9));
        assertEquals(false, board.isBarrierAt(3, 9));
    }

    @Test
    public void shouldNotBeGameOver() {
        assertEquals(false, board.isGameOver());
    }
}
