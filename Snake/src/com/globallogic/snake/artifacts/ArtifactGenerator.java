package com.globallogic.snake.artifacts;

import com.globallogic.snake.Snake;

public interface ArtifactGenerator {

	Apple generateApple(Snake snake, Stone stone, int boardSize);
	
	Stone generateStone(Snake snake, int boardSize);
}
