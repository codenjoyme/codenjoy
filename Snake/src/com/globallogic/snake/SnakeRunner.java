package com.globallogic.snake;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SnakeRunner {

	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws IOException {
		int size = 11;
		Board board = new Board(new RandomArtifactGenerator(), size);
		SnakePrinter printer = new SnakePrinter(size);
		Snake snake = board.getSnake();
		
		while (!board.isGameOver()) {		
			print(board, printer);
			
			String line = reader.readLine();
			if (line.length() != 0) {
				int ch = line.charAt(0);			
				
				if (ch == 115) {
					snake.turnDown();
				} else if (ch == 97) {
					snake.turnLeft();
				} else if (ch == 100) {
					snake.turnRight();
				} else if (ch == 119) {
					snake.turnUp();
				} 				
			}
			board.tact();						
		}
		print(board, printer);
		System.out.println("Game over!");
	}

	private static void print(Board board, SnakePrinter printer) {
		System.out.println(printer.print(board.getSnake(), board.getStone(), board.getApple()));
		System.out.println();
		System.out.println();
	}

}
