package com.globallogic.snake;

import java.util.Random;

public class RandomStoneGenerator implements StoneGenerator {

	public Stone generateStone(Snake snake, int boardSize) {
		int x = 0;
		int y = 0;
		do {
			y = random(boardSize);
			x = random(boardSize);
		} while ((snake.getX() - 1) <= x && x <= boardSize && y == snake.getY());

		return new Stone(x, y);
	}
	
	private int random(int boardSize) {
		return new Random().nextInt(boardSize + 1);
	}
	
}
