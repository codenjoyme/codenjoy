package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.bomberman.model.Elements.DEAD_MEAT_CHOPPER;
import static com.codenjoy.dojo.bomberman.model.Elements.MEAT_CHOPPER;

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
