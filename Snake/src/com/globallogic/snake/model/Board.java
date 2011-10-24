package com.globallogic.snake.model;

import com.globallogic.snake.model.artifacts.Apple;
import com.globallogic.snake.model.artifacts.Stone;

public interface Board {

	Snake getSnake();

	Stone getStone();

	void tact();

	boolean isGameOver();

	Apple getApple();

	int getSize();

}