package com.globallogic.snake;

import java.util.Random;


public class Board {

	private Snake snake;
	private int size;
	private Stone stone;	

	public Board(int size) {
		this.size = size; 
		if (size %2 == 0) {
			throw new IllegalArgumentException();
		}
		
		int position = size/2 + 1; 		
		snake = new Snake(position, position);
		
		stone = generateStone();
	}

	public Snake getSnake() {
		return snake; 
	}

	public Stone getStone() {  		
		return stone;				
	}

	private Stone generateStone() {
		int x = 0;
		int y = 0;
		do {
			y = random();
			x = random();
		} while ((snake.getX() - 1) <= x && x <= size && y == snake.getY());

		return new Stone(x, y);
	}

	private int random() {
		return new Random().nextInt(size + 1);
	}

	public void tact() {
		snake.moveLeft();
	}

}
