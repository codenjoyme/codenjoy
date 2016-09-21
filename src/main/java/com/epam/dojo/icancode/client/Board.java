package com.epam.dojo.icancode.client;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.Point;
import com.epam.dojo.icancode.model.Elements;

public class Board extends AbstractBoard2<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {
        return !(isAt(1, x, y, Elements.EMPTY, Elements.GOLD, Elements.ROBO_OTHER)
                && isAt(0, x, y, Elements.FLOOR, Elements.START, Elements.EXIT, Elements.GOLD));
    }

    public Point getMe() {
        return get(1, Elements.ROBO_FALLING,
                Elements.ROBO).get(0);
    }

    public Point getExit() {
        return get(0, Elements.EXIT).get(0);
    }

    public boolean isGameOver() {
        return !get(1, Elements.ROBO_FALLING).isEmpty();
    }
}