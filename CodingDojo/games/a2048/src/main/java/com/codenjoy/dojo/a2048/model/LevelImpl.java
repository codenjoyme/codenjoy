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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.apache.commons.lang3.StringUtils;

import java.lang.*;
import java.util.LinkedList;
import java.util.List;

public class LevelImpl implements Level {

    private final Settings settings;
    private LengthToXY xy;
    private Parameter<Integer> size;
    private Parameter<Integer> newAdd;
    private Parameter<Integer> mode;

    private String map;

    public LevelImpl() {
        settings = new SettingsImpl();
        size = settings.addEditBox("Size").type(Integer.class).def(5);
        newAdd = settings.addEditBox("New numbers").type(Integer.class).def(-1);
        mode = settings.addEditBox("Mode").type(Integer.class).def(0);
        map = StringUtils.leftPad("", size(), ' ');
        xy = new LengthToXY(size());
    }

    public LevelImpl(String board) {
        this();
        map = board;
        size.update((int)Math.sqrt(board.length()));
        xy = new LengthToXY(size());
    }

    @Override
    public int size() {
        return size.getValue();
    }

    @Override
    public List<Number> getNumbers() {
        List<Number> result = new LinkedList<Number>();

        for (Elements element : Elements.values()) {
            if (element == Elements.NONE) continue;

            List<Point> points = getPointsOf(element);
            for (Point pt : points) {
                result.add(new Number(element.number(), pt));
            }
        }

        return result;
    }

    @Override
    public int getNewAdd() {
        return newAdd.getValue();
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public Mode getMode() {
        return Mode.values()[mode.getValue()];
    }

    private List<Point> getPointsOf(Elements element) {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == element.ch) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }
}
