package com.codenjoy.dojo.snake.model;

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
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.snake.model.artifacts.Wall;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Walls implements Iterable<Wall>{

    private List<Wall> walls = new LinkedList<>();

    public void add(int x, int y) {
        this.walls.add(new Wall(x, y));
    }

    @Override
    public Iterator<Wall> iterator() {
        return new LinkedList<Wall>(){{
            addAll(walls);
        }}.iterator();
    }

    public boolean itsMe(int x, int y) {
        return itsMe(pt(x, y));
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
