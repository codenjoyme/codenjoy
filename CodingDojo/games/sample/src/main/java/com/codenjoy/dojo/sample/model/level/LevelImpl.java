package com.codenjoy.dojo.sample.model.level;

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


import com.codenjoy.dojo.sample.model.Hero;
import com.codenjoy.dojo.sample.model.items.Bomb;
import com.codenjoy.dojo.sample.model.items.Gold;
import com.codenjoy.dojo.sample.model.items.Wall;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.utils.LevelUtils;

import static com.codenjoy.dojo.sample.model.Elements.*;
import static com.codenjoy.dojo.utils.LevelUtils.*;

import java.util.List;

/**
 * Полезный утилитный класс для получения объектов на поле из текстового вида.
 */
public class LevelImpl implements Level {

    private LengthToXY xy;
    private String map;

    public LevelImpl(String map) {
        this.map = LevelUtils.clear(map);
        xy = new LengthToXY(size());
    }

    @Override
    public int size() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> heroes() {
        return getObjects(xy, map, Hero::new, HERO);
    }

    @Override
    public List<Gold> gold() {
        return getObjects(xy, map, Gold::new, GOLD);
    }

    @Override
    public List<Bomb> bombs() {
        return getObjects(xy, map, Bomb::new, BOMB);
    }

    @Override
    public List<Wall> walls() {
        return getObjects(xy, map, Wall::new, WALL);
    }


}
