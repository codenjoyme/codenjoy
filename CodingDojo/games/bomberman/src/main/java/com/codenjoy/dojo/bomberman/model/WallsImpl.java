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


import com.codenjoy.dojo.services.Point;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

public class WallsImpl implements Walls {

    private List<Wall> walls;

    public WallsImpl() {
        walls = new LinkedList<>();
    }

    public WallsImpl(Walls input) {
        this();
        for (Wall wall : input) {
            walls.add(wall.copy());
        }
    }

    @Override
    public void add(Point pt) {
        add(new Wall(pt));
    }

    @Override
    public void init(Field field) {
        // do nothing
    }

    @Override
    public Iterator<Wall> iterator() {
        LinkedList result = new LinkedList();
        result.addAll(walls);
        return result.iterator();
    }

    @Override
    public boolean itsMe(Point pt) {
        return walls.stream()
                .anyMatch(wall -> wall.itsMe(pt));
    }

    @Override
    public <T extends Wall> List<T> listSubtypes(Class<T> filter) {
        return list(wall -> filter.isAssignableFrom(wall.getClass()));
    }

    @Override
    public <T extends Wall> List<T> listEquals(Class<T> filter) {
        return list(wall -> filter.equals(wall.getClass()));
    }

    private List list(Predicate<Wall> predicate) {
        return (List)walls.stream()
                .filter(predicate)
                .collect(toList());
    }

    @Override
    public void add(Wall wall) {
        walls.add(wall);
    }

    @Override
    public Wall destroy(Point pt) {
        int index = walls.indexOf(new Wall(pt));
        if (index == -1) {
            return new Wall(-1, -1);
        }
        return walls.remove(index);
    }

    @Override
    public Wall destroyExact(Wall wall) {
        for (int index = 0; index < walls.size(); index++) {
            Wall item = walls.get(index);
            // если тот же элемент, или тип тот же и координаты идентичны
            if (item == wall
                    || ( item.getClass().equals(wall.getClass())
                        && item.equals(wall)))
            {
                return walls.remove(index);
            }
        }
        return new Wall(-1, -1);
    }

    @Override
    public Wall get(Point pt) {
        int index = walls.indexOf(new Wall(pt));
        if (index == -1) {
            return new NotAWall(-1, -1);
        }
        return walls.get(index);
    }

    @Override
    public void tick() {
        new LinkedList<>(walls).forEach(Wall::tick);
    }
}
