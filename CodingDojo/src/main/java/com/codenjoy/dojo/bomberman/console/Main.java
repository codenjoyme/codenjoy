package com.codenjoy.dojo.bomberman.console;

import com.codenjoy.dojo.bomberman.model.Board;
import com.codenjoy.dojo.bomberman.model.SingleBoard;
import com.codenjoy.dojo.bomberman.services.DefaultGameSettings;
import com.codenjoy.dojo.services.*;

public class Main {

	public static void main(String[] args) {
        Board board = new Board(new DefaultGameSettings());
        Game game = new SingleBoard(board, new Ticker(board), null);
        game.newGame();
		Console console = new ConsoleImpl();
		
		new Runner(game, console).playGame();
	}

}
