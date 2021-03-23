package com.codenjoy.dojo.hex.client;

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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.hex.model.Elements;
import com.codenjoy.dojo.services.Point;

import java.util.Collection;
import java.util.List;

import static com.codenjoy.dojo.hex.model.Elements.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public Collection<Point> getBarriers() {
        return get(WALL,
                MY_HERO,
                HERO1,
                HERO2,
                HERO3,
                HERO4,
                HERO5,
                HERO6,
                HERO7,
                HERO8,
                HERO9,
                HERO10,
                HERO11);
    }

    public Collection<Point> getWalls() {
        return get(WALL);
    }

    public boolean isBarrierAt(int x, int y) {
        Point pt = pt(x, y);
        return getBarriers().contains(pt) || pt.isOutOf(size());
    }

    public boolean isGameOver() {
        return get(MY_HERO).isEmpty() || get(NONE).isEmpty();
    }
}
