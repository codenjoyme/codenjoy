package com.globallogic.snake;



public class Board {

	private Snake snake;
	private Stone stone;
	static StoneGenerator stoneGenerator = new RandomStoneGenerator();  

	public Board(int size) {
		if (size %2 == 0) {
			throw new IllegalArgumentException();
		}
		
		int position = size/2 + 1; 		
		snake = new Snake(position, position);
		
		stone = stoneGenerator.generateStone(snake, size);
	}

	public Snake getSnake() {
		return snake; 
	}

	public Stone getStone() {  		
		return stone;				
	}

	public void tact() {
		if (Direction.RIGHT.equals(snake.getDirection())) {
			snake.moveRight();
		} else if (Direction.UP.equals(snake.getDirection())) {
			snake.moveUp();
		} else if (Direction.DOWN.equals(snake.getDirection()))  {					
			snake.moveDown();
		} else {
			snake.moveLeft();
		}
	}

	public boolean isGameOver() {
		return !snake.isAlive();
	}

}
