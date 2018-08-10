package com.codenjoy.dojo.quadro.model.items;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.quadro.model.Elements;
import com.codenjoy.dojo.quadro.model.Player;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Артефакт: Фишка на поле
 */
public class Chip extends PointImpl implements State<Elements, Player> {

    public static final Chip NULL = new Chip(false, pt(-1, -1)) {
        @Override
        public boolean itsMyColor(boolean color) {
            return false;
        }
    };

    private boolean color;

    public Chip(boolean color, int x, int y) {
        super(x, y);
        this.color = color;
    }

    public Chip(boolean color, Point point) {
        super(point);
        this.color = color;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (color) {
            return Elements.YELLOW;
        } else {
            return Elements.RED;
        }
    }

    public boolean getColor() {
        return color;
    }

    public boolean itsMyColor(boolean color) {
        return this.color == color;
    }
}
