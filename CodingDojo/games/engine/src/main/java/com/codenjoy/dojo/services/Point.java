package com.codenjoy.dojo.services;

/**
 * User: sanja
 * Date: 20.04.13
 * Time: 14:02
 */
public interface Point {
    int getX();

    int getY();

    PointImpl copy();

    boolean itsMe(Point point);

    boolean itsMe(int x, int y);

    boolean isOutOf(int size);
}
