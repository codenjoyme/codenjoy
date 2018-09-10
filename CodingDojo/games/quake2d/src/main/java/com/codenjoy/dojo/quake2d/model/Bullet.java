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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

/**
 * Артефакт Бомба на поле
 */
public class Bullet extends PointImpl implements Tickable, State<Elements, Player> {
    public static final int START_DAMAGE = 10;
    public static final int WEAPON_MULTIPLICATOR = 2;
    private Direction direction;

    private Field field;
    private int damage;

    public Field getField() {
        return field;
    }

    public Bullet(int x, int y, Direction direction, Field field, Hero hero) {
        super(x, y);
        this.direction = direction;
        this.damage = ((hero.getAbility() != null && hero.getAbility().getAbilityType() == Ability.Type.WEAPON) ? START_DAMAGE*WEAPON_MULTIPLICATOR : START_DAMAGE);

        this.field = field;
    }

    public Direction getDirection() {
        return direction;
    }
//    public Bullet(Point point) {
//        super(point);
//    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BULLET;
    }

    @Override
    public void tick() {
        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y);

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
//        direction = null;
    }

    public int getDamage() {
        return damage;
    }
}
