package com.globallogic.snake.console;

import com.globallogic.snake.model.Board;
import com.globallogic.snake.model.Snake;


public class SnakeRunner {
	
	private Board board;
	private SnakePrinter printer;
	private Console console;
		
	public SnakeRunner(Board board, SnakePrinter printer, Console console) {
		this.board = board;
		this.printer = printer;
		this.console = console;
	}

	public void playGame() {
		Snake snake = board.getSnake();
		
		do {		
			printBoard();
			
			String line = console.read();
			if (line.length() != 0) {
				int ch = line.charAt(0);			
				
				if (ch == 115) {
					snake.turnDown();
				} else if (ch == 97) {
					snake.turnLeft();
				} else if (ch == 100) {
					snake.turnRight();
				} else if (ch == 119) {
					snake.turnUp();
				} 				
			}
			board.tact();
		} while (!board.isGameOver());
		
		printBoard();
		console.print("Game over!");
	}
	
	private void printBoard() {
		console.print(printer.print(board));	
	}

}
