package com.codenjoy.dojo.clifford.model.items;

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


import com.codenjoy.dojo.games.loderunner.Element;
import com.codenjoy.dojo.clifford.model.Hero;
import com.codenjoy.dojo.clifford.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

public class Brick extends PointImpl implements Tickable, State<Element, Player> {

    // TODO move to settings
    public static int CRACK_TIMER = 13;
    public static final Brick NULL = new Brick(pt(-1, -1));
    private int crack;

    private Hero crackedBy;

    public Brick(Point xy) {
        super(xy);
        crack = -1;
    }

    public void crack(Hero hero) {
        this.crackedBy = hero;
        crack = 0;
    }

    @Override
    public void tick() {
        if (crack == -1) {
            crackedBy = null;
        }

        if (crack != -1) {
            crack++;
            if (crack == CRACK_TIMER) {
                crack = -1;
            }
        }
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (crack == 1) {
            return Element.CRACK_PIT;
        } else if (crack > 1) {
            switch (Brick.CRACK_TIMER - crack) {
                case 1 : return Element.PIT_FILL_1;
                case 2 : return Element.PIT_FILL_2;
                case 3 : return Element.PIT_FILL_3;
                case 4 : return Element.PIT_FILL_4;
                default: return Element.NONE;
            }
        } else {
            return Element.BRICK;
        }
    }

    public Hero getCrackedBy() {
        return crackedBy;
    }
}
