package com.codenjoy.dojo.snake.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.snake.model.artifacts.Element;
import com.codenjoy.dojo.snake.model.artifacts.Tail;
import com.codenjoy.dojo.snake.services.Events;

import java.util.Iterator;
import java.util.LinkedList;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.snake.model.BodyDirection.*;

public class Hero extends PlayerHero<Field> implements Element, Iterable<Tail> {

    private LinkedList<Tail> elements;
    private Direction direction; 
    private boolean alive;
    private int growBy;
    private Player player;

    public Hero(int x, int y) {
        elements = new LinkedList<>();
        elements.addFirst(new Tail(x, y, this));
        elements.addFirst(new Tail(x - 1, y, this));
        
        growBy = 0;
                
        direction = RIGHT;
        alive = true;
    }

    @Override
    public String toString() {
        return String.format("Snake[%s, %s]", direction, elements.toString());
    }

    public static Hero createHero(int size, int startLength) {
        int x = (size - 1)/2;
        int y = (size - 1)/2;
        Hero hero = new Hero(x, y);
        hero.growBy = Math.max(0, startLength - 2);
        return hero;
    }

    public int getX() {
        return getHead().getX();
    }

    public int getY() {
        return getHead().getY();
    }

    public int getLength() {
        return elements.size() + growBy;
    }

    public Direction getDirection() {
        return direction;
    }

    public void move(int x, int y) {
        elements.addLast(new Tail(x, y, this));
        
        if (growBy < 0) {             
            for (int count = 0; count <= -growBy; count++) {
                elements.removeFirst();
            }
            growBy = 0;
        } else if (growBy > 0) {
            growBy--;
        } else { // == 0
            elements.removeFirst();
        }
    }

    @Override
    public void down() {
        if (!alive) return;
        direction = DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;
        direction = UP;
    }

    @Override
    public void left() {
        if (!alive) return;
        direction = LEFT;
    }

    @Override
    public void right() {
        if (!alive) return;
        direction = RIGHT;
    }

    @Override
    public void act(int... p) {
        // do nothing
    }

    public boolean isAlive() {
        return alive;
    }

    public void killMe() {
        player.event(Events.KILL);
        alive = false;
    }

    public void grow() {
        growBy = 1;
        player.event(Events.EAT_APPLE);
    }

    public boolean itsMyHead(Point point) {
        return (getHead().itsMe(point));
    }
    
    public boolean itsMe(Point point) {
        return itsMyBody(point) || itsMyHead(point);
    }

    public boolean itsMe(int x, int y) {
        return itsMe(pt(x, y));
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
    public void affect(Hero snake) {
        killMe();
    }

    public void walk(Field board) {
        Point place = whereToMove();                                
        place = teleport(board.getSize(), place);
        board.getAt(place).affect(this);
        validatePosition(board.getSize(), place);
        move(place.getX(), place.getY());
    }

    private void validatePosition(int boardSize, Point place) {
        if (place.getX() >= boardSize || place.getX() < 0 ||
            place.getY() >= boardSize || place.getY() < 0)
        {
            this.killMe();
        }
    }

    private Point teleport(int boardSize, Point point) {
        int x = point.getX();
        int y = point.getY();
        if (x == boardSize) {
            x = 0;
        } else if (x == -1) {
            x = boardSize - 1;
        }
        if (y == boardSize) {
            y = 0;
        } else if (y == -1) {
            y = boardSize - 1;
        }

        return pt(x, y);
    }

    private Point whereToMove() {
        int x = direction.changeX(getX());
        int y = direction.changeY(getY());
        return pt(x, y);
    }

    public boolean itsMyTail(Point point) {
        return getTail().itsMe(point);
    }

    public Point getTail() {
        return elements.getFirst();
    }

    @Override
    public Iterator<Tail> iterator() {
        return elements.descendingIterator();
    }

    public void eatStone() {
        if (elements.size() <= 10) {
            player.event(Events.EAT_STONE);
            killMe();
        } else {
            growBy = -10;
            player.event(Events.EAT_STONE);
        }        
    }

    public BodyDirection getBodyDirection(Point curr) {
        int currIndex = elements.indexOf(curr);
        Point prev = elements.get(currIndex - 1);
        Point next = elements.get(currIndex + 1);

        BodyDirection nextPrev = orientation(next, prev);
        if (nextPrev != null) {
            return nextPrev;
        }

        if (orientation(prev, curr) == HORIZONTAL) {
            boolean clockwise = curr.getY() < next.getY() ^ curr.getX() > prev.getX();
            if (curr.getY() < next.getY()) {
                return (clockwise)?TURNED_RIGHT_UP:TURNED_LEFT_UP;
            } else {
                return (clockwise)?TURNED_LEFT_DOWN:TURNED_RIGHT_DOWN;
            }
        } else {
            boolean clockwise = curr.getX() < next.getX() ^ curr.getY() < prev.getY();
            if (curr.getX() < next.getX()) {
                return (clockwise)?TURNED_RIGHT_DOWN:TURNED_RIGHT_UP;
            } else {
                return (clockwise)?TURNED_LEFT_UP:TURNED_LEFT_DOWN;
            }
        }
    }

    private BodyDirection orientation(Point curr, Point next) {
        if (curr.getX() == next.getX()) {
            return VERTICAL;
        } else if (curr.getY() == next.getY()) {
            return HORIZONTAL;
        } else {
            return null;
        }
    }

    public Direction getTailDirection() {
        Point prev = elements.get(1);
        Point tail = getTail();

        if (prev.getX() == tail.getX()) {
            return (prev.getY() < tail.getY())?UP:DOWN;
        } else {
            return (prev.getX() < tail.getX())?RIGHT:LEFT;
        }
    }

    @Override
    public void tick() {
        // do nothing
    }

    public int getGrowBy() {
        return growBy;
    }

    public void init(Player player) {
        this.player = player;
    }
}
