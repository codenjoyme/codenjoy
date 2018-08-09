package com.codenjoy.dojo.reversi.model;

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


import com.codenjoy.dojo.reversi.model.items.Break;
import com.codenjoy.dojo.reversi.model.items.Chip;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(size());
    }

    @Override
    public int size() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Break> breaks(Field field) {
        return new LinkedList<Break>(){{
            addAll(pointsOf(Elements.BREAK).stream()
                    .map(Break::new)
                    .collect(toList()));
        }};
    }

    @Override
    public List<Chip> chips(Field field) {
        return new LinkedList<Chip>(){{
            addAll(pointsOf(Elements.WHITE, Elements.WHITE_TURN).stream()
                    .map(pt -> new Chip(true, pt, field))
                    .collect(toList()));

            addAll(pointsOf(Elements.BLACK, Elements.BLACK_TURN).stream()
                    .map(pt -> new Chip(false, pt, field))
                    .collect(toList()));
        }};
    }

    @Override
    public boolean currentColor() {
        boolean whiteTurn = !pointsOf(Elements.WHITE_TURN).isEmpty();
        boolean white = !pointsOf(Elements.WHITE).isEmpty();
        boolean blackTurn = !pointsOf(Elements.BLACK_TURN).isEmpty();
        boolean black = !pointsOf(Elements.BLACK).isEmpty();

        if (whiteTurn && white
                || blackTurn && black
                || white && black)
        {
            error();
        }

        return whiteTurn || !blackTurn;
    }

    private void error() {
        throw new IllegalArgumentException("Пожалуйста используте либо WHITE_TURN + BLACK, " +
                "либо BLACK_TURN + WHITE");
    }

    private List<Point> pointsOf(Elements... elements) {
        List<Point> result = new LinkedList<>();
        for (int index = 0; index < map.length(); index++) {
            for (Elements el : elements) {
                if (map.charAt(index) == el.ch) {
                    result.add(xy.getXY(index));
                }
            }
        }
        return result;
    }
}
