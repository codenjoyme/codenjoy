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

	Element getAt(Point point) {
		if (stone.itsMe(point)) {
			return stone; 
		}
		
		if (apple.itsMe(point)) {
			return apple;
		}
		
		// получается я свой хваст немогу укусить, потому как я за ним двинусь а он отползет
		// вроде логично
		if (snake.itsMyTail(point)) { 
			return new EmptySpace();
		}		
		
		if (snake.itsMe(point)) {
			return snake;
		}
		
		if (point.x < 0 || point.y < 0 || point.y >= size || point.x >= size) {
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
