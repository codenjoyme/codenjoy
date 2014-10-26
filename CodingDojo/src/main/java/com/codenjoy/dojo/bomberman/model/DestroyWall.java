package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.bomberman.model.Elements.DESTROY_WALL;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:22 PM
 */
public class DestroyWall extends Wall implements State<Elements, Player> {
    public DestroyWall(int x, int y) {
        super(x, y);
    }

    @Override
    public Wall copy() {
        return new DestroyWall(this.x, this.y);
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return DESTROY_WALL;
    }
}
