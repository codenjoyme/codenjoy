package com.codenjoy.bomberman.console;

import com.codenjoy.bomberman.model.*;

import java.lang.String;

public class Main {

	private static final int BOARD_SIZE = 15;

	public static void main(String[] args) {
        Level level = new Level() {
            @Override
            public int bombsCount() {
                return 2;
            }

            @Override
            public int bombsPower() {
                return 1;
            }
        };

        OriginalWalls walls1 = new OriginalWalls(BOARD_SIZE);
        DestroyWalls walls2 = new DestroyWalls(walls1, BOARD_SIZE, new RandomDice());
        MeatChoppers walls3 = new MeatChoppers(walls2, BOARD_SIZE, 5, new RandomDice());

        Board board = new Board(walls3, level, BOARD_SIZE);
		Printer printer = new BombermanPrinter(BOARD_SIZE);
		Console console = new ConsoleImpl();
		
		new BombermanRunner(board, printer, console).playGame();
	} 

}
