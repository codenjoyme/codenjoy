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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;

/**
 * Артефакт Золото на поле
 */
public class Ability extends PointImpl implements State<Elements, Player> {
    public static final int HEALTH_BONUS = 30;
    enum Type {WEAPON, DEFENCE, HEALTH}

    private Type abilityType;

    public Ability(int x, int y, Dice dice) {
        super(x, y);
        int randomChoice = dice.next(Type.values().length);
        for (Type elem : Type.values()){
            if (elem.ordinal() == randomChoice){
                abilityType = elem;
            }
        }
    }

    public Ability(Point point, Type abilityType) {
        super(point);
        this.abilityType = abilityType;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (abilityType == Type.WEAPON){
            return Elements.SUPER_ATTACK;
        }

        if (abilityType == Type.DEFENCE) {
            return Elements.SUPER_DEFENCE;
        }

        return Elements.HEALTH_PACKAGE;
    }

    public Type getAbilityType() {
        return abilityType;
    }
}
