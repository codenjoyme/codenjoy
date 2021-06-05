package com.codenjoy.dojo.sudoku.model;

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
import com.codenjoy.dojo.sudoku.client.Element;

public class Cell extends PointImpl implements State<Element, Player> {
    private int number;
    private boolean visible;

    public Cell(Point point, int number, boolean visible) {
        super(point);
        this.number = number;
        this.visible = visible;
    }

    public boolean isHidden() {
        return !visible;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return String.format("pt%s=%s%s", super.toString(), (isHidden())?"-":"+", number);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (visible) {
            return Element.valueOf(number);
        } else {
            if (alsoAtPoint[1] != null) {
                return Element.valueOf(((Cell) alsoAtPoint[1]).getNumber());
            } else {
                return Element.NONE;
            }
        }
    }
}
