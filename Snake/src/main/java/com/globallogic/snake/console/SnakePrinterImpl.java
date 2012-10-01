package com.globallogic.snake.console;

import com.globallogic.snake.model.Walls;
import com.globallogic.snake.model.artifacts.Apple;
import com.globallogic.snake.model.artifacts.Point;
import com.globallogic.snake.model.artifacts.Stone;
import com.globallogic.snake.model.Board;
import com.globallogic.snake.model.Snake;

public class SnakePrinterImpl implements SnakePrinter {

    public static final char WALL = '*';
    public static final char BODY = '0';
    public static final char SPACE = ' ';
    public static final char STONE = 'X';
    public static final char HEAD = '#';
    public static final char APPLE = '@';

    int size;
	private char[][] monitor;

	public String print(Board board) {
		this.size = board.getSize();

		clean();
		printWalls(board.getWalls());
		printApple(board.getApple());
		printStone(board.getStone());
		printSnake(board.getSnake());

		return asString();
	}

	void clean() {
		monitor = new char[size + 2][size + 2];

		for (int y = 0; y < size + 2; y++) {
			for (int x = 0; x < size + 2; x++) {
				monitor[x][y] = SPACE;
			}
		}
	}

	void printWalls(Walls walls) {
		for (int x = 0; x < size + 2; x++) {
			monitor[x][0] = WALL;
			monitor[x][size + 1] = WALL;
		}

		for (int y = 0; y < size + 2; y++) {
			monitor[0][y] = WALL;
			monitor[size + 1][y] = WALL;
		}

        if (walls != null) {
            for (Point element : walls) {
                monitor[element.getX() + 1][element.getY() + 1] = WALL;
            }
        }
	}

	void printSnake(Snake snake) {
		for (Point element : snake) {
			monitor[element.getX() + 1][element.getY() + 1] = BODY;
		}

		printHead(snake);
	}

	private void printHead(Snake snake) {
		Point head = snake.getHead();
        monitor[head.getX() + 1][head.getY() + 1] = HEAD;
	}

	void printStone(Stone stone) {
        if (stone != null){
            monitor[stone.getX() + 1][stone.getY() + 1] = STONE;
        }
	}

	void printApple(Apple apple) {
        if (apple != null){
		    monitor[apple.getX() + 1][apple.getY() + 1] = APPLE;
        }
	}

	String asString() {
		String result = "";
		for (int y = 0; y < size + 2; y++) {
			for (int x = 0; x < size + 2; x++) {			
				result += monitor[x][y];
			}
			result += "\n";
		}
		return result;
	}

}
