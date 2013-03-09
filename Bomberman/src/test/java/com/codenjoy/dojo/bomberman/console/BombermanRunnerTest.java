package com.codenjoy.dojo.bomberman.console;

import com.codenjoy.dojo.bomberman.model.Board;
import com.codenjoy.dojo.bomberman.model.Bomberman;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BombermanRunnerTest {

	private BombermanRunner runner;
    private Board board;
    private Printer printer;
    private Console console;
    private Bomberman bomberman;
    private List<String> calls = new LinkedList<String>();

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

    @Test
    public void shouldWorkWithManyCommands1() {
        String pattern = " * *";
        with(pattern, 's', "[bomb, down, tact, bomb, down, tact, gameover]");
        with(pattern, 'a', "[bomb, left, tact, bomb, left, tact, gameover]");
        with(pattern, 'd', "[bomb, right, tact, bomb, right, tact, gameover]");
        with(pattern, 'w', "[bomb, up, tact, bomb, up, tact, gameover]");
    }

    private void with(String pattern, char command, String expected) {
        // given
        when(console.read()).thenReturn(pattern.replace('*', command));

        calls.clear();
        init(board, "tact", null).tact();
        init(board, "gameover", true).isGameOver();
        init(bomberman, "right", null).right();
        init(bomberman, "left", null).left();
        init(bomberman, "down", null).down();
        init(bomberman, "up", null).up();
        init(bomberman, "bomb", null).bomb();

        // when
        runner.playGame();

        // then
        assertEquals(expected, calls.toString());
    }

    private <T> T init(T mock, final String method, final Object result) {
        return doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                calls.add(method);
                return result;
            }
        }).when(mock);
    }

    @Test
    public void shouldWorkWithManyCommands2() {
        String pattern = "* * ";
        with(pattern, 's', "[down, bomb, tact, down, bomb, tact, gameover]");
        with(pattern, 'a', "[left, bomb, tact, left, bomb, tact, gameover]");
        with(pattern, 'd', "[right, bomb, tact, right, bomb, tact, gameover]");
        with(pattern, 'w', "[up, bomb, tact, up, bomb, tact, gameover]");
    }

    @Test
    public void shouldWorkWithManyCommands3() {
        String pattern = "** *";
        with(pattern, 's', "[down, tact, down, bomb, tact, down, tact, gameover]");
        with(pattern, 'a', "[left, tact, left, bomb, tact, left, tact, gameover]");
        with(pattern, 'd', "[right, tact, right, bomb, tact, right, tact, gameover]");
        with(pattern, 'w', "[up, tact, up, bomb, tact, up, tact, gameover]");
    }

    @Test
    public void shouldWorkWithManyCommands4() {
        String pattern = " ** ";
        with(pattern, 's', "[bomb, down, tact, down, bomb, tact, gameover]");
        with(pattern, 'a', "[bomb, left, tact, left, bomb, tact, gameover]");
        with(pattern, 'd', "[bomb, right, tact, right, bomb, tact, gameover]");
        with(pattern, 'w', "[bomb, up, tact, up, bomb, tact, gameover]");
    }
	
	
}
