package com.globallogic.snake.model.artifacts;

import com.globallogic.snake.model.Snake;

public interface Element {

	void modify(Snake snake);

	boolean itsMe(Point point);
}

