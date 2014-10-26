package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.snake.model.artifacts.Apple;
import com.codenjoy.dojo.snake.model.artifacts.Element;
import com.codenjoy.dojo.snake.model.artifacts.Stone;

public interface Field extends Game {

	Stone getStone();

    Walls getWalls();

	Apple getApple();

    Element getAt(Point place);

    int getSize();

    Hero getSnake();

}