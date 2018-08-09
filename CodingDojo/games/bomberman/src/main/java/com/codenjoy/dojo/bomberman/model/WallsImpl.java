package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class WallsImpl implements Walls {
    private List<Wall> walls;

    public WallsImpl() {
        walls = new LinkedList<Wall>();
    }

    public WallsImpl(Walls sourceWalls) {
        this();
        for (Wall wall : sourceWalls) {
            walls.add(wall.copy());
        }
    }

    @Override
    public void add(int x, int y) {
        add(new Wall(x, y));
    }

    @Override
    public Iterator<Wall> iterator() {
        LinkedList result = new LinkedList();
        result.addAll(walls);
        return result.iterator();
    }

    @Override
    public boolean itsMe(int x, int y) {
        for (Wall wall : walls) {
            if (wall.itsMe(x, y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends Wall> List<T> subList(Class<T> filter) {
        List<Wall> result = new LinkedList<Wall>();
        for (Wall input: walls) {
            if (filter.isAssignableFrom(input.getClass())) {
                result.add(input);
            }
        }
        return (List<T>) result;
    }

    @Override
    public void add(Wall wall) {
        walls.add(wall);
    }

    @Override
    public Wall destroy(int x, int y) {
        int index = walls.indexOf(new Wall(x, y));
        if (index == -1) {
            return new Wall(-1, -1);
        }
        return walls.remove(index);
    }

    @Override
    public Wall get(int x, int y) {
        int index = walls.indexOf(new Wall(x, y));
        if (index == -1) {
            return new Wall(-1, -1);
        }
        return walls.get(index);
    }

    @Override
    public void tick() {
        // do nothing
    }
}
