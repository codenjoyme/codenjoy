package com.codenjoy.dojo.bomberman.console;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.Console;
import com.codenjoy.dojo.services.ConsoleImpl;
import com.codenjoy.dojo.services.Printer;
import com.codenjoy.dojo.services.Runner;

import java.lang.String;

public class Main {

	public static void main(String[] args) {
        Board board = new Board(new DefaultGameSettings(), null);
        board.newGame();
		Console console = new ConsoleImpl();
		
		new Runner(board, console).playGame();
	}

}
