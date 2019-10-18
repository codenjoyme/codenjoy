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


/**
 * Incapsulates coordinate of Element at the Board. All Board Elements must extends from PointImpl,
 * realized from of this interface.
 */
public interface Point extends Comparable<Point> {
    /**
     * @return Current X coordinate.
     */
    int getX();

    /**
     * @return Current Y coordinate.
     */
    int getY();

    /**
     * Change X and Y coordinates with new values.
     */
    void move(int x, int y);

    /**
     * Change X and Y coordinates with new values.
     */
    void move(Point pt);

    /**
     * @return cloned object.
     */
    Point copy();

    void setX(int x);

    void setY(int y);

    /**
     * @return true if points has equals X and Y coordinates.
     */
    boolean itsMe(Point point);

    /**
     * @return true if points has equals X and Y coordinates.
     */
    boolean itsMe(int x, int y);

    /**
     * @param size Board size.
     * @return true if point is out of board with given size.
     */
    boolean isOutOf(int size);

    /**
     * @param dw Border inside board.
     * @param dh Border inside board.
     * @param size Board size.
     * @return true if point is out of rectangle inside board.
     */
    boolean isOutOf(int dw, int dh, int size);

    /**
     * @param point2 Another point.
     * @return Distance between two points.
     */
    double distance(Point point2);

    /**
     * Changes current point with given delta.
     * @param delta Increment.
     */
    void change(Point delta);

    /**
     * Changes current point in given direction
     * @param direction one of 8 directions
     */
    void change(QDirection direction);

    /**
     * Changes current point in given direction
     * @param direction one of 4 directions
     */
    void change(Direction direction);

    /**
     * @param offset
     * @return this.x - offset.x, this.y - offset.y
     */
    Point relative(Point offset);
}
