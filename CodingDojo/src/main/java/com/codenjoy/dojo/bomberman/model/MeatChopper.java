package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.bomberman.model.Elements.*;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:22 PM
 */
public class MeatChopper extends Wall implements State<Elements, Player> {

    private Direction direction;

    public MeatChopper(int x, int y) {
        super(x, y);
    }

    @Override
    public Wall copy() {
        return new MeatChopper(this.x, this.y);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        Blast blast = null;

        if (alsoAtPoint.length > 1 && alsoAtPoint[1] != null) {
            if (alsoAtPoint[1] instanceof Blast) {
                blast = (Blast)alsoAtPoint[1];
            }
        }

        if (blast != null) {
            return DEAD_MEAT_CHOPPER;
        } else {
            return MEAT_CHOPPER;
        }
    }
}
