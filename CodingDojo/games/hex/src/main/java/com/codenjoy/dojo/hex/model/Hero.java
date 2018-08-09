package com.codenjoy.dojo.hex.model;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.joystick.NoActJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

public class Hero extends PlayerHero<Field> implements State<Elements, Player>, NoActJoystick {

    private Direction direction;
    private boolean jump;
    private Player player;
    private Elements element; // TODO надо раобраться мы храним или елемент или плеер

    public Hero(Point xy, Elements element) {
        super(xy);
        this.element = element;
    }

    public Hero(int x, int y, Elements element) {
        super(x, y);
        this.element = element;
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        direction = Direction.UP;
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
    }

    @Override
    public void tick() {
        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (jump) {
                newX = direction.changeX(newX);
                newY = direction.changeY(newY);
            }

            if (!field.isBarrier(newX, newY)) {
                if (jump) {
                    field.jumpHero(newX, newY, this);
                } else {
                    field.addHero(newX, newY, this);
                }
            }
        }
        direction = null;
        jump = false;
    }

    public void isJump(boolean jump) {
        this.jump = jump;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (player == this.player) {
            return Elements.MY_HERO;
        } else {
            return this.player.getElement();
        }
    }

    protected void newOwner(Player player) {
        this.player = player;
        if (player == null) {
            element = null;
        } else {
            this.element = player.getElement();
        }
    }

    public Elements getElement() {
        return element;
    }
}

