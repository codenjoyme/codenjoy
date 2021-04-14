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
import com.codenjoy.dojo.utils.LevelUtils;

import java.util.HashMap;
import java.util.List;

import static com.codenjoy.dojo.reversi.model.Elements.*;

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
    public List<Break> breaks(Field field) {
        return LevelUtils.getObjects(xy, map,
                Break::new,
                BREAK);
    }

    @Override
    public List<Chip> chips(Field field) {
        return LevelUtils.getObjects(xy, map,
                new HashMap<>(){{
                    put(WHITE,      pt -> new Chip(true, pt, field));
                    put(WHITE_TURN, pt -> new Chip(true, pt, field));
                    put(BLACK,      pt -> new Chip(false, pt, field));
                    put(BLACK_TURN, pt -> new Chip(false, pt, field));
                }});
    }

    @Override
    public boolean currentColor() {
        boolean whiteTurn = exists(WHITE_TURN);
        boolean white = exists(WHITE);
        boolean blackTurn = exists(BLACK_TURN);
        boolean black = exists(BLACK);

        if (whiteTurn && white
                || blackTurn && black
                || white && black)
        {
            error();
        }

        return whiteTurn || !blackTurn;
    }

    private boolean exists(Elements element) {
        return !LevelUtils.getObjects(xy, map, pt -> pt, element).isEmpty();
    }

    private void error() {
        throw new IllegalArgumentException("Пожалуйста используте либо WHITE_TURN + BLACK, " +
                "либо BLACK_TURN + WHITE");
    }
    
}
