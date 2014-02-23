package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.snake.model.Snake;
import com.codenjoy.dojo.snake.model.Walls;

public interface ArtifactGenerator {

	Apple generateApple(Snake snake, Apple apple, Stone stone, Walls walls, int boardSize);
	
	Stone generateStone(Snake snake, Apple apple, Walls walls, int boardSize);
}
