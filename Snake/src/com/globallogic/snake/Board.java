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
		Element element = snake.move(this);
		
		if (element instanceof Apple) { // TODO немного некрасиво, надо придумать что-то
			apple = generator.generateApple(snake, size);
		}
	}

	Element getAt(int x, int y) {
		if (stone.itsMe(x, y)) {
			return stone; 
		}
		
		if (apple.itsMe(x, y)) {
			return apple;
		}
		
		if (snake.itsMe(x, y)) {
			return snake;
		}
		
		if (x < 0 || y < 0 || y >= size || x >= size) {
			return new Wall();
		}
		
		return new EmptySpace();
	}

	public boolean isGameOver() {
		return !snake.isAlive();
	}

	public Apple getApple() {
		return apple;
	}

}
