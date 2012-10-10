package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;
import com.globallogic.snake.model.Walls;

public class ConstantArtifactGenerator implements ArtifactGenerator {

	@Override
	public Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize) {
		return new Stone(0, 0);
	}

    @Override
	public Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize) {
		return new Apple(1, 1);
	}

}
