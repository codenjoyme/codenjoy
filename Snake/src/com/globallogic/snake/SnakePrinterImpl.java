package com.globallogic.snake;

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
			monitor[element.x + 1][element.y + 1] = '0';
		}
		
		printHead(snake);		
	}

	private void printHead(Snake snake) {
		Point head = snake.getHead();
		monitor[head.x + 1][head.y + 1] = '#';
	}

	void printStone(Stone stone) {
		monitor[stone.x + 1][stone.y + 1] = 'X';		
	}

	void printApple(Apple apple) {
		monitor[apple.x + 1][apple.y + 1] = '@';		
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
