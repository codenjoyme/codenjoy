package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.bomberman.model.Elements.WALL;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 6:08 PM
 */
public class Wall extends PointImpl implements State<Elements, Player> {
    public Wall(int x, int y) {
        super(x, y);
    }

    public Wall(Wall wall) {
        super(wall);
    }

    public Wall copy() {
        return new Wall(this);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return WALL;
    }
}
