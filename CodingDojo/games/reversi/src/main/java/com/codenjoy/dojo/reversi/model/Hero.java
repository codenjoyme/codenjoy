package com.codenjoy.dojo.reversi.model;

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


import com.codenjoy.dojo.services.joystick.ActJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class Hero extends PlayerHero<Field> implements ActJoystick {

    private boolean act;
    private boolean color;

    public Hero(boolean color) {
        this.color = color;
        this.act = false;
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void act(int... p) {
        if (p.length < 2) {
            act = false;
            return;
        }

        this.x = p[0];
        this.y = p[1];

        if (isOutOf(field.size())) {
            return;
        }

        act = true;
    }

    @Override
    public void tick() {
        if (field.stop()) {
            act = false;
        }

        if (act) {
            if (field.currentColor() == color && !field.isBreak(x, y)) {
                field.setChip(color, x, y);
            }
            act = false;
        }
    }

    public boolean isAlive() {
        return true;
    }

    public boolean color() {
        return color;
    }
}
