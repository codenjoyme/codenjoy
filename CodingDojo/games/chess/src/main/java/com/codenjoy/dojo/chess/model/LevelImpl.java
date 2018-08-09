package com.codenjoy.dojo.chess.model;

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


import com.codenjoy.dojo.chess.model.figures.*;
import com.codenjoy.dojo.services.LengthToXY;

import java.util.LinkedList;
import java.util.List;

public class LevelImpl implements Level {
    private final LengthToXY xy;

    private String map;

    public LevelImpl(String map) {
        this.map = map;
        xy = new LengthToXY(getSize());
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Figure> getFigures(boolean isWhite) {
        List<Figure> result = new LinkedList<Figure>();
        for (int index = 0; index < map.length(); index++) {
            if (isWhite) {
                if (map.charAt(index) == Elements.WHITE_FERZ.ch) result.add(new Ferz(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_KON.ch) result.add(new Kon(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_KOROL.ch) result.add(new Korol(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_LADIA.ch) result.add(new Ladia(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_PESHKA.ch) result.add(new Peshka(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.WHITE_SLON.ch) result.add(new Slon(xy.getXY(index), isWhite));
            } else {
                if (map.charAt(index) == Elements.BLACK_FERZ.ch) result.add(new Ferz(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_KON.ch) result.add(new Kon(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_KOROL.ch) result.add(new Korol(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_LADIA.ch) result.add(new Ladia(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_PESHKA.ch) result.add(new Peshka(xy.getXY(index), isWhite));
                if (map.charAt(index) == Elements.BLACK_SLON.ch) result.add(new Slon(xy.getXY(index), isWhite));
            }
        }
        return result;
    }

    private char upper(char ch) {
        return ("" + ch).toUpperCase().charAt(0);
    }
}
