package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;
import com.globallogic.snake.model.Walls;

public interface ArtifactGenerator {

	Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize);
	
	Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize);
}
