package com.codenjoy.dojo.sokoban.model.levels;

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


import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.sokoban.model.items.*;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.List;

import static com.codenjoy.dojo.sokoban.model.items.Elements.*;

public class LevelImpl implements Level {

    private LengthToXY xy;
    private int marksToWin;
    private String map;

    public LevelImpl(String map) {
        this.map = LevelUtils.clear(map);
        xy = new LengthToXY(getSize());
        this.marksToWin = getMarks().size();
    }

    public LevelImpl(String map, int marksToWin) {
        this.map = map;
        xy = new LengthToXY(getSize());
        this.marksToWin = marksToWin;
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public int getMarksToWin() {
        return marksToWin;
    }

    @Override
    public List<Hero> getHero() {
        return LevelUtils.getObjects(xy, map,
                Hero::new,
                HERO);

    }

    @Override
    public List<Wall> getWalls() {
        return LevelUtils.getObjects(xy, map,
                Wall::new,
                WALL);
    }

    @Override
    public List<Box> getBoxes() {
        return LevelUtils.getObjects(xy, map,
                Box::new,
                BOX);
    }

    @Override
    public List<Mark> getMarks() {
        return LevelUtils.getObjects(xy, map,
                Mark::new,
                MARK_TO_WIN);
    }

    @Override
    public List<BoxOnTheMark> getBoxesOnTheMarks() {
        return LevelUtils.getObjects(xy, map,
                BoxOnTheMark::new,
                BOX_ON_THE_MARK);
    }

}
