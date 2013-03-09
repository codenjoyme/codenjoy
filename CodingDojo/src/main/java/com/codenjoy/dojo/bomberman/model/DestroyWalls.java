package com.codenjoy.dojo.bomberman.model;

import java.util.Iterator;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:17 PM
 */
public class DestroyWalls implements Walls {

    private Walls walls;
    private int size;
    private int count;
    private Dice dice;

    public DestroyWalls(Walls walls, Dice dice) {
        this.dice = dice;
        this.walls = new WallsImpl();
        for (Wall wall : walls) {
            this.walls.add(new DestroyWall(wall.x, wall.y));
        }
    }

    public DestroyWalls(Walls walls, int size, Dice dice) {
        this.dice = dice;
        this.walls = walls;
        this.size = size;
        this.count = size*size/10;
        randomFill();
    }

    private void randomFill() {
        int index = 0;
        int counter = 0;
        do {
            int x = dice.next(size);
            int y = dice.next(size);

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
            throw new  RuntimeException("Dead loop at DestroyWalls.randomFill!");
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
    public <T extends Wall> List<T> subList(Class<T> filter) {
        return walls.subList(filter);
    }

    @Override
    public void add(Wall wall) {
        walls.add(wall);
    }

    @Override
    public Wall destroy(int x, int y) {
        return walls.destroy(x, y);
    }
}
