package com.globallogic.snake;

public class Board {

	private Snake snake;

	public Board(int size) {
		if (size %2 == 0) {
			throw new IllegalArgumentException();
		}
		int position = size/2 + 1; 		
		this.snake = new Snake(position, position);
	}

	public Snake getSnake() {
		return snake; 
	}

}
