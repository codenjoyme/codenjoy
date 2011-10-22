package com.globallogic.snake;

public interface Element {

	void modify(Snake snake);

	boolean itsMe(Point point);
}

