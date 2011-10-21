package com.globallogic.snake;

import java.util.Random;


public class Board {

	private Snake snake;
	private int size;
	private static int i=1;

	public Board(int size) {
		this.size = size; 
		if (size %2 == 0) {
			throw new IllegalArgumentException();
		}
		int position = size/2 + 1; 		
		this.snake = new Snake(position, position);
	}

	public Snake getSnake() {
		return snake; 
	}

	public Stone getStone() {
		int x = new Random().nextInt(size + 1);
		int y = new Random().nextInt(size + 1);		
		return new Stone(x, y);				
	}

}
