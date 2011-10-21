package com.globallogic.snake;

public class Snake {

	private int x;
	private int y;
	private Direction direction; 
	private boolean alive;

	public Snake(int x, int y) {
		this.x = x;
		this.y = y;
		direction = Direction.RIGHT;
		alive = true;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getLength() {
		return 2;
	}

	public Direction getDirection() {
		return direction;
	}

	// методы turn - это методы, который может вызывать игрок, 
	// а вот move  - вызывает сама доска (игра) когда проходит один такт, 
	// именно по этому мы их деалем невидимыми извне пакета  
	void moveRight() { 
		x++;
	}

	public void turnDown() {
		checkAlive();
		direction = Direction.DOWN;
	}

	void moveDown() {
		y++;
	}

	public void turnUp() {
		checkAlive();
		if (direction.equals(Direction.DOWN)) {
			killMe();
		}
		direction = Direction.UP;		
	}

	public void moveUp() {
		y--;
	}

	private void checkAlive() {
		if (!isAlive()) {
			throw new IllegalStateException("Game over!");
		}
	}

	public void turnLeft() {
		checkAlive();
		killMe();
	}

	private void killMe() {
		alive = false;
	}

	public boolean isAlive() {
		return alive;
	}

	public void turnRight() {
		checkAlive();
		direction = Direction.RIGHT;
	}

}
