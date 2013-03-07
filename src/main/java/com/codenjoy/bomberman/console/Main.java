package com.codenjoy.bomberman.console;

import com.codenjoy.bomberman.model.BasicWalls;
import com.codenjoy.bomberman.model.Board;
import com.codenjoy.bomberman.model.Level;
import java.lang.String;

public class Main {

	private static final int BOARD_SIZE = 10;

	public static void main(String[] args) {
        Level level = new Level() {
            @Override
            public int bombsCount() {
                return 2;
            }
        };
        Board board = new Board(new BasicWalls(BOARD_SIZE), level, BOARD_SIZE);
		Printer printer = new BombermanPrinter();
		Console console = new ConsoleImpl();
		
		new BombermanRunner(board, printer, console).playGame();
	} 

}
