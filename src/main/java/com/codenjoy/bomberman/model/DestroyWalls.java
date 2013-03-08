package com.codenjoy.bomberman.model;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:17 PM
 */
public class DestroyWalls implements Walls {

    private Walls walls;
    private int size;
    private int count;

    public DestroyWalls(Walls walls, int size) {
        this(walls, size, size*size/4);
    }

    public DestroyWalls(Walls walls) {
        this.walls = new WallsImpl();
        for (Wall wall : walls) {
            this.walls.add(new DestroyWall(wall.x, wall.y));
        }
    }

    private DestroyWalls(Walls walls, int size, int count) {
        if (walls.subList(Wall.class).size() + count >= size*size - 1) {
            throw new IllegalArgumentException("No more space at board for DestroyWalls");
        }
        this.walls = walls;
        this.size = size;
        this.count = count;
        randomDestroyWalls();
    }

    private void randomDestroyWalls() {
        int index = 0;
        int counter = 0;
        do {
            int x = new Random().nextInt(size);
            int y = new Random().nextInt(size);

            if (walls.itsMe(x, y)) {
                continue;
            }

            if (x <= 3 && y == 1) {  // чтобы бомбермену было откуда начинать
                continue;
            }

            walls.add(new DestroyWall(x, y));

            index++;
            counter++;
        } while (index != count && counter < 10000);
        if (counter == 10000) {
            throw new  RuntimeException("Dead loop at DestroyWalls.randomDestroyWalls!");
        }
    }

    @Override
    public Iterator<Wall> iterator() {
        return walls.iterator();
    }

    @Override
    public void add(int x, int y) {
        walls.add(x, y);
    }

    @Override
    public boolean itsMe(int x, int y) {
        return walls.itsMe(x, y);
    }

    @Override
    public List<Wall> subList(Class<? extends Wall>...filter) {
        return walls.subList(filter);
    }

    @Override
    public void add(Wall wall) {
        walls.add(wall);
    }

    @Override
    public void destroy(int x, int y) {
        walls.destroy(x, y);
    }
}
