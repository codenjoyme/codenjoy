package com.codenjoy.dojo.bomberman.client;

import com.codenjoy.dojo.bomberman.model.Elements;
import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public Point getBomberman() {
        List<Point> result = new LinkedList<Point>();
        result.addAll(findAll(BOMBERMAN));
        result.addAll(findAll(BOMB_BOMBERMAN));
        result.addAll(findAll(DEAD_BOMBERMAN));
        return result.get(0);
    }

    public Collection<Point> getOtherBombermans() {
        List<Point> result = new LinkedList<Point>();
        result.addAll(findAll(OTHER_BOMBERMAN));
        result.addAll(findAll(OTHER_BOMB_BOMBERMAN));
        result.addAll(findAll(OTHER_DEAD_BOMBERMAN));
        return result;
    }

    public boolean isGameOver() {
        return board.indexOf(DEAD_BOMBERMAN.ch()) != -1;
    }


    public Elements getAt(int x, int y) {
        if (x < 0 || y < 0 || x >= size || y >= size) {
            return WALL;
        }
        return super.getAt(x, y);
    }

    public List<Point> getBarriers() {
        List<Point> all = getMeatChoppers();
        all.addAll(getWalls());
        all.addAll(getBombs());
        all.addAll(getDestroyWalls());
        all.addAll(getOtherBombermans());

        return removeDuplicates(all);
    }

    @Override
    public String toString() {
        return String.format("Board:\n%s\n" +
            "Bomberman at: %s\n" +
            "Other bombermans at: %s\n" +
            "Meat choppers at: %s\n" +
            "Destroy walls at: %s\n" +
            "Bombs at: %s\n" +
            "Blasts: %s\n" +
            "Expected blasts at: %s",
                boardAsString(),
                getBomberman(),
                getOtherBombermans(),
                getMeatChoppers(),
                getDestroyWalls(),
                getBombs(),
                getBlasts(),
                getFutureBlasts());
    }

    public List<Point> getMeatChoppers() {
        return findAll(MEAT_CHOPPER);
    }

    public List<Point> getWalls() {
        return findAll(WALL);
    }

    public List<Point> getDestroyWalls() {
        return findAll(DESTROY_WALL);
    }

    public List<Point> getBombs() {
        List<Point> result = new LinkedList<Point>();
        result.addAll(findAll(BOMB_TIMER_1));
        result.addAll(findAll(BOMB_TIMER_2));
        result.addAll(findAll(BOMB_TIMER_3));
        result.addAll(findAll(BOMB_TIMER_4));
        result.addAll(findAll(BOMB_TIMER_5));
        result.addAll(findAll(BOMB_BOMBERMAN));
        return result;
    }

    public List<Point> getBlasts() {
        return findAll(BOOM);
    }

    public List<Point> getFutureBlasts() {
        List<Point> result = new LinkedList<Point>();
        List<Point> bombs = getBombs();
        bombs.addAll(findAll(OTHER_BOMB_BOMBERMAN));
        bombs.addAll(findAll(BOMB_BOMBERMAN));

        for (Point bomb : bombs) {
            result.add(bomb);
            result.add(new PointImpl(bomb.getX() - 1, bomb.getY()));
            result.add(new PointImpl(bomb.getX() + 1, bomb.getY()));
            result.add(new PointImpl(bomb.getX()    , bomb.getY() - 1));
            result.add(new PointImpl(bomb.getX()    , bomb.getY() + 1));
        }
        for (Point blast : result.toArray(new Point[0])) {
            if (blast.isOutOf(size) || getWalls().contains(blast)) {
                result.remove(blast);
            }
        }
        return removeDuplicates(result);
    }

    public boolean isBarrierAt(int x, int y) {
        return getBarriers().contains(pt(x, y));
    }

}