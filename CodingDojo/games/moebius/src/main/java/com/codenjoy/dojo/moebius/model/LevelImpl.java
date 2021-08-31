package com.codenjoy.dojo.moebius.model;

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
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.codenjoy.dojo.games.moebius.Element.*;

public class LevelImpl implements Level {

    private LengthToXY xy;
    private String map;

    public LevelImpl(int size) {
        this.map = buildMap(size);
        xy = new LengthToXY(getSize());
    }

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    private String buildMap(int size) {
        StringBuilder board = new StringBuilder();
        board.append(pad(size, '╔', '═', '╗'));
        for (int y = 1; y < size - 1; y++) {
            board.append(pad(size, '║', ' ', '║'));
        }
        board.append(pad(size, '╚', '═', '╝'));
        return board.toString();
    }

    private String pad(int len, char left, char middle, char right) {
        return left + StringUtils.rightPad("", len - 2, middle) + right;
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Line> getLines() {
        return LevelUtils.getObjects(xy, map,
                Line::new,
                LEFT_UP,
                UP_RIGHT,
                RIGHT_DOWN,
                DOWN_LEFT,
                LEFT_RIGHT,
                UP_DOWN,
                CROSS);
    }
}
