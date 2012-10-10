package com.globallogic.snake.model;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import com.globallogic.snake.model.artifacts.Element;
import com.globallogic.snake.model.artifacts.Point;

public class Snake implements Element, Iterable<Point>, Joystick {

	private Deque<Point> elements;
	private Direction direction; 
	private boolean alive;
	private int growBy;

	public Snake(int x, int y) {	
		elements = new LinkedList<Point>();
		elements.addFirst(new Point(x, y));
		elements.addFirst(new Point(x - 1, y));
		
		growBy = 0;
				
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

	public void move(int x, int y) {
		elements.addLast(new Point(x, y));
		
		if (growBy < 0) { 			
			for (int count = 0; count <= -growBy; count++) {
				elements.removeFirst();
			}
		} else if (growBy > 0) {
			
		} else { // == 0
			elements.removeFirst();
		}
		growBy = 0;		
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
		growBy = 1;
	}

	public boolean itsMyHead(Point point) {
		return (getHead().itsMe(point));
	}
	
	public boolean itsMe(Point point) {
		return itsMyBody(point) || itsMyHead(point);
	}

    public boolean itsMe(int x, int y) {
        return itsMe(new Point(x, y));
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

	public void walk(Board board) {
		Point place = whereToMove();								
		board.getAt(place).affect(this);		
		// TODO тут если написать так
        //
        // то змейка попадая на стенку телепортируется и появится с другой стороны
        move(place.getX(), place.getY());
	}

	private Point whereToMove() {
		int x = getX();
		int y = getY();
		if (Direction.RIGHT.equals(direction)) {
			x++;
		} else if (Direction.UP.equals(direction)) {
			y++;
		} else if (Direction.DOWN.equals(direction))  {					
			y--;
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

	public void eatStone() {
		if (elements.size() <= 10) {
			killMe();
		} else {
			growBy = -10;
		}		
	}	

}
