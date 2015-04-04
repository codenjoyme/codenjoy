package com.codenjoy.dojo.battlecity.client;

import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public List<Point> getBarriers() {
        List<Point> all = getWalls();
        return removeDuplicates(all);
    }

    public List<Point> getWalls() {
        return findAll(Elements.BATTLE_WALL);
    }

    public boolean isBarrierAt(int x, int y) {
        return getBarriers().contains(pt(x, y));
    }

    public Point getMe() {
        return get(Elements.TANK_UP, Elements.TANK_DOWN, Elements.TANK_LEFT, Elements.TANK_RIGHT).get(0);
    }

    public boolean isGameOver() {
        return get(Elements.TANK_UP, Elements.TANK_DOWN, Elements.TANK_LEFT, Elements.TANK_RIGHT).isEmpty();
    }
}