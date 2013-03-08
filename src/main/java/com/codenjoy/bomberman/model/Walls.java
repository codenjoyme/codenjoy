package com.codenjoy.bomberman.model;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 6:04 PM
 */
public class Walls implements Iterable<Wall> {
    private List<Wall> walls;

    public Walls() {
        walls = new LinkedList<Wall>();
    }

    public Walls(Walls sourceWalls) {
        this();
        for (Wall wall : sourceWalls) {
            walls.add(new Wall(wall));
        }
    }

    public void add(int x, int y) {
        this.walls.add(new Wall(x, y));
    }

    @Override
    public Iterator<Wall> iterator() {
        LinkedList result = new LinkedList();
        result.addAll(walls);
        return result.iterator();
    }

    public boolean itsMe(int x, int y) {
        for (Wall wall : walls) {
            if (wall.itsMe(x, y)) {
                return true;
            }
        }
        return false;
    }

    public List<Point> asList() {
        return ListUtils.unmodifiableList(walls);
    }
}
