package com.codenjoy.dojo.bomberman.console;

import com.codenjoy.dojo.bomberman.model.Board;
import com.codenjoy.dojo.bomberman.model.Bomberman;


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
            boolean bomb = false;
            boolean move = false;
			for (Character ch : line.toCharArray()) {
				if (ch == 's' || ch == 'ы') {
                    if (move) {
                        board.tact();
                        bomb = false;
                    }
                    bomberman.down();
                    move = true;
				} else if (ch == 'a' || ch == 'ф') {
                    if (move) {
                        board.tact();
                        bomb = false;
                    }
					bomberman.left();
                    move = true;
				} else if (ch == 'd' || ch == 'в') {
                    if (move) {
                        board.tact();
                        bomb = false;
                    }
					bomberman.right();
                    move = true;
				} else if (ch == 'w' || ch == 'ц') {
                    if (move) {
                        board.tact();
                        bomb = false;
                    }
					bomberman.up();
                    move = true;
				} else if (ch == ' ') {
                    if (bomb) {
                        board.tact();
                        move = false;
                    }
                    bomberman.bomb();
                    bomb = true;
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
