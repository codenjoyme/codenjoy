package com.codenjoy.dojo.rubicscube.model;

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


import com.codenjoy.dojo.games.rubicscube.Element;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

public class Cell extends PointImpl implements State<Element, Player> {
    private Element color;

    public Cell(Point point, Element color) {
        super(point);
        this.color = color;
    }

    @Override
    public String toString() {
        return String.format("pt%s=%s", super.toString(), color.ch());
    }

    public char getColor() {
        return color.ch();
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return Element.valueOf(this.getColor());
    }
}
