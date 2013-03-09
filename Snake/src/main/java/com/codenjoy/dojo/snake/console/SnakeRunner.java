package com.codenjoy.dojo.snake.console;

import com.codenjoy.dojo.snake.model.Board;
import com.codenjoy.dojo.snake.model.Snake;


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
				
				if (ch == 's' || ch == 'ы') {
					snake.turnDown();
				} else if (ch == 'a' || ch == 'ф') {
					snake.turnLeft();
				} else if (ch == 'd' || ch == 'в') {
					snake.turnRight();
				} else if (ch == 'w' || ch == 'ц') {
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
