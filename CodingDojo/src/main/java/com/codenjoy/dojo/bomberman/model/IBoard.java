package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 16.04.13
 * Time: 22:05
 */
public interface IBoard {  // TODO применить тут ISP (все ли методы должны быть паблик?)
    int size();

    List<Bomberman> getBombermans();

    List<Bomb> getBombs();

    List<Bomb> getBombs(MyBomberman bomberman);

    Walls getWalls();

    boolean isBarrier(int x, int y, boolean isWithMeatChopper);

    void remove(Player player);

    List<Blast> getBlasts();

    void drop(Bomb bomb);

    void removeBomb(Bomb bomb);
}
