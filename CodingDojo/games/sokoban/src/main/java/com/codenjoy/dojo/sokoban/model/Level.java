package com.codenjoy.dojo.sokoban.model;

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
import com.codenjoy.dojo.services.field.AbstractLevel;
import com.codenjoy.dojo.sokoban.model.items.*;
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.List;

import static com.codenjoy.dojo.games.sokoban.Element.*;

public class Level extends AbstractLevel {

    private int marksToWin;

    public Level(String map) {
        super(map);
        this.marksToWin = getMarks().size();
    }

    public Level(String map, int marksToWin) {
        super(map);
        this.marksToWin = marksToWin;
    }

    public int getMarksToWin() {
        return marksToWin;
    }

    public List<Hero> getHero() {
        return find(Hero::new, HERO);
    }

    public List<Wall> getWalls() {
        return find(Wall::new, WALL);
    }

    public List<Box> getBoxes() {
        return find(Box::new, BOX);
    }

    public List<Mark> getMarks() {
        return find(Mark::new, MARK_TO_WIN);
    }

    public List<BoxOnTheMark> getBoxesOnTheMarks() {
        return find(BoxOnTheMark::new, BOX_ON_THE_MARK);
    }
}