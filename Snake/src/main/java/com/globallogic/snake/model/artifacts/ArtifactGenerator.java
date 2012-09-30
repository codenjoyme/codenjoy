package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public interface ArtifactGenerator {

	Apple generateApple(Snake snake, Stone stone, int boardSize);
	
	Stone generateStone(Snake snake, int boardSize);
}
