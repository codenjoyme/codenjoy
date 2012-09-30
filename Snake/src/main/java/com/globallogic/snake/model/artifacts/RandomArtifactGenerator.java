package com.globallogic.snake.model.artifacts;

import java.util.Random;

import com.globallogic.snake.model.Direction;
import com.globallogic.snake.model.Snake;

public class RandomArtifactGenerator implements ArtifactGenerator {

	@Override
	public Stone generateStone(Snake snake, int boardSize) {
		int x = 0;
		int y = 0;
		do {
			y = random(boardSize);
			x = random(boardSize);
		} while (snake.itsMe(new Point(x, y)) ||
                ((snake.getX() + 1) <= x && x <= boardSize && y == snake.getY() && snake.getDirection().equals(Direction.RIGHT)) ||
                ((snake.getY() + 1) <= y && y <= boardSize && x == snake.getX() && snake.getDirection().equals(Direction.DOWN)));

		return new Stone(x, y);
	}
	
	private int random(int boardSize) {
		return new Random().nextInt(boardSize);
	}

	@Override
	public Apple generateApple(Snake snake, Stone stone, int boardSize) {
		int x = 0;
		int y = 0;
		do {
			x = random(boardSize);
			y = random(boardSize);
		} while (snake.itsMe(new Point(x, y)) || stone.itsMe(new Point(x, y))); 
		
		return new Apple(x, y);
	}

}
