package com.codenjoy.bomberman.console;

import com.codenjoy.bomberman.model.Board;
import com.codenjoy.bomberman.model.Bomberman;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BombermanRunnerTest {

	private BombermanRunner runner;
    private Board board;
    private Printer printer;
    private Console console;
    private Bomberman bomberman;

    @Before
	public void initMocks() {
		board = mock(Board.class);
		printer = mock(Printer.class);
		console = mock(Console.class);
        bomberman = mock(Bomberman.class);
        when(board.getBomberman()).thenReturn(bomberman);

		runner = new BombermanRunner(board, printer, console);
	}

	// проверяем что доска будет изъята из принтера и напечатана на консоли
    // в принтер передаются все артефакты доски чтобы с них напечатать все что надо
	@Test
	public void shouldPrintBoardWhenStartGame() {
		// given
        when(printer.print(board)).thenReturn("board");
        when(board.isGameOver()).thenReturn(false, true);
        when(console.read()).thenReturn("");

		// when
		runner.playGame();
		
		// then
        verify(printer, times(3)).print(board);
        verify(console, times(3)).print("board");
        verify(console).print("Game over!");
	}

	// хочу проверить что после каждого цикла будет вызван метод tact()
	@Test
	public void shouldCallTactOnEachCycle() {
		// given
        when(board.isGameOver()).thenReturn(false, false, false, true);
        when(console.read()).thenReturn("");

		// when
		runner.playGame();

		// then
		verify(board, times(4)).tact();
	}

	// хочу проверить что при нажатии на S/Ы вызовется метод down бомбермена
	@Test
	public void shouldCallDownWhenPressSButton() {
		// given
        when(board.isGameOver()).thenReturn(false, true);
        when(console.read()).thenReturn("s", "ы");

		// when
		runner.playGame();

		// then
        verify(bomberman, times(2)).down();
	}

	// хочу проверить что при нажатии на A/Ф вызовется метод left бомбермена
	@Test
	public void shouldCallLeftWhenPressAButton() {
		// given
        when(board.isGameOver()).thenReturn(false, true);
        when(console.read()).thenReturn("a", "ф");

		// when
		runner.playGame();

		// then
        verify(bomberman, times(2)).left();
	}

	// хочу проверить что при нажатии на D/В вызовется метод right бомбермена
	@Test
	public void shouldCallRightWhenPressDButton() {
		// given
        when(board.isGameOver()).thenReturn(false, true);
        when(console.read()).thenReturn("d", "в");

		// when
		runner.playGame();

		// then
        verify(bomberman, times(2)).right();
	}

	// хочу проверить что при нажатии на W/Ц вызовется метод up бомбермена
	@Test
	public void shouldCallUpWhenPressWButton() {
		// given
        when(board.isGameOver()).thenReturn(false, true);
        when(console.read()).thenReturn("w", "ц");

		// when
		runner.playGame();

		// then
        verify(bomberman, times(2)).up();
	}

    // хочу проверить что при нажатии на пробел вызовется метод bomb бомбермена
    @Test
    public void shouldCallBombWhenPressSpaceButton() {
        // given
        when(board.isGameOver()).thenReturn(true);
        when(console.read()).thenReturn(" ");

        // when
        runner.playGame();

        // then
        verify(bomberman).bomb();
    }
	
	
}
