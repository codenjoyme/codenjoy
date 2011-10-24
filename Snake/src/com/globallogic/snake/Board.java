package com.globallogic.snake;

public interface Board {

	Snake getSnake();

	Stone getStone();

	void tact();

	boolean isGameOver();

	Apple getApple();

	int getSize();

}