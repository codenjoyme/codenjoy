package com.codenjoy.dojo.crossyroad.model;

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


import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.services.*;

public class Hero extends PlayerHero<Field> implements State<Elements, Player> {

    private boolean alive;
    private Direction direction;

    public Hero(Point xy) {
        super(xy);
        direction = Direction.STOP;
        alive = true;
    }

    //смена направления героя
    @Override
    public void down() {/* do nothing, this should never happen*/}

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
    public void act(int... p) {
        if (!alive) return;
    }

    @Override
    public void tick() {
        if (!alive) return;
        if (direction != null) {
            //приращение по x y по значению передаваемой точки, если напрваление не вверх
            // то есть, direction героя смениться на UP, но вперед он идти не будет,
            // только влево-вправо
            if (direction != Direction.UP) {
                Point to = direction.change(this.copy());
                move(to);
            }
        }
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (alive) {
            if (direction == Direction.UP) {
                return Elements.HEROUP;
            } else if (direction == Direction.LEFT) {
                return Elements.HEROLEFT;
            } else if (direction == Direction.RIGHT) {
                return Elements.HERORIGHT;
            } else
                return Elements.HEROSTOP;
        } else {
            return Elements.BLACK_HERO;
        }
    }

    public void dies() {
        alive = true; // Чтобы герой возрождался после смерти alive = true
        teleport();
    }

    public void teleport() {
        direction = Direction.STOP;
        move(10, 2); // TODO сделать расчет точки респавна зависимым от размера уровня
            }

    public Direction getDirection() {
        return this.direction;
    }

}
