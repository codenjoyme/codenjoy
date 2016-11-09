package com.codenjoy.dojo.snake.battle.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.*;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static com.codenjoy.dojo.snake.battle.model.BodyDirection.*;
import static com.codenjoy.dojo.snake.battle.model.DirectionUtils.getPointAt;
import static com.codenjoy.dojo.snake.battle.model.TailDirection.*;

/**
 * Это реализация змейки. Змейка имплементит {@see Joystick}, а значит может быть управляема фреймворком
 * Так же она имплементит {@see Tickable}, что значит - есть возможность её оповещать о каждом тике игры.
 */
public class Hero implements Joystick, Tickable, State<LinkedList<Tail>, Player> {

    private LinkedList<Tail> elements;
    private Field field;
    private boolean alive;
    private Direction direction;
    private int growBy;

    Hero(Point xy) {
        elements = new LinkedList<>();
        elements.add(new Tail(xy.getX() - 1, xy.getY(), this));
        elements.add(new Tail(xy, this));
        growBy = 0;
        direction = RIGHT;
        alive = true;
    }

    List<Tail> getBody() {
        return elements;
    }

    Tail getTail() {
        return elements.getFirst();
    }

    Point getHead() {
        return elements.getLast();
    }

    void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive) return;

        direction = RIGHT;
    }

    @Override
    public void act(int... p) {
        if (!alive) return;

//        field.setStone(x, y);
    }

    Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) {
            clear();
            return;
        }

        Point next = getNextPoint();

        if (field.isApple(next)) {
            growBy(1);
        }
        if (field.isStone(next)) {
            alive = false;
        }
        if (field.isBarrier(next)) {
            alive = false;
        }
        if (growBy > 0)
            grow(next);
        else
            move(next);
    }

    Point getNextPoint() {
        return getPointAt(getHead(), direction);
    }

    private void grow(Point newLocation) {
        growBy--;
        elements.add(new Tail(newLocation, this));
    }

    private void move(Point newLocation) {
        elements.add(new Tail(newLocation, this));
        elements.removeFirst();
    }

    boolean isAlive() {
        return alive;
    }

    @Override
    public LinkedList<Tail> state(Player player, Object... alsoAtPoint) {
        return elements;
    }

    BodyDirection getBodyDirection(Tail curr) {
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
                return (clockwise) ? TURNED_RIGHT_UP : TURNED_LEFT_UP;
            } else {
                return (clockwise) ? TURNED_LEFT_DOWN : TURNED_RIGHT_DOWN;
            }
        } else {
            boolean clockwise = curr.getX() < next.getX() ^ curr.getY() < prev.getY();
            if (curr.getX() < next.getX()) {
                return (clockwise) ? TURNED_RIGHT_DOWN : TURNED_RIGHT_UP;
            } else {
                return (clockwise) ? TURNED_LEFT_UP : TURNED_LEFT_DOWN;
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

    TailDirection getTailDirection() {
        Point body = elements.get(1);
        Point tail = getTail();

        if (body.getX() == tail.getX()) {
            return (body.getY() < tail.getY()) ? VERTICAL_UP : VERTICAL_DOWN;
        } else {
            return (body.getX() < tail.getX()) ? HORIZONTAL_RIGHT : HORIZONTAL_LEFT;
        }
    }

    boolean itsMyHead(Point point) {
        return (getHead().itsMe(point));
    }

    boolean itsMyTail(Point point) {
        return getTail().itsMe(point);
    }

    void growBy(int val) {
        growBy += val;
    }

    private void clear(){
        elements = new LinkedList<>();
        growBy = 0;
    }
}
