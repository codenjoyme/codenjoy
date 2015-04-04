package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 20.04.13
 * Time: 14:02
 */
public interface Point extends Comparable<Point> {
    int getX();

    int getY();

    PointImpl copy();

    boolean itsMe(Point point);

    boolean itsMe(int x, int y);

    boolean isOutOf(int size);

    double distance(Point point2);
}
