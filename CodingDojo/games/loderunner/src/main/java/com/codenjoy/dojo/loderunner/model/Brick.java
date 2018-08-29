package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

public class Brick extends PointImpl implements Tickable, State<Elements, Player> {

    public static final int DRILL_TIMER = 13;
    public static final Brick NULL = new Brick(pt(-1, -1));
    private int drill;

    private Hero drilledBy;

    public Brick(Point xy) {
        super(xy);
        drill = -1;
    }

    public void drill(Hero hero) {
        this.drilledBy = hero;
        drill = 0;
    }

    @Override
    public void tick() {
        if (drill == -1) {
            drilledBy = null;
        }

        if (drill != -1) {
            drill++;
            if (drill == DRILL_TIMER) {
                drill = -1;
            }
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (drill == 1) {
            return Elements.DRILL_PIT;
        } else if (drill > 1) {
            switch (Brick.DRILL_TIMER - drill) {
                case 1 : return Elements.PIT_FILL_1;
                case 2 : return Elements.PIT_FILL_2;
                case 3 : return Elements.PIT_FILL_3;
                case 4 : return Elements.PIT_FILL_4;
                default: return Elements.NONE;
            }
        } else {
            return Elements.BRICK;
        }
    }

    public Hero getDrilledBy() {
        return drilledBy;
    }
}
