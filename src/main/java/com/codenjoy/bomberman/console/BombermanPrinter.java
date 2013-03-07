package com.codenjoy.bomberman.console;

import com.codenjoy.bomberman.model.*;

import java.util.Iterator;
import java.util.List;

public class BombermanPrinter implements Printer {

    public final static char BOMBERMAN = '☺';
    public final static char BOMB_BOMBERMAN = '☻';
    public final static char DEAD_BOMBERMAN = 'Ѡ';
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
		printBomberman(board.getBomberman());
        printBombs(board.getBombs());
        printBlasts(board.getBlasts());
        printWall(board.getWalls());

		return asString();
	}

    private void printBlasts(List<Blast> blasts) {
        for (Blast blast : blasts) {
            char c = monitor[blast.getX()][blast.getY()];
            if (BOMBS.indexOf(c, 0) != -1) {
                continue;
            } else if (c == BOMBERMAN || c == BOMB_BOMBERMAN) {
                monitor[blast.getX()][blast.getY()] = DEAD_BOMBERMAN;
            } else {
                monitor[blast.getX()][blast.getY()] = BOOM;
            }
        }
    }

    void printBombs(List<Bomb> bombs) {
        for (Bomb bomb : bombs) {
            char c = monitor[bomb.getX()][bomb.getY()];
            if (c == BOMBERMAN) {
                monitor[bomb.getX()][bomb.getY()] = BOMB_BOMBERMAN;
            } else {
                monitor[bomb.getX()][bomb.getY()] = BOMBS.charAt(bomb.getTimer());
            }
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

	public String asString() {
		String result = "";
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				result += monitor[x][y];
			}
			result += "\n";
		}
		return result;
	}

    void printWall(Walls walls) {
        for (Wall wall : walls) {
            monitor[wall.getX()][wall.getY()] = WALL;
        }
    }

    public BombermanPrinter printSmth(int size, Iterable<Point> points, char c) {
        this.size = size;
        clean();

        for (Point point : points) {
            monitor[point .getX()][point .getY()] = c;
        }
        return this;
    }
}
