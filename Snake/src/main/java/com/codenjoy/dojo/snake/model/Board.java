package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.Element;
import com.codenjoy.dojo.snake.model.artifacts.Point;
import com.codenjoy.dojo.snake.model.artifacts.Stone;

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