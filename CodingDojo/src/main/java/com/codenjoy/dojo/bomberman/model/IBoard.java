package com.codenjoy.dojo.bomberman.model;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sanja
 * Date: 16.04.13
 * Time: 22:05
 * To change this template use File | Settings | File Templates.
 */
public interface IBoard {
    int size();

    Bomberman getBomberman();

    List<Bomb> getBombs();

    Walls getWalls();

    List<Point> getBlasts();
}
