package com.codenjoy.dojo.pong.model;

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

import com.codenjoy.dojo.games.pong.Element;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Wall extends PointImpl implements State<Element, Player>, Barrier {

    BarrierOrientation orientation;

    public Wall(Point pt, BarrierOrientation orientation) {
        super(pt);
        this.orientation = orientation;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        switch (orientation) {
            case VERTICAL:
                return Element.VERTICAL_WALL;
            case HORIZONTAL:
                return Element.HORIZONTAL_WALL;
            default:
                return Element.HORIZONTAL_WALL;
        }
    }

    @Override
    public BarrierOrientation getOrientation() {
        return orientation;
    }
}
