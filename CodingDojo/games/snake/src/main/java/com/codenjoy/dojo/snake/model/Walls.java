package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.artifacts.Wall;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 5:28 AM
 */
public class Walls implements Iterable<Wall>{

    private List<Wall> walls = new LinkedList<Wall>();

    public void add(int x, int y) {
        this.walls.add(new Wall(x, y));
    }

    @Override
    public Iterator<Wall> iterator() {
        LinkedList<Wall> result = new LinkedList<Wall>();
        result.addAll(walls);
        return result.iterator();
    }

    public boolean itsMe(int x, int y) {
        return itsMe(new PointImpl(x, y));
    }

    public boolean itsMe(Point point) {
        for (Point element : walls) {
            if (element.itsMe(point)) {
                return true;
            }
        }
        return false;
    }

    public int getCountOfWalls(){
        return walls.size();
    }
}
