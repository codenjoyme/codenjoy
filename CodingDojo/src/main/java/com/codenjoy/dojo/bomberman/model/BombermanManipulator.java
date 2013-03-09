package com.codenjoy.dojo.bomberman.model;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 8:30 PM
 */
public interface BombermanManipulator extends Bomberman {
    void apply();

    void kill();

    boolean itsMe(Point point);
}
