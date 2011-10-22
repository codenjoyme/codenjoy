package com.globallogic.snake;

public interface ArtifactGenerator {

	Apple generateApple(Snake snake, Stone stone, int boardSize);
	
	Stone generateStone(Snake snake, int boardSize);
}
