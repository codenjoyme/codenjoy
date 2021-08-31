package com.codenjoy.dojo.startandjump.model;

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
import com.codenjoy.dojo.games.startandjump.Element;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.List;

public class LevelImpl implements Level {

    private LengthToXY xy;
    private String map;

    public LevelImpl(String map) {
        this.map = LevelUtils.clear(map);
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Hero> getHero() {
        return LevelUtils.getObjects(xy, map,
                Hero::new,
                Element.HERO);
    }

    @Override
    public List<Wall> getWalls() {
        return LevelUtils.getObjects(xy, map,
                Wall::new,
                Element.WALL);
    }

    @Override
    public List<Platform> getPlatforms() {
        return LevelUtils.getObjects(xy, map,
                pt -> new Platform(pt),
                Element.PLATFORM);
    }
}
