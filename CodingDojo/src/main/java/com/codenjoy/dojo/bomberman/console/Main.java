package com.codenjoy.dojo.bomberman.console;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.*;

import java.lang.String;

public class Main {

	public static void main(String[] args) {
        Game game = new SingleBoard(new Board(new DefaultGameSettings(), null));
        game.newGame();
		Console console = new ConsoleImpl();
		
		new Runner(game, console).playGame();
	}

}
