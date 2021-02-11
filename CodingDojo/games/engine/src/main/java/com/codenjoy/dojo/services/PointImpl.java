package com.codenjoy.dojo.services;

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


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Каждый объект на поле имеет свои координаты. Этот класс обычно используется дял указания координат или как родитель.
 * Может использоваться в коллекциях.
 */
@Getter
@Setter
@AllArgsConstructor
public class PointImpl implements Point, Comparable<Point> {

    protected int x;
    protected int y;

    public PointImpl() {
        this(-1, -1);
    }

    public PointImpl(Point point) {
        this(point.getX(), point.getY());
    }

    public PointImpl(JSONObject json) {
        this(json.getInt("x"), json.getInt("y"));
    }

    @Override
    public boolean itsMe(Point pt) {
        return itsMe(pt.getX(), pt.getY());
    }

    public boolean itsMe(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public boolean isOutOf(int size) {
        return isOutOf(0, 0, size);
    }

    @Override
    public boolean isOutOf(int dw, int dh, int size) {
        return x < dw || y < dh || y > size - 1 - dh || x > size - 1 - dw;
    }

    @Override
    public double distance(Point other) {
        return Math.sqrt((x - other.getX())*(x - other.getX()) + (y - other.getY())*(y - other.getY()));
    }

    @Override
    public int hashCode() {
        return x*1000 + y;
    }

    public int parentHashCode() {
        return super.hashCode();
    }

    public boolean parentEquals(Object o) {
        return super.equals(o);
    }

    @Override
    public String toString() {
        return String.format("[%s,%s]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof PointImpl)) {
            return false;
        }

        PointImpl p = (PointImpl)o;

        return (p.x == x && p.y == y);
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void move(Point pt) {
        this.x = pt.getX();
        this.y = pt.getY();
    }

    @Override
    public Point copy() {
        return new PointImpl(this);
    }

    @Override
    public void change(Point delta) {
        x += delta.getX();
        y += delta.getY();
    }

    @Override
    public void change(QDirection direction) {
        this.move(direction.change(this));
    }

    @Override
    public void change(Direction direction) {
        this.move(direction.change(this));
    }

    public static Point pt(int x, int y) {
        return new PointImpl(x, y);
    }

    @Override
    public int compareTo(Point o) {
        if (o == null) {
            return -1;
        }
        return Integer.compare(this.hashCode(), o.hashCode());
    }

    @Override
    public Point relative(Point offset) {
        return pt(x - offset.getX(), y - offset.getY());
    }

    public static Point random(Dice dice, int size) {
        return pt(dice.next(size), dice.next(size));
    }
}
