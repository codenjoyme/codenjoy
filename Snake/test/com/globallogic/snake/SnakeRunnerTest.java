package com.globallogic.snake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import junit.framework.Assert;

import org.junit.Test;

public class SnakeRunnerTest {

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

		private String board;

		@Override
		public String print(Snake snake, Stone stone, Apple apple) {
			return board;
		}

		public void shoudReturnWhenPrintBoard(String string) {
			this.board = string;
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
	
	@Test 
	public void shouldPrintBoardWhenStartGame() {		
		MockedBoard board = new MockedBoard();
		MockedPrinter printer = new MockedPrinter();
		MockedConsole console = new MockedConsole();
		
		SnakeRunner runner = new SnakeRunner(board, printer, console);
		
		console.shoudReturnButtonPressed("");
		board.shoudReturnWhenIsGameOver(false, true);
		printer.shoudReturnWhenPrintBoard("board");

		runner.playGame();
		
		console.assertPrinted("board");
		console.assertPrinted("board");
		console.assertPrinted("Game over!");
		console.assertNothingMore();
	}
	
}
