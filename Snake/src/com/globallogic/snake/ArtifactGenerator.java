package com.globallogic.snake;

public interface ArtifactGenerator {

	Apple generateApple(Snake snake, int boardSize);
	
	Stone generateStone(Snake snake, int boardSize);
}
