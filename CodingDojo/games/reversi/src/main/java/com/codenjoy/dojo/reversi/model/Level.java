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


import com.codenjoy.dojo.games.reversi.Element;
import com.codenjoy.dojo.reversi.model.items.Break;
import com.codenjoy.dojo.reversi.model.items.Chip;
import com.codenjoy.dojo.services.field.AbstractLevel;

import java.util.LinkedHashMap;
import java.util.List;

import static com.codenjoy.dojo.games.reversi.Element.*;

public class Level extends AbstractLevel {

    public Level(String map) {
        super(map);
    }

    public List<Break> breaks(Field field) {
        return find(Break::new, BREAK);
    }

    public List<Chip> chips(Field field) {
        return find(new LinkedHashMap<>() {{
            put(WHITE, pt -> new Chip(true, pt, field));
            put(WHITE_TURN, pt -> new Chip(true, pt, field));
            put(BLACK, pt -> new Chip(false, pt, field));
            put(BLACK_TURN, pt -> new Chip(false, pt, field));
        }});
    }

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

    private boolean exists(Element element) {
        return !find( pt -> pt, element).isEmpty();
    }

    private void error() {
        throw new IllegalArgumentException("Пожалуйста используте либо WHITE_TURN + BLACK, " +
                "либо BLACK_TURN + WHITE");
    }
}