package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Printer;
import static com.codenjoy.dojo.bomberman.model.PlotColor.*;

import java.util.List;

public class BombermanPrinter implements Printer {

    private IBoard board;
	private PlotColor[][] monitor;
    private int size;

    public BombermanPrinter(IBoard board) {
        this.board = board;
    }

    @Override
	public String print() {
        size = board.size();
        clean();
		printBomberman(board.getBomberman());
        printOtherBombermans(board.getBombermans());
        printBombs(board.getBombs());
        printWall(board.getWalls());
        printBlasts(board.getBlasts());

		return asString();
	}

    private void printOtherBombermans(List<Bomberman> bombermans) {
        for (Bomberman bomberman : bombermans) {
            if (bomberman != board.getBomberman()) {
                drawAt(bomberman, OTHER_BOMBERMAN);
            }
        }
    }

    private void printBlasts(List<IPoint> blasts) {
        for (IPoint blast : blasts) {
            if (getAt(blast).isBomb()) {
                continue;
            } else if (getAt(blast).isOtherBomberman()) {
                drawAt(blast, OTHER_DEAD_BOMBERMAN);
            } else if (getAt(blast).isBomberman()) {
                drawAt(blast, DEAD_BOMBERMAN);
            } else if (getAt(blast).isMeatChopper()) {
                drawAt(blast, DEAD_MEAT_CHOPPER);
            } else if (getAt(blast).isDestroyWall()) {
                drawAt(blast, DESTROYED_WALL);
            } else {
                drawAt(blast, BOOM);
            }
        }
    }

    void printBombs(List<Bomb> bombs) {
        for (Bomb bomb : bombs) {
            if (getAt(bomb).isOtherBomberman()) {
                drawAt(bomb, OTHER_BOMB_BOMBERMAN);
            } else if (getAt(bomb).isBomberman()) {
                drawAt(bomb, BOMB_BOMBERMAN);
            } else {
                drawAt(bomb, PlotColor.getBomb(bomb.getTimer()));
            }
        }
    }

    void clean() {
		monitor = new PlotColor[size][size];

		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				monitor[x][y] = EMPTY;
			}
		}
	}

	void printBomberman(Bomberman bomberman) {
        drawAt(bomberman, BOMBERMAN);
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
        for (Wall element : walls) {
            if (element instanceof DestroyWall) {
                drawAt(element, DESTROY_WALL);
            } else if (element instanceof MeatChopper) {
                if (getAt(element).isOtherBomberman()) {
                    drawAt(element, OTHER_DEAD_BOMBERMAN);
                } else if (getAt(element).isBomberman()) {
                    drawAt(element, DEAD_BOMBERMAN);
                } else {
                    drawAt(element, MEAT_CHOPPER);
                }
            } else {
                drawAt(element, WALL);
            }
        }
    }

    private PlotColor getAt(IPoint pt) {
        return monitor[pt.getX()][pt.getY()];
    }

    private void drawAt(IPoint pt, PlotColor color) {
        monitor[pt.getX()][pt.getY()] = color;
    }

    public BombermanPrinter printSmth(Iterable<? extends IPoint> points, Class who, PlotColor color) {
        for (IPoint point : points) {
            if (point.getX() < 0 || point.getY() < 0 || point.getX() >= size || point.getY() >= size) {
                continue;
            }
            if (point.getClass().equals(who)) {
                drawAt(point, color);
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
