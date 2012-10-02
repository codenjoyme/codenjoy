package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public class ConstantArtifactGenerator implements ArtifactGenerator {

	@Override
	public Stone generateStone(Snake snake, Apple apple, int boardSize) {
		return new Stone(0, 0);
	}

    @Override
	public Apple generateApple(Snake snake, Stone stone, int boardSize) {
		return new Apple(1, 1);
	}

}
