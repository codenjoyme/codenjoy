package com.codenjoy.dojo.bomberman.console;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.Console;
import com.codenjoy.dojo.services.ConsoleImpl;
import com.codenjoy.dojo.services.Printer;

import java.lang.String;

public class Main {

	public static void main(String[] args) {
        Board board = new Board(new DefaultGameSettings(), null);

        board.newGame();

		Printer printer = new BombermanPrinter(board);
		Console console = new ConsoleImpl();
		
		new BombermanRunner(board, printer, console).playGame();
	} 

}
