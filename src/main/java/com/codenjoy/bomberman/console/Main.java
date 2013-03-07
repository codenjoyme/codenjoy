package com.codenjoy.bomberman.console;

import com.codenjoy.bomberman.model.Board;
import com.codenjoy.bomberman.model.Level;

public class Main {

	private static final int BOARD_SIZE = 10;

	public static void main(String[] args) {
		Board board = new Board(new Level() {
            @Override
            public int bombsCount() {
                return 2;
            }
        }, BOARD_SIZE);
		Printer printer = new BombermanPrinter();
		Console console = new ConsoleImpl();
		
		new BombermanRunner(board, printer, console).playGame();
	} 

}
