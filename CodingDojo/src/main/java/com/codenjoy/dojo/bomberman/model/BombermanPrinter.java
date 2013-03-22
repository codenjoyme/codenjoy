package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Printer;

import java.util.List;

public class BombermanPrinter implements Printer {

    public final static char BOMBERMAN = '☺';
    public final static char BOMB_BOMBERMAN = '☻';
    public final static char DEAD_BOMBERMAN = 'Ѡ';
    public final static char BOOM = '҉';
    public final static String BOMBS = "012345";
    public final static char WALL = '☼';
    public final static char DESTROY_WALL = '#';
    public final static char DESTROYED_WALL = 'H';
    public final static char MEAT_CHOPPER = '&';
    public final static char DEAD_MEAT_CHOPPER = 'x';
    public static final char SPACE = ' ';

    private Board board;
	private char[][] monitor;
    private int size;

    public BombermanPrinter(Board board) {
        this.board = board;        
    }

    @Override
	public String print() {
        size = board.size();
        clean();
		printBomberman(board.getBomberman());
        printBombs(board.getBombs());
        printWall(board.getWalls());
        printBlasts(board.getBlasts());

		return asString();
	}

    private void printBlasts(List<Point> blasts) {
        for (Point blast : blasts) {
            char c = monitor[blast.getX()][blast.getY()];
            if (BOMBS.indexOf(c, 0) != -1) {
                continue;
            } else if (c == BOMBERMAN || c == BOMB_BOMBERMAN || c == DEAD_BOMBERMAN) {
                monitor[blast.getX()][blast.getY()] = DEAD_BOMBERMAN;
            } else if (c == MEAT_CHOPPER) {
                monitor[blast.getX()][blast.getY()] = DEAD_MEAT_CHOPPER;
            } else if (c == DESTROY_WALL) {
                monitor[blast.getX()][blast.getY()] = DESTROYED_WALL;
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
            if (wall instanceof DestroyWall) {
                monitor[wall.getX()][wall.getY()] = DESTROY_WALL;
            } else if (wall instanceof MeatChopper) {
                char c = monitor[wall.getX()][wall.getY()];
                if (c == BOMBERMAN) {
                    monitor[wall.getX()][wall.getY()] = DEAD_BOMBERMAN;
                } else {
                    monitor[wall.getX()][wall.getY()] = MEAT_CHOPPER;
                }
            } else {
                monitor[wall.getX()][wall.getY()] = WALL;
            }
        }
    }

    public BombermanPrinter printSmth(Iterable<? extends Point> points, Class who, char c) {
        for (Point point : points) {
            if (point.getX() < 0 || point.getY() < 0 || point.getX() >= size || point.getY() >= size) {
                continue;
            }
            if (point.getClass().equals(who)) {
                monitor[point.getX()][point .getY()] = c;
            }
        }
        return this;
    }

    public static BombermanPrinter get(int size) {
        BombermanPrinter printer = new BombermanPrinter(null);
        printer.size = size;
        printer.clean();
        return printer;
    }
}
