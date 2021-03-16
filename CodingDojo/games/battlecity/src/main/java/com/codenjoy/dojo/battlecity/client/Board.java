package com.codenjoy.dojo.battlecity.client;

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


import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;

import java.util.List;

import static com.codenjoy.dojo.battlecity.model.Elements.*;
import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    @Override
    protected int inversionY(int y) { // TODO разобраться с этим чудом
        return size - 1 - y;
    }

    public boolean isBarrierAt(int x, int y) {
        if (isOutOfField(x, y)) {
            return true;
        }

        return getBarriers().contains(pt(x, y));
    }

    public List<Point> getBarriers() {
        return get(BATTLE_WALL,
                WALL,
                WALL_DESTROYED_DOWN,
                WALL_DESTROYED_UP,
                WALL_DESTROYED_LEFT,
                WALL_DESTROYED_RIGHT,
                WALL_DESTROYED_DOWN_TWICE,
                WALL_DESTROYED_UP_TWICE,
                WALL_DESTROYED_LEFT_TWICE,
                WALL_DESTROYED_RIGHT_TWICE,
                WALL_DESTROYED_LEFT_RIGHT,
                WALL_DESTROYED_UP_DOWN,
                WALL_DESTROYED_UP_LEFT,
                WALL_DESTROYED_RIGHT_UP,
                WALL_DESTROYED_DOWN_LEFT,
                WALL_DESTROYED_DOWN_RIGHT);
    }

    public Point getMe() {
        List<Point> points = get(TANK_UP,
                TANK_DOWN,
                TANK_LEFT,
                TANK_RIGHT);
        if (points.isEmpty()) {
            return null;
        }
        return points.get(0);
    }

    public List<Point> getEnemies() {
        return get(AI_TANK_UP,
                AI_TANK_DOWN,
                AI_TANK_LEFT,
                AI_TANK_RIGHT,
                OTHER_TANK_UP,
                OTHER_TANK_DOWN,
                OTHER_TANK_LEFT,
                OTHER_TANK_RIGHT,
                AI_TANK_PRIZE);
    }

    public List<Point> getBullets() {
        return get(BULLET);
    }

    public boolean isGameOver() {
        return get(TANK_UP,
                TANK_DOWN,
                TANK_LEFT,
                TANK_RIGHT).isEmpty();
    }

    public Elements getAt(int x, int y) {
        if (isOutOfField(x, y)) {
            return BATTLE_WALL;
        }
        return super.getAt(x, y);
    }

    public boolean isBulletAt(int x, int y) {
        return getAt(x, y).equals(BULLET);
    }

    @Override
    public String toString() {
        return String.format("%s\n" +
                        "My tank at: %s\n" +
                        "Enemies at: %s\n" +
                        "Bullets at: %s\n",
                boardAsString(),
                getMe(),
                getEnemies(),
                getBullets());
    }
}
