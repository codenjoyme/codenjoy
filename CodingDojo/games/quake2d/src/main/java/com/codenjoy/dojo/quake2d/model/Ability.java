package com.codenjoy.dojo.quake2d.model;

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

import com.codenjoy.dojo.games.quake2d.Element;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

import static com.codenjoy.dojo.quake2d.model.Ability.Type.*;

public class Ability extends PointImpl implements State<Element, Player> {
    public static final int HEALTH_BONUS = 30;
    enum Type {WEAPON, DEFENCE, HEALTH}

    private Type type;

    public Ability(Point pt, Dice dice) {
        super(pt);
        int randomChoice = dice.next(Type.values().length);
        for (Type elem : Type.values()){
            if (elem.ordinal() == randomChoice){
                type = elem;
            }
        }
    }

    public Ability(Point pt, Element element) {
        super(pt);
        if (element == Element.SUPER_WEAPON) {
            type = WEAPON;
        } else if (element == Element.SUPER_DEFENCE) {
            type = DEFENCE;
        } else if (element == Element.HEALTH_PACKAGE) {
            type = HEALTH;
        }
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        if (type == WEAPON){
            return Element.SUPER_WEAPON;
        }

        if (type == Type.DEFENCE) {
            return Element.SUPER_DEFENCE;
        }

        if (type == Type.HEALTH) {
            return Element.HEALTH_PACKAGE;
        }

        throw new RuntimeException("Unknown ability type: " + type);
    }

    public Type getType() {
        return type;
    }
}
