package com.codenjoy.bomberman.console;

import com.codenjoy.bomberman.model.Board;
import com.codenjoy.bomberman.model.Bomberman;


public class BombermanRunner {
	
	private Board board;
	private Printer printer;
	private Console console;
		
	public BombermanRunner(Board board, Printer printer, Console console) {
		this.board = board;
		this.printer = printer;
		this.console = console;
	}

	public void playGame() {
        Bomberman bomberman = board.getBomberman();
		
		do {		
			printBoard();
			
			String line = console.read();
			if (line.length() != 0) {
				int ch = line.charAt(0);			
				
				if (ch == 's' || ch == 'ы') {
					bomberman.down();
				} else if (ch == 'a' || ch == 'ф') {
					bomberman.left();
				} else if (ch == 'd' || ch == 'в') {
					bomberman.right();
				} else if (ch == 'w' || ch == 'ц') {
					bomberman.up();
				} else if (ch == ' ') {
                    bomberman.bomb();
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
