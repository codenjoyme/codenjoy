package com.codenjoy.dojo.snake.model;

import com.codenjoy.dojo.snake.model.artifacts.Point;
import com.codenjoy.dojo.snake.model.artifacts.Wall;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 5:28 AM
 */
public class Walls implements Iterable<Point>{

    private List<Wall> walls = new LinkedList<>();

    public void add(int x, int y) {
        this.walls.add(new Wall(x, y));
    }

    @Override
    public Iterator<Point> iterator() {
        LinkedList result = new LinkedList();
        result.addAll(walls);
        return result.iterator();
    }

    public boolean itsMe(int x, int y) {
        return itsMe(new Point(x, y));
    }

    public boolean itsMe(Point point) {
        for (Point element : walls) {
            if (element.itsMe(point)) {
                return true;
            }
        }
        return false;
    }
}
