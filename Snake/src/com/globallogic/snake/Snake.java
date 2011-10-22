package com.globallogic.snake;

import java.util.Deque;
import java.util.LinkedList;

public class Snake implements Element {

	private Deque<Point> elements;
	private Direction direction; 
	private boolean alive; 
	private boolean grow;

	public Snake(int x, int y) {	
		elements = new LinkedList<Point>();
		elements.addFirst(new Point(x, y));
		elements.addFirst(new Point(x - 1, y));
		
		grow = false;
				
		direction = Direction.RIGHT;
		alive = true;
	}
	
	public int getX() {
		return getHead().x;
	}

	public int getY() {
		return getHead().y;
	}

	public int getLength() {
		return elements.size();
	}

	public Direction getDirection() {
		return direction;
	}

	// методы turn - это методы, который может вызывать игрок, 
	// а вот move  - вызывает сама доска (игра) когда проходит один такт, 
	// именно по этому мы turn деалем невидимыми извне пакета  
	void moveRight() { 
		move(getX() + 1, getY());
	}
	
	void moveUp() {
		move(getX(), getY() - 1);
	}
	
	void moveLeft() {	
		move(getX() - 1, getY());
	}
	
	void moveDown() {
		move(getX(), getY() + 1);
	}

	private void move(int x, int y) {
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

	public void turnUp() {
		checkAlive();
		if (direction.equals(Direction.DOWN)) {
			killMe(); 
		}
		direction = Direction.UP;		
	}

	public void turnLeft() {
		checkAlive();
		if (direction.equals(Direction.RIGHT)) {
			killMe();
		}
		direction = Direction.LEFT;		
	}
	
	public void turnRight() {
		checkAlive();
		if (direction.equals(Direction.LEFT)) {
			killMe();
		}
		direction = Direction.RIGHT;
	}
	
	void checkAlive() {
		if (!isAlive()) {
			throw new IllegalStateException("Game over!");
		}
	}
	
	public boolean isAlive() {
		return alive;
	}

	void killMe() {
		alive = false;
	}

	void grow() {
		grow = true;
	}

	public boolean itsMyHead(Point point) {
		return (getHead().itsMe(point));
	}
	
	public boolean itsMyBody(Point point) {		
		if (itsMyHead(point)) {
			return false;
		}
		
		for (Point element : elements) {
			if (element.itsMe(point)) {
				return true;
			}
		}
		return false;
	}

	private Point getHead() {
		return elements.getLast();
	}

	@Override
	public void modify(Snake snake) {
		killMe();
	}

	void eat(Element element) {
		element.modify(this);
	}

	Element move(Board board) {	
		Point newPoint = whereToMove();		
						
		Element element = board.getAt(newPoint);
		eat(element);		
		move(newPoint.x, newPoint.y);
		
		return element;
	}

	private Point whereToMove() {
		Point result = new Point(getX(), getY());
		if (Direction.RIGHT.equals(direction)) {
			result.x++;
		} else if (Direction.UP.equals(direction)) {
			result.y--;
		} else if (Direction.DOWN.equals(direction))  {					
			result.y++;
		} else {			
			result.x--;
		}
		return result;
	}

	public boolean itsMyTail(Point point) {
		return elements.getFirst().itsMe(point);
	}

}
