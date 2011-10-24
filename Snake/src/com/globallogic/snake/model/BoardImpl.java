package com.globallogic.snake.model;

import com.globallogic.snake.model.artifacts.Apple;
import com.globallogic.snake.model.artifacts.ArtifactGenerator;
import com.globallogic.snake.model.artifacts.Element;
import com.globallogic.snake.model.artifacts.EmptySpace;
import com.globallogic.snake.model.artifacts.Point;
import com.globallogic.snake.model.artifacts.Stone;
import com.globallogic.snake.model.artifacts.Wall;

public class BoardImpl implements Board {

	private Snake snake;
	private Stone stone;
	private int size;
	private Apple apple;

	public BoardImpl(ArtifactGenerator generator, int size) {
		this.size = size;
		if (size%2 == 0) {
			throw new IllegalArgumentException();
		}
		
		int position = (size - 1)/2; 		
		snake = new Snake(position, position);
				
		stone = generator.generateStone(snake, size);
		 		
		makeApplesAppearChain(generator);
	}

	/**
	 * метод настраивает систему так, что при схедании одного яблока автоматически появляется другое. 
	 * @param generator генератор, генерирующий яблоки

	 */
	private void makeApplesAppearChain(ArtifactGenerator generator) {
		apple = generator.generateApple(snake, stone, size);		
		apple.onEat(new OnAppleEat(generator));
	}
	
	class OnAppleEat implements Runnable { // ну замудрил... TODO
		
		private ArtifactGenerator generator;

		public OnAppleEat(ArtifactGenerator generator) {
			this.generator = generator;
		}
		
		@Override
		public void run() {
			makeApplesAppearChain(generator);
		}
	}

	public Snake getSnake() {
		return snake; 
	}

	public Stone getStone() {  		
		return stone;				
	}

	public void tact() {
		snake.checkAlive();		
		snake.walk(this);
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
