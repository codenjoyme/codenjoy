package com.globallogic.snake.console;

import com.globallogic.snake.model.artifacts.BasicWalls;
import com.globallogic.snake.model.artifacts.RandomArtifactGenerator;
import com.globallogic.snake.model.Board;
import com.globallogic.snake.model.BoardImpl;

public class Main {

	private static final int BOARD_SIZE = 5;

	public static void main(String[] args) {
		Board board = new BoardImpl(new RandomArtifactGenerator(),
                new BasicWalls(BOARD_SIZE), BOARD_SIZE);
		SnakePrinter printer = new SnakePrinterImpl();
		Console console = new ConsoleImpl();
		
		new SnakeRunner(board, printer, console).playGame();
	} 

}
