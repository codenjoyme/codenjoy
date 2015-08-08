package com.codenjoy.dojo.football.model.elements;

import com.codenjoy.dojo.football.model.Elements;
import com.codenjoy.dojo.football.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Артефакт Стена/Внешняя разметка на поле
 */
public class Wall extends PointImpl implements State<Elements, Player> {

    public Wall(int x, int y) {
        super(x, y);
    }

    public Wall(Point point) {
        super(point);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.WALL;
    }
}
