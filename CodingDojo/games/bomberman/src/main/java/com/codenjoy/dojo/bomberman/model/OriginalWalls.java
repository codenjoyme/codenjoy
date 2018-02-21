package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 10/10/12
 * Time: 8:29 PM
 */
public class OriginalWalls extends WallsImpl implements Iterable<Wall> {

    public OriginalWalls(List<Point> points) {
        super();

        for (Point pt : points) {
            add(pt.getX(), pt.getY());
        }
    }

    @Override
    public void tick() {
        super.tick(); // TODO протестить эту строчку
    }

}
