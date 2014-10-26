package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.bomberman.model.Elements.*;
import static com.codenjoy.dojo.bomberman.model.Elements.BOOM;

/**
 * User: sanja
 * Date: 20.04.13
 * Time: 15:46
 */
public class Blast extends PointImpl implements State<Elements, Player> {

    private Bomberman bomberman;

    public Blast(int x, int y, Bomberman bomberman) {
        super(x, y);
        this.bomberman = bomberman;
    }

    public boolean itsMine(Bomberman bomberman) {
        return this.bomberman == bomberman;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return BOOM;
    }
}
