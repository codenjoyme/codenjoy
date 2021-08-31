package com.codenjoy.dojo.sokoban.model.items;

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


import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.joystick.NoActJoystick;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.games.sokoban.Element;
import com.codenjoy.dojo.sokoban.model.Field;
import com.codenjoy.dojo.sokoban.model.Player;

public class Hero extends PlayerHero<Field> implements State<Element, Player>, NoActJoystick {

    private boolean alive;
    private Direction direction;

    public Hero(Point xy) {
        super(xy);
        direction = null;
        alive = true;
    }

    @Override
    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (direction != null) {
            Point dest = direction.change(this);
            Point newDest = direction.change(dest);

            if (!(field.isBarrier(newDest) || field.isMark(newDest))) {
                if (field.isBox(dest)) {
                    field.moveBox(dest, newDest);
                }
                if (field.isBoxOnTheMark(dest)) {
                    field.removeBoxOnTheMark(dest);
                    field.setBox(newDest);
                    field.setMark(dest);
                }
            }
            if (!field.isBarrier(newDest) && field.isMark(newDest)) {
                field.removeBox(dest);
                field.setBoxOnTheMark(newDest);
            }

            if (!field.isBarrier(dest)) {
                move(dest);
            }
        }
        direction = null;
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return Element.HERO;
    }
}
