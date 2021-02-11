package com.codenjoy.dojo.sokoban.model.itemsImpl;

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


import com.codenjoy.dojo.sokoban.model.items.Level;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.sokoban.model.itemsImpl.Elements.*;
import static java.util.stream.Collectors.toList;

public class LevelImpl implements Level {
    private final LengthToXY xy;
    private final int expectedBoxesValuesInMarks;
    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
        this.expectedBoxesValuesInMarks = getMarks().size();
    }

    public LevelImpl(String map, int expectedBoxesValuesInMarks) {
        this.map = map;
        xy = new LengthToXY(getSize());
        this.expectedBoxesValuesInMarks = expectedBoxesValuesInMarks;
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public int getExpectedBoxesValuesInMarks() {
        return this.expectedBoxesValuesInMarks;
    }

    @Override
    public List<Hero> getHero() {
        return pointsOf(HERO).stream()
                .map(Hero::new)
                .collect(toList());

    }

    @Override
    public List<Gold> getGold() {
        return pointsOf(GOLD).stream()
                .map(Gold::new)
                .collect(toList());
    }

    @Override
    public List<Wall> getWalls() {
        return pointsOf(WALL).stream()
                .map(Wall::new)
                .collect(toList());
    }

    @Override
    public List<Box> getBoxes() {
        return pointsOf(BOX).stream()
                .map(Box::new)
                .collect(toList());
    }

    @Override
    public List<Mark> getMarks() {
        return pointsOf(MARK_TO_WIN).stream()
                .map(Mark::new)
                .collect(toList());
    }

    @Override
    public List<BoxOnTheMark> getBoxesOnTheMarks() {
        return pointsOf(BOX_ON_THE_MARK).stream()
                .map(BoxOnTheMark::new)
                .collect(toList());
    }

    private List<Point> pointsOf(Elements element) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }


}
