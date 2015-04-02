package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Tickable;

import java.util.Iterator;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/23/13
 * Time: 12:44 AM
 */
public class WallsDecorator implements Walls, Tickable {
    protected Walls walls;

    public WallsDecorator(Walls walls) {
        this.walls = walls;
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

    @Override
    public Wall get(int x, int y) {
        return walls.get(x, y);
    }

    @Override
    public void tick() {
        walls.tick();
    }
}
