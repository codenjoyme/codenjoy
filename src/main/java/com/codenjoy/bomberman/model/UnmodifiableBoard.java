package com.codenjoy.bomberman.model;

import com.codenjoy.bomberman.model.*;
import org.apache.commons.collections.ListUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 1:00 PM
 */
public class UnmodifiableBoard extends Board{

    public UnmodifiableBoard(Walls walls, Level level, int size) {
        super(walls, level, size);
    }

    public List<Bomb> getBombs() {
        List<Bomb> result = new LinkedList<Bomb>();
        for (Bomb bomb : super.getBombs()) {
            result.add(new BombCopier(bomb));
        }
        return result;
    }

    public List<Point> getBlasts() {
        List<Point> result = new LinkedList<Point>();
        for (Point blast : super.getBlasts()) {
            result.add(new Point(blast));
        }
        return result;
    }

    public Walls getWalls() {
        return new Walls(super.getWalls());
    }

}
