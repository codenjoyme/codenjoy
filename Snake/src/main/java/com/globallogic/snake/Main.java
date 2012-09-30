package com.globallogic.snake;

import com.globallogic.snake.console.Console;
import com.globallogic.snake.console.ConsoleImpl;
import com.globallogic.snake.console.SnakePrinter;
import com.globallogic.snake.console.SnakePrinterImpl;
import com.globallogic.snake.console.SnakeRunner;
import com.globallogic.snake.model.artifacts.RandomArtifactGenerator;
import com.globallogic.snake.model.Board;
import com.globallogic.snake.model.BoardImpl;

public class Main {

	private static final int BOARD_SIZE = 7;

	public static void main(String[] args) {
		Board board = new BoardImpl(new RandomArtifactGenerator(), BOARD_SIZE);
		SnakePrinter printer = new SnakePrinterImpl();
		Console console = new ConsoleImpl();
		
		new SnakeRunner(board, printer, console).playGame();
	} 

}
