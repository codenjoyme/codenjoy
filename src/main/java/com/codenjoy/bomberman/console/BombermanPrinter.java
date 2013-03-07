package com.codenjoy.bomberman.console;

import com.codenjoy.bomberman.model.Board;
import com.codenjoy.bomberman.model.Bomb;
import com.codenjoy.bomberman.model.Bomberman;

import java.lang.Override;import java.lang.String;
import java.util.List;

public class BombermanPrinter implements Printer {

    public final static char BOMBERMAN = '☺';
    public final static char DEAD_BOMBERMAN = '☻';
    public final static char BOOM = '҉';
    public final static String BOMBS = "012345";
    public final static char WALL = '☼';
    public static final char SPACE = ' ';

    int size;
	private char[][] monitor;

    @Override
	public String print(Board board) {
		this.size = board.size();

		clean();
        printBombs(board.getBombs());
		printBomberman(board.getBomberman());

		return asString();
	}

    void printBombs(List<Bomb> bombs) {
        for (Bomb bomb : bombs) {
            monitor[bomb.getX()][bomb.getY()] = BOMBS.charAt(bomb.getTimer());
        }
    }

    void clean() {
		monitor = new char[size][size];

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				monitor[x][y] = SPACE;
			}
		}
	}

	void printBomberman(Bomberman bomberman) {
        monitor[bomberman.getX()][bomberman.getY()] = BOMBERMAN;
	}

	String asString() {
		String result = "";
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				result += monitor[x][y];
			}
			result += "\n";
		}
		return result;
	}

}
