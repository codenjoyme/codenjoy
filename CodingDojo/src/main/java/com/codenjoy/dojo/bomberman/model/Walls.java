package com.codenjoy.dojo.bomberman.model;

import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:28 PM
 */
public interface Walls extends Iterable<Wall> {
    void add(int x, int y);

    boolean itsMe(int x, int y);

    <T extends Wall> List<T> subList(Class<T> filter);

    void add(Wall wall);

    Wall destroy(int x, int y);

    Wall get(int x, int y);
}
