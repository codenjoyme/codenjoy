package com.codenjoy.dojo.battlecity.client;

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


import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public boolean isBarrierAt(int x, int y) {
        return isAt(x, y, Elements.BATTLE_WALL) ||
                isAt(x, y, Elements.CONSTRUCTION) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_DOWN) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_UP) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_LEFT) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_RIGHT) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_DOWN_TWICE) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_UP_TWICE) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_LEFT_TWICE) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_RIGHT_TWICE) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_LEFT_RIGHT) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_UP_DOWN) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_UP_LEFT) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_RIGHT_UP) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_DOWN_LEFT) ||
                isAt(x, y, Elements.CONSTRUCTION_DESTROYED_DOWN_RIGHT);
    }

    public Point getMe() {
        return get(Elements.TANK_UP,
                Elements.TANK_DOWN,
                Elements.TANK_LEFT,
                Elements.TANK_RIGHT).get(0);
    }

    public boolean isGameOver() {
        return get(Elements.TANK_UP,
                Elements.TANK_DOWN,
                Elements.TANK_LEFT,
                Elements.TANK_RIGHT).isEmpty();
    }

    public boolean isBulletAt(int x, int y) {
        return getAt(x, y).equals(Elements.BULLET);
    }
}