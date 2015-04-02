package com.codenjoy.dojo.snake.model.artifacts;

import com.codenjoy.dojo.snake.model.Hero;
import com.codenjoy.dojo.snake.model.Walls;

public interface ArtifactGenerator {

	Apple generateApple(Hero snake, Apple apple, Stone stone, Walls walls, int boardSize);
	
	Stone generateStone(Hero snake, Apple apple, Walls walls, int boardSize);
}
