package com.codenjoy.dojo.battlecity.client;

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