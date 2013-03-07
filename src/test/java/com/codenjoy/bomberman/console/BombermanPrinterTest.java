package com.codenjoy.bomberman.console;

import com.codenjoy.bomberman.model.Blast;
import com.codenjoy.bomberman.model.Board;
import com.codenjoy.bomberman.model.Bomb;
import com.codenjoy.bomberman.model.Bomberman;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BombermanPrinterTest {

	private static final int BOARD_SIZE = 7;
	private BombermanPrinter printer;
	
	@Before
	public void init() {
		printer = new BombermanPrinter();
		printer.size = BOARD_SIZE;
		printer.clean();
	}
	
	@Test
	public void checkCleanBoard() {
		assertEquals("       \n       \n       \n       \n       \n       \n       \n", printer.asString());
	}
	
	@Test
	public void checkPrintBomberman() {
        Bomberman bomberman = mock(Bomberman.class);
        when(bomberman.getX()).thenReturn(2);
        when(bomberman.getY()).thenReturn(2);

        printer.printBomberman(bomberman);

		assertEquals(
				"       \n" +
				"       \n" +
				"  ☺    \n" +
				"       \n" +
				"       \n" +
				"       \n" +
				"       \n", printer.asString());
	}

    @Test
    public void checkPrintBombermanWithBomb_timer5() {
        int x = 2;
        int y = 2;
        printer.printBombs(Arrays.asList(getBomb(5, x, y)));

        assertEquals(
                "       \n" +
                "       \n" +
                "  5    \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n", printer.asString());
    }

    @Test
    public void checkPrintBombermanWithBomb_timer4() {
        int x = 2;
        int y = 2;
        printer.printBombs(Arrays.asList(getBomb(4, x, y)));

        assertEquals(
                "       \n" +
                "       \n" +
                "  4    \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n", printer.asString());
    }

    @Test
    public void checkPrintBombermanWithBomb_timer3() {
        int x = 2;
        int y = 2;
        printer.printBombs(Arrays.asList(getBomb(3, x, y)));

        assertEquals(
                "       \n" +
                "       \n" +
                "  3    \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n", printer.asString());
    }

    @Test
    public void checkPrintBombermanWithBomb_timer2() {
        int x = 2;
        int y = 2;
        printer.printBombs(Arrays.asList(getBomb(2, x, y)));

        assertEquals(
                "       \n" +
                "       \n" +
                "  2    \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n", printer.asString());
    }

    @Test
    public void checkPrintBombermanWithBomb_timer1() {
        int x = 2;
        int y = 2;
        printer.printBombs(Arrays.asList(getBomb(1, x, y)));

        assertEquals(
                "       \n" +
                "       \n" +
                "  1    \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n", printer.asString());
    }

    @Test
    public void checkPrintBombermanWithBomb_timer0() {
        int x = 2;
        int y = 2;
        printer.printBombs(Arrays.asList(getBomb(0, x, y)));

        assertEquals(
                "       \n" +
                "       \n" +
                "  0    \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "       \n", printer.asString());
    }

    @Test
    public void checkPrintBoardWithBombExploded() {
        Board board = makeBoard(2, 2, 0);

        printer.print(board);

        assertEquals(
                "       \n" +
                "       \n" +
                "  ☺    \n" +
                " ҉҉҉   \n" +
                "       \n" +
                "     0 \n" +
                "       \n", printer.asString());
    }

    private Board makeBoard(int bx, int by, int timer) {
        int bombxy = 5;
        Bomb bomb = getBomb(timer, bombxy, bombxy);
        Board board = mock(Board.class);
        Bomberman bomberman = mock(Bomberman.class);
        when(board.getBlasts()).thenReturn(Arrays.asList(
                new Blast(3, 3), new Blast(2, 3), new Blast(1, 3),
                new Blast(bombxy + timer, bombxy + timer))); // ☺
        when(board.getBomberman()).thenReturn(bomberman);
        when(board.size()).thenReturn(BOARD_SIZE);
        when(bomberman.getX()).thenReturn(bx);
        when(bomberman.getY()).thenReturn(by);
        when(board.getBombs()).thenReturn(Arrays.asList(bomb));
        return board;
    }

    @Test
    public void checkPrintBoardWithBombExploded_bombermenDie() {
        Board board = makeBoard(2, 3, 0);

        printer.print(board);

        assertEquals(
                "       \n" +
                "       \n" +
                "       \n" +
                " ҉☻҉   \n" +
                "       \n" +
                "     0 \n" +
                "       \n", printer.asString());
    }

    @Test
    public void checkPrintBoardWithBombExploded_bombermenDieAtBomb() {
        Board board = makeBoard(5, 5, 0);

        printer.print(board);

        assertEquals(
                "       \n" +
                "       \n" +
                "       \n" +
                " ҉҉҉   \n" +
                "       \n" +
                "     ☻ \n" +
                "       \n", printer.asString());
    }

    @Test
    public void checkPrintBoardWithBomb_bombermenAtBomb() {
        Board board = makeBoard(5, 5, 1);

        printer.print(board);

        assertEquals(
                "       \n" +
                "       \n" +
                "       \n" +
                " ҉҉҉   \n" +
                "       \n" +
                "     ☺ \n" +
                "      ҉\n", printer.asString());
    }

    private Bomb getBomb(int timer, int x, int y) {
        Bomb bomb = mock(Bomb.class);
        when(bomb.getX()).thenReturn(x);
        when(bomb.getY()).thenReturn(y);
        when(bomb.getBlastWaveLength()).thenReturn(3);
        when(bomb.getTimer()).thenReturn(timer);
        return bomb;
    }


}
