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


import com.codenjoy.dojo.services.Tickable;

import java.util.Iterator;
import java.util.List;

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
