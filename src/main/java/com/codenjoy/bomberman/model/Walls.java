package com.codenjoy.bomberman.model;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:28 PM
 */
public interface Walls extends Iterable<Wall> {
    void add(int x, int y);

    boolean itsMe(int x, int y);

    List<Point> asList();

    void add(Wall wall);
}
