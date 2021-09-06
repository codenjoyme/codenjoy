package com.codenjoy.dojo.snakebattle.model.objects;

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
import com.codenjoy.dojo.games.snakebattle.Element;
import com.codenjoy.dojo.snakebattle.model.Player;

/**
 * Артефакт "Пилюля ярости" на поле (позволяет жевать всё и всех)
 */
public class FuryPill extends PointImpl implements State<Element, Player> {

    public FuryPill(Point point) {
        super(point);
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return Element.FURY_PILL;
    }
}
