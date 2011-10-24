package com.globallogic.snake.artifacts;

import com.globallogic.snake.Snake;

public interface Element {

	void modify(Snake snake);

	boolean itsMe(Point point);
}

