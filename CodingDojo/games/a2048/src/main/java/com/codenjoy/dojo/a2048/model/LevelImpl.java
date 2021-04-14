package com.codenjoy.dojo.a2048.model;

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


import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.List;

public class LevelImpl implements Level {

    private LengthToXY xy;
    private String map;
    private int size;

    public LevelImpl(String board) {
        map = LevelUtils.clear(board);
        size = (int)Math.sqrt(board.length());
        xy = new LengthToXY(size());
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public List<Number> numbers() {
        return LevelUtils.getObjects(xy, map,
                (pt, el) -> new Number(el.number(), pt),
                Elements.valuesExcept(Elements.NONE));
    }

    @Override
    public List<Number> breaks() {
        return LevelUtils.getObjects(xy, map,
                (pt, el) -> new Number(el.number(), pt),
                Elements._x);
    }

}
