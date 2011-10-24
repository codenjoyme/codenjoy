package com.globallogic.snake;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Queue;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class SnakeRunnerTest {

	private MockedBoard board;
	private MockedPrinter printer;
	private MockedConsole console;	
	private SnakeRunner runner;
	
	@Before
	public void initMocks() {
		board = new MockedBoard();
		printer = new MockedPrinter();
		console = new MockedConsole();		
		runner = new SnakeRunner(board, printer, console);
	}
	
	class MockedBufferedReader extends BufferedReader {

		public MockedBufferedReader(Reader arg0) {
			super(arg0);
		}
		
	}
	
	class MockedBoard implements Board {

		private Queue<Boolean> isGameOver = new LinkedList<Boolean>();

		@Override
		public Apple getApple() {
			return null;
		}

		@Override
		public int getSize() {
			return 0;
		}

		@Override
		public Snake getSnake() {
			return null;
		}

		@Override
		public Stone getStone() {
			return null;
		}

		@Override
		public boolean isGameOver() {
			return this.isGameOver.remove();
		}

		@Override
		public void tact() {
			
		}

		public void shoudReturnWhenIsGameOver(boolean...booleans) {
			for (boolean b : booleans) {
				this.isGameOver.add(b);
			}
		}		
	}
	
	class MockedPrinter implements SnakePrinter {

		private String string;
		private Board printedBoard;

		@Override
		public String print(Board board) {
			this.printedBoard = board;
			return string;
		}

		public void shoudReturnWhenPrintBoard(String string) {
			this.string = string;
		}

		public void assertProcessedBoard(Board board) {
			Assert.assertSame(board, printedBoard);
		}
		
	}
	
	class MockedConsole implements Console{

		private Queue<String> printed = new LinkedList<String>();
		private String pressed;

		@Override
		public void print(String string) {
			this.printed.add(string);
		}

		@Override
		public String read() {
			return pressed;
		}

		public void assertPrinted(String expeced) {
			Assert.assertEquals(expeced, printed.remove());			
		}

		public void shoudReturnButtonPressed(String string) {
			this.pressed = string;
		}

		public void assertNothingMore() {
			Assert.assertTrue(printed.isEmpty());
		}
		
	}
	
	// проверяем что доска будет изъята из принтера и напечатана на консоли 
	@Test 
	public void shouldPrintBoardWhenStartGame() {			
		// given
		console.shoudReturnButtonPressed("");
		board.shoudReturnWhenIsGameOver(false, true);
		printer.shoudReturnWhenPrintBoard("board");

		// when
		runner.playGame();
		
		// then
		console.assertPrinted("board");
		console.assertPrinted("board");
		console.assertPrinted("Game over!");  
		console.assertNothingMore();	
	}
	
	// хочу проверить что в принтер передаются все артефакты доски чтобы с них напечатать все что надо
	@Test 
	public void shouldBoardProcessedOnPrinter() {			
		// given
		console.shoudReturnButtonPressed("");
		board.shoudReturnWhenIsGameOver(false, true);
		
		// when
		runner.playGame();
		
		// then
		printer.assertProcessedBoard(board);
	}
	
	
}
