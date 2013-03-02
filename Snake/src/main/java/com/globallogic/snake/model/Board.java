package com.globallogic.snake.model;

import com.globallogic.snake.model.artifacts.Apple;
import com.globallogic.snake.model.artifacts.Element;
import com.globallogic.snake.model.artifacts.Point;
import com.globallogic.snake.model.artifacts.Stone;

public interface Board {

	Snake getSnake();

	Stone getStone();

    Walls getWalls();

	void tact();

	boolean isGameOver();

	Apple getApple();

	int getSize();

    Element getAt(Point place);

    void newGame();

    int getMaxLength();
}