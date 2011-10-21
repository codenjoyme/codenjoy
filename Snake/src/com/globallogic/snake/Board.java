package com.globallogic.snake;




public class Board {

	private Snake snake;
	private Stone stone;
	private int size;
	private Apple apple;
	private ArtifactGenerator generator = new RandomArtifactGenerator();

	public Board(int size) {
		this.size = size;
		if (size%2 == 0) {
			throw new IllegalArgumentException();
		}
		
		int position = (size - 1)/2; 		
		snake = new Snake(position, position);
		
		// stone = generator.generateStone(snake, size);
		// stone = generator.generateApple(snake, size); // TODO test this line
	}
	
	public Board(ArtifactGenerator generator, int size) {
		this(size);
		this.generator = generator;
		stone = generator.generateStone(snake, size);
		apple = generator.generateApple(snake, size);
	}

	public Snake getSnake() {
		return snake; 
	}

	public Stone getStone() {  		
		return stone;				
	}

	public void tact() {
		snake.checkAlive();
		if (Direction.RIGHT.equals(snake.getDirection())) {
			snake.moveRight();
		} else if (Direction.UP.equals(snake.getDirection())) {
			snake.moveUp();
		} else if (Direction.DOWN.equals(snake.getDirection()))  {					
			snake.moveDown();
		} else {			
			snake.moveLeft();
		} 
		if (stone.getX() == snake.getX() && stone.getY() == snake.getY()) {
			snake.killMe();
		} 
		if (snake.getX() < 0 || snake.getY() >= size || snake.getY() < 0 || snake.getX() >= size) {
			snake.killMe();
		}
		if (snake.getX() == apple.getX() && snake.getY() == apple.getY()) {
			apple = generator.generateApple(snake, size); 
		}
	}

	public boolean isGameOver() {
		return !snake.isAlive();
	}

	public Apple getApple() {
		return apple;
	}

}
