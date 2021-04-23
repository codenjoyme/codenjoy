package com.codenjoy.dojo.services.algs;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Оптимизированная версия Map<Point, List<Direction>>.
 * Хранит список направлений движений, которым надо следовать
 * чтобы прийти из from в заданную точку.
 */
public class Path {

    private List<Direction>[][] all;

    public Path(int size) {
        all = new ArrayList[size][size];
    }

    private List<Direction> setList(Point pt, List<Direction> list) {
        return all[pt.getX()][pt.getY()] = list;
    }

    private List<Direction> getList(Point pt) {
        return all[pt.getX()][pt.getY()];
    }

    public List<Direction> get(Point pt) {
        List<Direction> list = getList(pt);
        if (list != null) {
            return list;
        }
        list = new ArrayList(100);
        setList(pt, list);
        return list;
    }
}
