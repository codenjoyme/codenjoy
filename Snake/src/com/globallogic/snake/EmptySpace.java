package com.globallogic.snake;

public class EmptySpace extends Point implements Element {

	public EmptySpace(Point point) {
		super(point);
	}

	@Override
	public void modify(Snake snake) {
		// do nothing		
	}

}
