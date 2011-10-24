package com.globallogic.snake;

import com.globallogic.snake.artifacts.Apple;
import com.globallogic.snake.artifacts.Point;
import com.globallogic.snake.artifacts.Stone;

public class SnakePrinterImpl implements SnakePrinter {

	int size;
	private char[][] monitor;  
	
	public String print(Board board) {
		this.size = board.getSize();
 
		clean();
		printWalls();
		printApple(board.getApple());
		printStone(board.getStone());
		printSnake(board.getSnake());
		
		return asString();		
	}
	
	void clean() {
		monitor = new char[size + 2][size + 2];
		
		for (int y = 0; y < size + 2; y++) {
			for (int x = 0; x < size + 2; x++) {			
				monitor[x][y] = ' ';
			}
		}
	}

	void printWalls() {
		for (int x = 0; x < size + 2; x++) {
			monitor[x][0] = '*';
			monitor[x][size + 1] = '*';
		}
		
		for (int y = 0; y < size + 2; y++) {
			monitor[0][y] = '*';
			monitor[size + 1][y] = '*';
		}
	}

	void printSnake(Snake snake) {
		for (Point element : snake) {
			monitor[element.getX() + 1][element.getY() + 1] = '0';
		}
		
		printHead(snake);		
	}

	private void printHead(Snake snake) {
		Point head = snake.getHead();
		monitor[head.getX() + 1][head.getY() + 1] = '#';
	}

	void printStone(Stone stone) {
		monitor[stone.getX() + 1][stone.getY() + 1] = 'X';		
	}

	void printApple(Apple apple) {
		monitor[apple.getX() + 1][apple.getY() + 1] = '@';		
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
