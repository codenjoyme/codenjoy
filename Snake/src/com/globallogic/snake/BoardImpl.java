package com.globallogic.snake;

import com.globallogic.snake.artifacts.Apple;
import com.globallogic.snake.artifacts.ArtifactGenerator;
import com.globallogic.snake.artifacts.Element;
import com.globallogic.snake.artifacts.EmptySpace;
import com.globallogic.snake.artifacts.Point;
import com.globallogic.snake.artifacts.Stone;
import com.globallogic.snake.artifacts.Wall;

public class BoardImpl implements Board {

	private Snake snake;
	private Stone stone;
	private int size;
	private Apple apple;
	private ArtifactGenerator generator;

	public BoardImpl(ArtifactGenerator generator, int size) {
		this.size = size;
		if (size%2 == 0) {
			throw new IllegalArgumentException();
		}
		
		int position = (size - 1)/2; 		
		snake = new Snake(position, position);
				
		this.generator = generator;
		stone = generator.generateStone(snake, size);
		apple = generator.generateApple(snake, stone, size);
	}

	public Snake getSnake() {
		return snake; 
	}

	public Stone getStone() {  		
		return stone;				
	}

	public void tact() {
		snake.checkAlive();		
		Element element = snake.move(this);
		
		if (element instanceof Apple) { // TODO немного некрасиво, надо придумать что-то
			apple = generator.generateApple(snake, stone, size);
		}
	}

	Element getAt(Point point) {
		if (stone.itsMe(point)) {
			return stone; 
		}
		
		if (apple.itsMe(point)) {
			return apple;
		}
		
		// получается я свой хвост немогу укусить, потому как я за ним двинусь а он отползет
		// вроде логично
		if (snake.itsMyTail(point)) { 
			return new EmptySpace(point);
		}		
		
		if (snake.itsMyBody(point)) {
			return snake;
		}
		
		if (isWall(point)) {
			return new Wall(point);
		}
		
		return new EmptySpace(point);
	}

	private boolean isWall(Point point) {
		return point.getX() < 0 || point.getY() < 0 || point.getY() >= size || point.getX() >= size;
	}

	public boolean isGameOver() {
		return !snake.isAlive();
	}

	public Apple getApple() {
		return apple;
	}

	public int getSize() {
		return size;
	}

}
