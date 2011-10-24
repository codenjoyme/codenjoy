package com.globallogic.snake;

import com.globallogic.snake.artifacts.Apple;
import com.globallogic.snake.artifacts.Stone;

public interface Board {

	Snake getSnake();

	Stone getStone();

	void tact();

	boolean isGameOver();

	Apple getApple();

	int getSize();

}