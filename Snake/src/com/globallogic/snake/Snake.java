package com.globallogic.snake;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class Snake implements Element {

	private int x;
	private int y;
	private Direction direction; 
	private boolean alive;
	private int length;
	private Deque<Point> elements;
	private boolean grow;

	public Snake(int x, int y) {	
		elements = new LinkedList<Point>();
		elements.addFirst(new Point(x, y));
		elements.addFirst(new Point(x - 1, y));
		
		grow = false;
		
		this.x = x;
		this.y = y;
		
		length = 2;
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
		return length;
	}

	public Direction getDirection() {
		return direction;
	}

	// методы turn - это методы, который может вызывать игрок, 
	// а вот move  - вызывает сама доска (игра) когда проходит один такт, 
	// именно по этому мы их деалем невидимыми извне пакета  
	void moveRight() { 
		x++;
		refresh();
	}

	private void refresh() {
		elements.addLast(new Point(x, y));
		if (grow) {
			grow = false;
			return;
		}
		elements.removeFirst();
	}

	public void turnDown() {
		checkAlive();
		if (direction.equals(Direction.UP)) {
			killMe();
		}
		direction = Direction.DOWN;
	}

	void moveDown() {
		y++;
		refresh();
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
		refresh();
	}

	void checkAlive() {
		if (!isAlive()) {
			throw new IllegalStateException("Game over!");
		}
	}

	public void turnLeft() {
		checkAlive();
		if (direction.equals(Direction.RIGHT)) {
			killMe();
		}
		direction = Direction.LEFT;		
	}

	void killMe() {
		alive = false;
	}

	public boolean isAlive() {
		return alive;
	}

	public void turnRight() {
		checkAlive();
		if (direction.equals(Direction.LEFT)) {
			killMe();
		}
		direction = Direction.RIGHT;
	}

	void moveLeft() {	
		x--;
		refresh();
	}	

	void grow() {
		length++;
		grow = true;
	}

	public boolean itsMe(int x2, int y2) {		
		if (x == x2 && y == y2) {
			return false; 
		}
		
		for (Point element : elements) {
			if (element.itsMe(x2, y2)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void modify(Snake snake) {
		killMe();
	}

	void eat(Element element) {
		element.modify(this);
	}

	Element move(Board board) {	
		int x2 = x;
		int y2 = y;
		if (Direction.RIGHT.equals(direction)) {
			x2++;
		} else if (Direction.UP.equals(direction)) {
			y2--;
		} else if (Direction.DOWN.equals(direction))  {					
			y2++;
		} else {			
			x2--;
		}		
						
		Element element = board.getAt(x2, y2);
		eat(element);
		
		x = x2;
		y = y2;
		refresh();
		System.out.println(elements);
		
		return element;
	}

}
