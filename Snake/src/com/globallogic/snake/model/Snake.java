package com.globallogic.snake.model;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.globallogic.snake.model.artifacts.Element;
import com.globallogic.snake.model.artifacts.Point;

public class Snake implements Element, Iterable<Point> {

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
		return getHead().getX();
	}

	public int getY() {
		return getHead().getY();
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

	public void move(int x, int y) {
		elements.addLast(new Point(x, y));
		if (grow) {
			grow = false;
			return;
		}
		elements.removeFirst();
	}

	public void turnDown() {
		checkAlive();
		direction = Direction.DOWN;
	}

	public void turnUp() {
		checkAlive();
		direction = Direction.UP;		
	}

	public void turnLeft() {
		checkAlive();
		direction = Direction.LEFT;		
	}
	
	public void turnRight() {
		checkAlive();
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

	public void killMe() {
		alive = false;
	}

	public void grow() {
		grow = true;
	}

	public boolean itsMyHead(Point point) {
		return (getHead().itsMe(point));
	}
	
	public boolean itsMe(Point point) {
		return itsMyBody(point) || itsMyHead(point);
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

	public Point getHead() {
		return elements.getLast();
	}

	@Override
	public void affect(Snake snake) {
		killMe();
	}

	void walk(BoardImpl board) {	
		Point place = whereToMove();								
		board.getAt(place).affect(this);		
		move(place.getX(), place.getY());				
	}

	private Point whereToMove() {
		int x = getX();
		int y = getY();
		if (Direction.RIGHT.equals(direction)) {
			x++;
		} else if (Direction.UP.equals(direction)) {
			y--;
		} else if (Direction.DOWN.equals(direction))  {					
			y++;
		} else {			
			x--;
		}
		return new Point(x, y);
	}

	public boolean itsMyTail(Point point) {
		return elements.getFirst().itsMe(point);
	}

	@Override
	public Iterator<Point> iterator() {
		return elements.iterator();
	}
	

}
